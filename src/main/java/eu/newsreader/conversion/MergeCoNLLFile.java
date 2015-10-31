package eu.newsreader.conversion;

import eu.newsreader.util.Util;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by piek on 16/10/15.
 */
public class MergeCoNLLFile {


    static public void main (String[] args) {
        String pathToInPutFolder = "";
        String pathToOutputFile = "";
        String extension = "";
        String conllLabel ="";

        pathToInPutFolder = "/Users/piek/Desktop/NWR/NWR-benchmark/ecb/data/ecb_conll/4/response/";
        pathToOutputFile = "/Users/piek/Desktop/NWR/NWR-benchmark/ecb/cross-document/topic_4.key.response";
        extension = ".response";
        conllLabel = "4";


        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equalsIgnoreCase("--input-folder") && args.length>(i+1)) {
                pathToInPutFolder = args[i+1];
            }
            else if (arg.equalsIgnoreCase("--output-folder") && args.length>(i+1)) {
                pathToOutputFile = args[i+1];
            }
            else if (arg.equalsIgnoreCase("--extension") && args.length>(i+1)) {
                extension = args[i+1];
            }
            else if (arg.equalsIgnoreCase("--id") && args.length>(i+1)) {
                conllLabel = args[i+1];
            }
        }


        File inputFolder = new File (pathToInPutFolder);
        String content = "#begin document ("+conllLabel+");\n";
        ArrayList<File> files = Util.makeFlatFileList(inputFolder, extension);
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            content += readConllFile(file);
        }
        content += "#end document\n";
        try {
            OutputStream fos = new FileOutputStream(pathToOutputFile);
            fos.write(content.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String readConllFile (File file) {
        String content = "";
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader in = new BufferedReader(isr);
            String inputLine = "";
            if (in.ready()&&(inputLine = in.readLine()) != null) {
                ///skip first line
            }
            while (in.ready()&&(inputLine = in.readLine()) != null) {
                if (inputLine.indexOf("#end document")==-1) {
                    content += inputLine + "\n";
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}
