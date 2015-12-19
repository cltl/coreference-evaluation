package eu.newsreader.util;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by piek on 04/11/15.
 */
public class RestrictToSingleSpan {

    static public void main (String[] args) {
        String pathToFolder = "";
        String pathToList = "";
        pathToList = "/Users/piek/Desktop/NWR/NWR-benchmark/ecb/reference-conversion-tools/resources/ecb+.txt";
        pathToFolder = "/Users/piek/Desktop/NWR/NWR-benchmark/ecb/cross-doc/data.sw.4b";
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("--conll-key") && args.length>(i+1)) {
                pathToFolder = args[i+1];
            }
            else if (arg.equals("--exclude-list") && args.length>(i+1)) {
                pathToList = args[i+1];
            }
        }
        ArrayList<String> excluded = readExclude(new File (pathToList));
        ArrayList<File> files = Util.makeRecursiveFileList(new File(pathToFolder), ".key");
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            String reduced = reduceSpanConll(file, excluded);
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

    static public String reduceSpanConll(File file, ArrayList<String> excluded) {
        String buffer = "";
        if (file.exists() ) {
            try {
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                while (in.ready()&&(inputLine = in.readLine()) != null) {
                    if (inputLine.trim().length()>0) {
                        String[] fields = inputLine.split("\t");
                        if (fields.length==5) {
                            String token = fields[3];
                            String tag = fields[4];

                            if (tag.endsWith(")") && !tag.startsWith("(")) {
                                tag = "-";
                            }
                            else if (!tag.endsWith(")") && tag.startsWith("(")) {
                                tag += ")";
                            }
                            else if (!tag.equals("-")) {
                                if (excluded.contains(token.toLowerCase())) {
                                  //  System.out.println("token = " + token);
                                    tag = "-";
                                }
                            }
                            buffer += fields[0]+"\t"+fields[1]+"\t"+fields[2]+"\t"+fields[3]+"\t"+tag+"\n";
                        }
                        else {
                            buffer += inputLine+"\n";
                        }
                    }
                    else {
                        buffer +="\n";
                    }
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer;
    }
    static public ArrayList<String> readExclude(File file) {
        ArrayList<String> list = new ArrayList<String>();
        if (file.exists() ) {
            try {
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                while (in.ready()&&(inputLine = in.readLine()) != null) {
                    if (inputLine.trim().length()>0) {
                        list.add(inputLine.toLowerCase());
                    }
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

}
