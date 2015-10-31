package eu.newsreader.conversion;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.tdb.TDBFactory;
import eu.newsreader.util.Util;
import org.apache.jena.riot.RDFDataMgr;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by piek on 18/10/15.
 */
public class SemCoref {


        static final String instanceGraph = "http://www.newsreader-project.eu/instances";


        static public HashMap<String, String>  readSemTrig (String trigFolder) {
            HashMap<String, String> tokenIdMap = new HashMap<String, String>();
            ArrayList<File> trigFiles = new ArrayList<File>();
            trigFiles = Util.makeRecursiveFileList(new File(trigFolder), ".trig");
            System.out.println("trigFiles.size() = " + trigFiles.size());

            ArrayList<String> instanceTriples = new ArrayList<String>();

            for (int i = 0; i < trigFiles.size(); i++) {
                File file = trigFiles.get(i);
                if (i%50==0) {
                    System.out.println(i+": file.getName() = " + file.getParentFile().getName());
                }
                Dataset dataset = TDBFactory.createDataset();
                dataset = RDFDataMgr.loadDataset(file.getAbsolutePath());
                Iterator<String> it = dataset.listNames();
                while (it.hasNext()) {
                    String name = it.next();
                    if (name.equals(instanceGraph)) {
                        Model namedModel = dataset.getNamedModel(name);
                        StmtIterator siter = namedModel.listStatements();
                        while (siter.hasNext()) {
                            Statement s = siter.nextStatement();
                            updateTokenMap(tokenIdMap, s);
                        }
                    }
                }
                dataset.close();
            }
            return tokenIdMap;
        }

        static void updateTokenMap (HashMap<String, String> tokenIdMap, Statement s) {
            String predicate = s.getPredicate().getURI();
            String subject = s.getSubject().getURI();
            String object = "";
            if (s.getObject().isLiteral()) {
                object = s.getObject().asLiteral().toString();
            }
            else if (s.getObject().isURIResource()) {
                object = s.getObject().asResource().getURI();
            }

            int idx = predicate.lastIndexOf("/");
            if (idx>-1) predicate = predicate.substring(idx+1);
            idx = subject.lastIndexOf("/");
            if (idx>-1) subject = subject.substring(idx+1);
            idx = object.lastIndexOf("/");
            if (idx>-1) object = object.substring(idx+1);

            if (predicate.endsWith("denotedBy")) {
                if (subject.indexOf("#ev")>-1) {
                    String token = "";
                    String id = "";
                    String file = "";
                    String sentence = "";
                    file = Util.getFileFromMention(object);
                    sentence = Util.getSentence(object);
                    token = Util.getTokenId(object);
                    id = Util.getNumericId(subject);
                    //1_10ecbplus.xml.naf.fix.xml#ev27
                    //1_10ecbplus.xml.naf.fix.xml#char=592,597&word=w116&term=t116&sentence=5
                    // get ID
                    // get tokens
                    if (!token.isEmpty()) {
                        String tokenKey = file+"\t"+sentence+"\t"+token;
                        if (tokenIdMap.containsKey(tokenKey)) {
                            System.out.println("id = " + id);
                            System.out.println("token = " + token);
                            System.out.println("subject = " + subject);
                            System.out.println("object = " + object);
                        } else {
                            tokenIdMap.put(tokenKey, id);
                        }
                    }
                }
            }
        }

    static public void main (String[] args) {
        HashMap<String, String> tokenIdMap = new HashMap<String, String>();
        String trigfolder = "";
        String conllFile = "";
        trigfolder = "/Users/piek/Desktop/NWR/NWR-benchmark/ecb/data/ecb_pip.v3/2" ;
        conllFile = "/Users/piek/Desktop/NWR/NWR-benchmark/ecb/cross-document/topic.2.key";
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("--trig-folder") && args.length>(i+1)) {
                trigfolder = args[i+1];
            }
            else if (arg.equals("--conll-file") && args.length>(i+1)) {
                conllFile = args[i+1];
            }
        }
        tokenIdMap = readSemTrig(trigfolder);
        addSemToCoNLL(new File(conllFile), tokenIdMap);
    }


    static public void addSemToCoNLL (File file, HashMap<String, String> tokenIdMap){
        try {
            OutputStream fos = new FileOutputStream(file.getAbsoluteFile()+".xdoc");
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader in = new BufferedReader(isr);
            String inputLine = "";
            String fileName = file.getName();
            int idx = fileName.indexOf(".");
            if (idx>-1) {
                fileName = fileName.substring(0, idx);
            }
            HashMap<String, ArrayList<String>> labelSet = new HashMap<String, ArrayList<String>>();
            HashMap<String, ArrayList<String>> tokenIdSet = new HashMap<String, ArrayList<String>>();
            while (in.ready()&&(inputLine = in.readLine()) != null) {
                    /**
                     * 1_5ecbplus	3	48	said	(132015829897728138111)
                     1_5ecbplus	3	49	Thursday	-
                     1_5ecbplus	3	50	the	-
                     1_5ecbplus	3	51	actress	-
                     1_5ecbplus	3	52	checked	(132015832182464413376
                     1_5ecbplus	3	53	into	132015832182464413376)

                     */
                String [] fields = inputLine.split("\t");
                if (fields.length==5) {
                    String fileId = fields[0];
                    String sentenceId = fields[1];
                    String tokenId = fields[2];
                    String token = fields[3];
                    String tokenKey = fileId + "\t" + sentenceId + "\t" + tokenId;
                    if (tokenIdMap.containsKey(tokenKey)) {
                        String corefId = "(" + tokenIdMap.get(tokenKey) + ")";
                        String str = tokenKey + "\t" + token + "\t" + corefId + "\n";
                        fos.write(str.getBytes());
                    } else {
                        String str = tokenKey + "\t" + token + "\t" + "-" + "\n";
                        fos.write(str.getBytes());
                    }
                }
                else {
                        fos.write((inputLine+"\n").getBytes());
                }
            }
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}