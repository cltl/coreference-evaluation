package eu.newsreader.result;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by piek on 05/01/16.
 */
public class OverviewMetrics {
    static double conllRecall;
    static double conllPrecision;
    static double conllF;
    static String columns;
    static String rows;

    static public void main (String[] args) {
        String name = args[0];
        conllRecall = 0;
        conllPrecision = 0;
        conllF = 0;
        columns = "";
        rows = name;
        ArrayList<File> files = new ArrayList<File>();
        for (int i = 1; i < args.length; i++) {
            String arg = args[i];
            File file = new File (arg);
            files.add(file);
        }
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            readResultFile(file);
        }
        columns += ",CONLLf";
        rows += ","+conllF/3;
        System.out.println(columns);
        System.out.println(rows);
    }

    static void readResultFile(File file) {
        String metric ="";
        String recall="";
        String precision="";
        String f1="";
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                while (in.ready() && (inputLine = in.readLine()) != null) {
                        /*
    Macro average bcub
recall	32.02604651162792%
precision	62.88790697674417%
f1	41.789302325581396%
     */             if (inputLine.startsWith("Macro average ")) {
                        metric = inputLine.substring(inputLine.lastIndexOf(" ")).trim();
                    }
                    else if (inputLine.startsWith("recall\t")) {
                        recall = inputLine.substring(inputLine.lastIndexOf("\t"), inputLine.length()-1).trim();
                    }
                    else if (inputLine.startsWith("precision\t")) {
                        precision = inputLine.substring(inputLine.lastIndexOf("\t"),inputLine.length()-1).trim();
                    }
                    else if (inputLine.startsWith("f1\t")) {
                        f1 = inputLine.substring(inputLine.lastIndexOf("\t"),inputLine.length()-1).trim();
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            columns += ","+metric+"-r,"+metric+"-p,"+metric+"-f";
            rows += ","+recall+","+precision+","+f1;

            if (!metric.equalsIgnoreCase("blanc")) {
                conllRecall += Double.parseDouble(recall);
                conllPrecision += Double.parseDouble(precision);
                conllF += Double.parseDouble(f1);
            }
        }
    }
}
