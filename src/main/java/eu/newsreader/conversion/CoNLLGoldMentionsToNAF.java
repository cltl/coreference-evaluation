package eu.newsreader.conversion;

import eu.kyotoproject.kaf.CorefTarget;
import eu.kyotoproject.kaf.KafEvent;
import eu.kyotoproject.kaf.KafSaxParser;
import eu.kyotoproject.kaf.KafTerm;
import eu.newsreader.util.Util;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by piek on 02/03/16.
 */
public class CoNLLGoldMentionsToNAF {


    static public void main (String[] args) {
          String coNLLPath = "/Users/piek/Desktop/NWR/benchmark/ecb/nwr/ecb+topic+sw.key";
          String nafFilePath = "/Users/piek/Desktop/NWR/benchmark/ecb/nwr/data/ecb_pip.gold";
          for (int i = 0; i < args.length; i++) {
              String arg = args[i];
              if (arg.equals("--CoNLL") && args.length>(i+1)) {
                  coNLLPath = args[i+1];
              }
              else if (arg.equals("--naf") && args.length>(i+1)) {
                  nafFilePath = args[i+1];
              }
          }
          File coNLLFile = new File(coNLLPath);
          File nafFile = new File(nafFilePath);
          HashMap<String, ArrayList<CoNLLdata>> conllDataMap = readCoNLLFile(coNLLFile);
          System.out.println("conllDataMap.size() = "+conllDataMap.size());
          ArrayList<File> nafFiles = Util.makeRecursiveFileList(nafFile, ".xml");
          for (int i = 0; i < nafFiles.size(); i++) {
            File file = nafFiles.get(i);
              System.out.println("file.getName() = " + file.getName());
            processNafFile(file, conllDataMap);
          }
          //readCoNLLFile(coNLLFile, nafFile);
    }

    static void removeNew (KafSaxParser kafSaxParser) {
        ArrayList<KafEvent> newEvents = new ArrayList<KafEvent>();
        for (int i = 0; i < kafSaxParser.kafEventArrayList.size(); i++) {
            KafEvent kafEvent = kafSaxParser.kafEventArrayList.get(i);
             if (!kafEvent.getStatus().equalsIgnoreCase("new")) {
                 newEvents.add(kafEvent);
             }
        }
        kafSaxParser.kafEventArrayList = newEvents;
    }

