package eu.newsreader.script;

import eu.newsreader.util.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by piek on 12/16/14.
 */
public class MakeScoreScript {

    static public void main (String[] args) {
        try {
            String pathToKeyFolder = "";
            String pathToResponseFolder = "";
            String corpus = "";
            String method = "";
           //   pathToKeyFolder = "/Users/piek/Desktop/NWR/NWR-benchmark/coreference/corpus_CONLL/corpus_apple/entities/key";
           //   pathToResponseFolder = "/Users/piek/Desktop/NWR/NWR-benchmark/coreference/corpus_CONLL/corpus_apple/entities/response";
           //   corpus = "corpus_apple_entities";
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg.equalsIgnoreCase("--key") && args.length>(i+1)) {
                    pathToKeyFolder = args[i+1];
                }
                else if (arg.equalsIgnoreCase("--response") && args.length>(i+1)) {
                    pathToResponseFolder = args[i+1];
                }
                else if (arg.equalsIgnoreCase("--corpus") && args.length>(i+1)) {
                    corpus = args[i+1];
                }
                else if (arg.equalsIgnoreCase("--method") && args.length>(i+1)) {
                    method = args[i+1];
                }
            }

            String parentFolder = new File(pathToKeyFolder).getParent();
            String scriptFile = parentFolder+"/"+corpus+".score.sh";
            OutputStream scriptFos = new FileOutputStream(scriptFile);
            ArrayList<File> keyFiles = Util.makeFlatFileList(new File(pathToKeyFolder));
            ArrayList<File> responseFiles = Util.makeFlatFileList(new File(pathToResponseFolder));
            System.out.println("keyFiles = " + keyFiles.size());
            System.out.println("responseFiles = " + responseFiles.size());
            for (int i = 0; i < keyFiles.size(); i++) {
                File keyFile = keyFiles.get(i);
                String keyS1 = Util.readFirstSentence(keyFile);
                for (int j = 0; j < responseFiles.size(); j++) {
                    File responseFile = responseFiles.get(j);
                    String responseS1 = Util.readFirstSentence(responseFile);
                    if (keyS1.equals(responseS1)) {
                      //  System.out.println("keyFile = " + keyFile.getCanonicalPath());
                      //  System.out.println("keyFile = " + keyFile.getAbsolutePath());
                        String str = "perl ./v8.01/scorer.pl "+method+" "+keyFile.getAbsolutePath()+" "+responseFile.getAbsolutePath()+" > "+responseFile.getAbsolutePath()+"."+method+".result \n";
                        scriptFos.write(str.getBytes());
                    }
                }
                //break;
            }
            scriptFos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
