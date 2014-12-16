package eu.newsreader.conversion;

import eu.newsreader.util.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by piek on 12/15/14.
 */
public class ReduceConllResponse {

    static public void main (String[] args) {
        try {
            String pathToKeyFolder = "";
            String pathToResponseFolder = "";
            pathToKeyFolder = "/Users/piek/Desktop/NWR/NWR-benchmark/coreference/corpus_CONLL/corpus_airbus/events/key";
            pathToResponseFolder = "/Users/piek/Desktop/NWR/NWR-benchmark/coreference/corpus_CONLL/corpus_airbus/events/response";
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg.equalsIgnoreCase("--key") && args.length>(i+1)) {
                    pathToKeyFolder = args[i+1];
                }
                else if (arg.equalsIgnoreCase("--response") && args.length>(i+1)) {
                    pathToResponseFolder = args[i+1];
                }
            }


            ArrayList<File> keyFiles = Util.makeFlatFileList(new File(pathToKeyFolder));
            ArrayList<File> responseFiles = Util.makeFlatFileList(new File(pathToResponseFolder));
            System.out.println("keyFiles = " + keyFiles.size());
            System.out.println("responseFiles = " + responseFiles.size());
            for (int i = 0; i < keyFiles.size(); i++) {
                File keyFile = keyFiles.get(i);
                ArrayList<String> sentenceIds =  Util.getSentenceIdsConllFile(keyFile);
              //  System.out.println("sentenceIds.toString() = " + sentenceIds.toString());
                String keyS1 = Util.readFirstSentence(keyFile);
                boolean MATCH = false;
                for (int j = 0; j < responseFiles.size(); j++) {
                    File responseFile = responseFiles.get(j);
                    String responseS1 = Util.readFirstSentence(responseFile);
                    if (keyS1.equals(responseS1)) {
                        String reducedResponse = Util.reduceConllFileForSentenceIds(responseFile, sentenceIds);
                       // System.out.println("reducedResponse = " + reducedResponse);
                        OutputStream responseFos = new FileOutputStream(responseFile);
                        responseFos.write(reducedResponse.getBytes());
                        responseFos.close();
                        MATCH = true;
                        break;
                    }
                }
                if (!MATCH) {
                    System.out.println("NO MATCH for keyFile = " + keyFile.getName());
                    System.out.println("sentenceIds = " + sentenceIds.toString());
                }
               // break;
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }


}