    static void processNafFile (File pathToNafFile, HashMap<String, ArrayList<CoNLLdata>> conllDataMap) {
        String fileid = pathToNafFile.getName();
        int idx = fileid.indexOf(".");
        if (idx>-1) {
            fileid = fileid.substring(0, idx);
        }

        if (conllDataMap.containsKey(fileid)) {
            ArrayList<String> matchedEvents = new ArrayList<String>();
            KafSaxParser kafSaxParser = new KafSaxParser();
            kafSaxParser.parseFile(pathToNafFile);
            removeNew(kafSaxParser);
            ArrayList<CoNLLdata> coNLLdataArrayList = conllDataMap.get(fileid);
            for (int i = 0; i < kafSaxParser.kafEventArrayList.size(); i++) {
                KafEvent kafEvent = kafSaxParser.kafEventArrayList.get(i);
                ArrayList<String> spanIds = kafEvent.getSpanIds();
                boolean match = false;
                for (int j = 0; j < spanIds.size(); j++) {
                    String s = spanIds.get(j);
                    KafTerm kafTerm = kafSaxParser.getTerm(s);
                    if (kafTerm!=null) {
                        for (int k = 0; k < kafTerm.getSpans().size(); k++) {
                            String tokenId = kafTerm.getSpans().get(k);
                           // System.out.println("tokenId = " + tokenId);
                            for (int l = 0; l < coNLLdataArrayList.size(); l++) {
                                CoNLLdata coNLLdata = coNLLdataArrayList.get(l);
                              //  System.out.println("coNLLdata.getTokenId() = " + coNLLdata.getTokenId());
                                if (coNLLdata.getTokenId().equals(tokenId)) {
                                    matchedEvents.add(tokenId);
                                    kafEvent.setStatus("true");
                                  //  System.out.println("true = " + tokenId);
                                    match = true;
                                    break;
                                }
                            }
                        }
                    }
                    else {
                        System.out.println("cannot find term for s = " + s);
                    }
                }
                if (!match) {
                    kafEvent.setStatus("false");
                   // System.out.println("wrong kafEvent = " + kafEvent.getId());
                }
            }
            int n = kafSaxParser.kafEntityArrayList.size()+100;
            for (int i = 0; i < coNLLdataArrayList.size(); i++) {
                CoNLLdata coNLLdata = coNLLdataArrayList.get(i);
                if (!matchedEvents.contains(coNLLdata.getTokenId())) {
                    KafTerm kafTerm = kafSaxParser.getTermForWordId(coNLLdata.getTokenId());
                    if (kafTerm != null) {
                        KafEvent kafEvent = new KafEvent();
                        n++;
                        kafEvent.setId("p" + n);
                        kafEvent.setStatus("new");
                      //  System.out.println("new = " + kafTerm.getTid());
                        CorefTarget corefTarget = new CorefTarget();
                        corefTarget.setId(kafTerm.getTid());
                        kafEvent.addSpan(corefTarget);
                        kafSaxParser.kafEventArrayList.add(kafEvent);
                    }
                }
            }
            try {
                OutputStream fos = new FileOutputStream(pathToNafFile);
                kafSaxParser.writeNafToStream(fos);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("cannot find fileid = " + fileid);
        }


    }
    /**
     *
     *
     */
    static HashMap<String, ArrayList<CoNLLdata>> readCoNLLFile (File coNLLfile) {
        /*
        1_10ecb	0	1	Perennial	-
        1_10ecb	0	2	party	-
        1_10ecb	0	3	girl	-
        1_10ecb	0	4	Tara	-
        1_10ecb	0	5	Reid	-
        1_10ecb	0	6	checked	(132016236402809085484)
        1_10ecb	0	7	herself	-
        1_10ecb	0	8	into	(132016236402809085484)
         */
        HashMap<String, ArrayList<CoNLLdata>> conllDataMap = new HashMap<String,ArrayList<CoNLLdata>>();
        try {
            FileInputStream fis = new FileInputStream(coNLLfile);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader in = new BufferedReader(isr);
            String inputLine = "";
            while (in.ready() && (inputLine = in.readLine()) != null) {
                String[] fields = inputLine.split("\t");
                if (fields.length == 5) {
                    String fileId = fields[0];
                    // System.out.println("fileId = " + fileId);
                    String sentenceId = fields[1];
                    String tokenId = "w" + fields[2];
                    String token = fields[3];
                    String tag = fields[4];
                    if (!tag.equals("-")) {
                        CoNLLdata coNLLdata = new CoNLLdata(fileId, sentenceId, tokenId, token, tag);
                        if (conllDataMap.containsKey(fileId)) {
                            ArrayList<CoNLLdata> coNLLdataList = conllDataMap.get(fileId);
                            coNLLdataList.add(coNLLdata);
                            conllDataMap.put(fileId, coNLLdataList);
                        }
                        else {
                            ArrayList<CoNLLdata> coNLLdataList = new ArrayList<CoNLLdata>();
                            coNLLdataList.add(coNLLdata);
                            conllDataMap.put(fileId, coNLLdataList);
                        }
                    }
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return conllDataMap;
    }

    /**
     *
     *
     */
    static void readCoNLLFile (File coNLLfile, File kafFile) {
        /*
        1_10ecb	0	1	Perennial	-
        1_10ecb	0	2	party	-
        1_10ecb	0	3	girl	-
        1_10ecb	0	4	Tara	-
        1_10ecb	0	5	Reid	-
        1_10ecb	0	6	checked	(132016236402809085484)
        1_10ecb	0	7	herself	-
        1_10ecb	0	8	into	(132016236402809085484)
         */
        KafSaxParser kafSaxParser = new KafSaxParser();
        kafSaxParser.parseFile(kafFile);
       // System.out.println("kafFile.getName() = " + kafFile.getName());
        HashMap<String, CoNLLdata> conllDataMap = new HashMap<String,CoNLLdata>();
        ArrayList<String> matchedEvents = new ArrayList<String>();
        try {
            FileInputStream fis = new FileInputStream(coNLLfile);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader in = new BufferedReader(isr);
            String inputLine = "";
            while (in.ready() && (inputLine = in.readLine()) != null) {
                String[] fields = inputLine.split("\t");
                if (fields.length == 5) {
                    String fileId = fields[0];
                   // System.out.println("fileId = " + fileId);
                    if (kafFile.getName().startsWith(fileId) &&
                        !kafFile.getName().startsWith(fileId+"plus")) {
                        String sentenceId = fields[1];
                        String tokenId = "w"+fields[2];
                        String token = fields[3];
                        String tag = fields[4];
                        if (!tag.equals("-")) {
                          //  System.out.println("tokenId = " + tokenId);
                            KafTerm term = kafSaxParser.getTermForWordId(tokenId);
                            if (term != null) {
                                CoNLLdata coNLLdata = new CoNLLdata(fileId, sentenceId, tokenId, token, tag);
                                conllDataMap.put(term.getTid(), coNLLdata);
                            }
                            else {
                             //   System.out.println("no term for tokenId = " + tokenId);
                            }
                        }
                    }
                }
            }
            in.close();
            System.out.println("conllDataMap = " + conllDataMap.size());
            for (int i = 0; i < kafSaxParser.kafEventArrayList.size(); i++) {
                KafEvent kafEvent = kafSaxParser.kafEventArrayList.get(i);
                ArrayList<String> spanIds = kafEvent.getSpanIds();
                boolean match = false;
                for (int j = 0; j < spanIds.size(); j++) {
                    String s = spanIds.get(j);
                    if (conllDataMap.containsKey(s)) {
                        matchedEvents.add(s);
                        kafEvent.setStatus("true");
                        System.out.println("true = " + s);
                    }
                }
                if (!match) {
                    kafEvent.setStatus("false");
                }
            }
            Set keySet = conllDataMap.keySet();
            Iterator<String> keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                if (!matchedEvents.contains(key)) {
                    KafEvent kafEvent = new KafEvent();
                    int n = kafSaxParser.kafEntityArrayList.size();
                    kafEvent.setId("p"+n);
                    kafEvent.setStatus("new");
                    System.out.println("new = " + key);
                    CorefTarget corefTarget = new CorefTarget();
                    corefTarget.setId(key);
                    kafEvent.addSpan(corefTarget);
                    kafSaxParser.kafEventArrayList.add(kafEvent);
                }
            }
          //  kafSaxParser.writeNafToStream(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
