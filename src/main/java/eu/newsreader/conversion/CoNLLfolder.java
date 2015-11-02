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
public class CoNLLfolder {

    static public void main (String[] args) {
        String pathToKeyFolder = "";
        String pathToResponseFolder = "";
        pathToKeyFolder = "/Users/piek/Desktop/NWR/NWR-benchmark/coreference/corpus_CONLL-wn-sim-2.5-wsd8-ims-ukb-top/corpus_stock_market/events/key/";
        pathToResponseFolder = "/Users/piek/Desktop/NWR/NWR-benchmark/coreference/corpus_CONLL-wn-sim-2.5-wsd8-ims-ukb-top/corpus_stock_market/events/response/";
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("--key-folder") && args.length>(i+1)) {
                pathToKeyFolder = args[i+1];
            }
            else if (arg.equals("--response-folder") && args.length>(i+1)) {
                pathToResponseFolder = args[i+1];
            }
        }

        File inputKeyFolder = new File(pathToKeyFolder);
        File inputResponseFolder = new File(pathToResponseFolder);
        CoNLLfile.CorefStatistics corefKeyStatistics = new CoNLLfile.CorefStatistics();
        CoNLLfile.CorefStatistics corefResponseStatistics = new CoNLLfile.CorefStatistics();
        try {
            OutputStream fos = new FileOutputStream(inputKeyFolder.getParentFile()+"/"+"corefOverview.csv");
            ArrayList<File> keyFiles = Util.makeRecursiveFileList(inputKeyFolder);
            for (int i = 0; i < keyFiles.size(); i++) {
                File file = keyFiles.get(i);
                CoNLLfile.readCorefSetFromCoNLL(file, fos, corefKeyStatistics, "key");
            }
            ArrayList<File> responseFiles = Util.makeRecursiveFileList(inputResponseFolder);
            for (int i = 0; i < responseFiles.size(); i++) {
                File file = responseFiles.get(i);
                CoNLLfile.readCorefSetFromCoNLL(file, fos, corefResponseStatistics, "response");
            }
            String str = "KEY:\n"+corefKeyStatistics.toString("KEY");
            fos.write(str.getBytes());
            str = "RESPONSE:\n"+corefResponseStatistics.toString("RESPONSE");
            fos.write(str.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
