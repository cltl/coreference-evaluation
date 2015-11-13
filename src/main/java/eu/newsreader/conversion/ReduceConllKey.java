package eu.newsreader.conversion;

import eu.newsreader.util.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by piek on 12/15/14.
 */
public class ReduceConllKey {

    static public void main (String[] args) {
        try {
            String pathToKeyFolder = "";
            pathToKeyFolder = "/Users/piek/Desktop/NWR/NWR-benchmark/ecb/data/ECB+CoNLL";
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg.equalsIgnoreCase("--key") && args.length>(i+1)) {
                    pathToKeyFolder = args[i+1];
                }
            }
            ArrayList<File> keyFiles = Util.makeRecursiveFileList(new File(pathToKeyFolder), ".key");
            for (int i = 0; i < keyFiles.size(); i++) {
                File keyFile = keyFiles.get(i);
               // System.out.println("keyFile = " + keyFile);
                String reduced = Util.reduceFileToCoreferenceAnnotatedSentences(keyFile);
                OutputStream keyFos = new FileOutputStream(keyFile);
                keyFos.write(reduced.getBytes());
                keyFos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
