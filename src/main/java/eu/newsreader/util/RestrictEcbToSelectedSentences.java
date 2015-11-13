package eu.newsreader.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by piek on 04/11/15.
 */
public class RestrictEcbToSelectedSentences {

    static public void main (String[] args) {
        String pathToFolder = "";
        String pathToCsvFile = "";
        pathToFolder = "/Users/piek/Desktop/NWR/NWR-benchmark/ecb/data/ECB+CoNLL";
        pathToCsvFile = "/Users/piek/Desktop/NWR/NWR-benchmark/ecb/ECBplus_coreference_sentences.csv";
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("--conll-key") && args.length>(i+1)) {
                pathToFolder = args[i+1];
            }
            else if (arg.equals("--csv") && args.length>(i+1)) {
                pathToCsvFile = args[i+1];
            }
        }
        HashMap<String, ArrayList<String>> idMap = Util.readTopicSentenceIds(pathToCsvFile);
        ArrayList<File> files = Util.makeRecursiveFileList(new File(pathToFolder), ".key");
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            String reduced = Util.reduceConllFileForSentenceIds(file, idMap);
            try {
                OutputStream keyFos = new FileOutputStream(file);
                keyFos.write(reduced.getBytes());
                keyFos.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
