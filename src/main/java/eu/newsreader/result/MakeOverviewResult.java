package eu.newsreader.result;

import eu.newsreader.util.Util;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by piek on 12/07/15.
 */
public class MakeOverviewResult {
    static String recallMacroString = "";
    static String precisionMacroString = "";
    static String fmeasureMacroString = "";
    static String recallMicroString = "";
    static String precisionMicroString = "";
    static String fmeasureMicroString = "";
    static double averageMacroRecall = 0.0;
    static double averageMacroPrecision = 0.0;
    static double averageMacroF = 0.0;
    static double averageMicroRecall = 0.0;
    static double averageMicroPrecision = 0.0;
    static double averageMicroF = 0.0;

    static double averageMentionsMacroRecall = 0.0;
    static double averageMentionsMacroPrecision = 0.0;
    static double averageMentionsMacroF = 0.0;
    static double averageMentionsMicroRecall = 0.0;
    static double averageMentionsMicroPrecision = 0.0;
    static double averageMentionsMicroF = 0.0;

/*
    # coref sets	215
            # coref sets	361
            # multitons	56
            # multitons	45
            # singletons	159
            # singletons	316*/
     static double averageNrKeyCorefSets = 0.0;
     static double averageNrResponseCorefSets = 0.0;
     static double averageKeyMultisets = 0.0;
     static double averageKeySingletons = 0.0;
     static double averageResponseMultisets = 0.0;
     static double averageResponseSingletons = 0.0;


    //results.csv
    static public void main (String[] args) {
        try {
            String pathToFolder = "";
            String method = "";
            String threshold = "";
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg.equals("--folder") && args.length>(i+1)) {
                    pathToFolder = args[i+1];
                }
                else if (arg.equals("--method") && args.length>(i+1)) {
                    method = args[i+1];
                }
                else if (arg.equals("--threshold") && args.length>(i+1)) {
                    threshold = args[i+1];
                }
            }
            recallMacroString = "Macro average recall "+threshold+"-"+method+"\tMentions\tCoreference"+"\n";
            precisionMacroString = "Macro average precision "+threshold+"-"+method+"\tMentions\tCoreference"+"\n";
            fmeasureMacroString = "Macro average f "+threshold+"-"+method+"\tMentions\tCoreference"+"\n";
            recallMicroString = "Micro average recall "+threshold+"-"+method+"\tMentions\tCoreference"+"\n";
            precisionMicroString = "Micro average precision "+threshold+"-"+method+"\tMentions\tCoreference"+"\n";
            fmeasureMicroString = "Micro average f "+threshold+"-"+method+"\tMentions\tCoreference"+"\n";

            File resultFolder = new File(pathToFolder);
            OutputStream fos = new FileOutputStream(resultFolder.getAbsolutePath()+"/"+"overall-evaluation.csv");
            ArrayList<File> corpora = Util.makeFolderList(resultFolder);
            for (int i = 0; i < corpora.size(); i++) {
                File corpus = corpora.get(i);
                File resultFile = new File (corpus.getAbsolutePath()+"/"+"results.csv");
                readResultsCsv(resultFile, method);
                File overviewFile = new File (corpus.getAbsolutePath()+"/"+"corefOverview.csv");
                readOverviewCsv(overviewFile);
            }
            recallMacroString += "Average\t"+method+"\t"+averageMacroRecall/corpora.size()+"\t"+averageMentionsMacroRecall/corpora.size()+"\n\n";
            precisionMacroString += "Average\t"+method+"\t"+averageMacroPrecision/corpora.size()+"\t"+averageMentionsMacroPrecision/corpora.size()+"\n\n";
            fmeasureMacroString += "Average\t"+method+"\t"+averageMacroF/corpora.size()+"\t"+averageMentionsMacroF/corpora.size()+"\n\n";

/*
            recallMacroString += "Average_mentions\t"+method+"\t"+averageMentionsMacroRecall/corpora.size()+"\n\n";
            precisionMacroString += "Average_mentions\t"+method+"\t"+averageMentionsMacroPrecision/corpora.size()+"\n\n";
            fmeasureMacroString += "Average_mentions\t"+method+"\t"+averageMentionsMacroF/corpora.size()+"\n\n";
*/

            recallMicroString += "Average\t"+method+"\t"+averageMicroRecall/corpora.size()+"\t"+averageMentionsMicroRecall/corpora.size()+"\n\n";
            precisionMicroString += "Average\t"+method+"\t"+averageMicroPrecision/corpora.size()+"\t"+averageMentionsMicroPrecision/corpora.size()+"\n\n";
            fmeasureMicroString += "Average\t"+method+"\t"+averageMicroF/corpora.size()+"\t"+averageMentionsMicroF/corpora.size()+"\n\n";

/*
            recallMicroString += "Average_mentions\t"+method+"\t"+averageMentionsMicroRecall/corpora.size()+"\n\n";
            precisionMicroString += "Average_mentions\t"+method+"\t"+averageMentionsMicroPrecision/corpora.size()+"\n\n";
            fmeasureMicroString += "Average_mentions\t"+method+"\t"+averageMentionsMicroF/corpora.size()+"\n\n";
*/

            fos.write(recallMacroString.getBytes());
            fos.write(precisionMacroString.getBytes());
            fos.write(fmeasureMacroString.getBytes());
            fos.write(recallMicroString.getBytes());
            fos.write(precisionMicroString.getBytes());
            fos.write(fmeasureMicroString.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void readResultsCsv (File file, String method) {
        try {
            boolean MACRO = false;
            boolean MICRO = false;
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader in = new BufferedReader(isr);
            String inputLine = "";
            String corpusName = file.getParentFile().getName();
            while (in.ready()&&(inputLine = in.readLine()) != null) {
                if (inputLine.trim().startsWith("Macro")) {
                    MACRO = true;
                }
                else if (inputLine.trim().startsWith("Micro")) {
                    MACRO = false;
                    MICRO = true;
                }
                else if (inputLine.trim().startsWith("Coreference")) {
                    String [] fields = inputLine.split("\t");
                    if (fields.length==4) {
                        String recall = fields[1];
                        String precision = fields[2];
                        String fmeasure = fields[3];
                        if (MACRO) {
                            recallMacroString +=  "\t" + recall +"\n";
                            precisionMacroString +=  "\t" + precision + "\n";
                            fmeasureMacroString +=  "\t" + fmeasure + "\n";
                            averageMacroRecall += Double.parseDouble(recall);
                            averageMacroPrecision += Double.parseDouble(precision);
                            averageMacroF += Double.parseDouble(fmeasure);

                        }
                        if (MICRO) {
                            recallMicroString +=  "\t" + recall + "\n";
                            precisionMicroString +=  "\t" + precision + "\n";
                            fmeasureMicroString +=  "\t" + fmeasure + "\n";
                            averageMicroRecall += Double.parseDouble(recall);
                            averageMicroPrecision += Double.parseDouble(precision);
                            averageMicroF += Double.parseDouble(fmeasure);
                        }
                    }
                }
                else if (inputLine.trim().startsWith("Identification of Mentions")) {
                    String [] fields = inputLine.split("\t");
                    if (fields.length==4) {
                        String recall = fields[1];
                        String precision = fields[2];
                        String fmeasure = fields[3];
                        if (MACRO) {
                            recallMacroString += corpusName + "_mentions\t" + method + "\t" + recall ;
                            precisionMacroString += corpusName + "_mentions\t" + method + "\t" + precision ;
                            fmeasureMacroString += corpusName + "_mentions\t" + method + "\t" + fmeasure ;
                            averageMentionsMacroRecall += Double.parseDouble(recall);
                            averageMentionsMacroPrecision += Double.parseDouble(precision);
                            averageMentionsMacroF += Double.parseDouble(fmeasure);

                        }
                        if (MICRO) {
                            recallMicroString += corpusName + "_mentions\t" + method + "\t" + recall ;
                            precisionMicroString += corpusName + "_mentions\t" + method + "\t" + precision ;
                            fmeasureMicroString += corpusName + "_mentions\t" + method + "\t" + fmeasure ;
                            averageMentionsMicroRecall += Double.parseDouble(recall);
                            averageMentionsMicroPrecision += Double.parseDouble(precision);
                            averageMentionsMicroF += Double.parseDouble(fmeasure);
                        }
                    }
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static void readOverviewCsv (File file) {
        try {
            boolean KEY = false;
            boolean RESPONSE = false;
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader in = new BufferedReader(isr);
            String inputLine = "";
            String corpusName = file.getParentFile().getName();
            while (in.ready()&&(inputLine = in.readLine()) != null) {
                if (inputLine.trim().startsWith("KEY")) {
                    KEY = true;
                    RESPONSE = false;
                }
                else if (inputLine.trim().startsWith("RESPONSE")) {
                    KEY = false;
                    RESPONSE = true;
                }
                else if (inputLine.trim().endsWith("coref sets")) {
                    String [] fields = inputLine.split("\t");
                    if (fields.length==2) {
                        String field = fields[1];
                        if (KEY) {
                            averageNrKeyCorefSets+= Double.parseDouble(field);
                        }
                        if (RESPONSE) {
                            averageNrResponseCorefSets+= Double.parseDouble(field);
                        }
                    }
                }
                else if (inputLine.trim().endsWith("multitons")) {
                    String [] fields = inputLine.split("\t");
                    if (fields.length==2) {
                        String field = fields[1];
                        if (KEY) {
                            averageKeyMultisets+= Double.parseDouble(field);
                        }
                        if (RESPONSE) {
                            averageResponseMultisets+= Double.parseDouble(field);
                        }
                    }
                }
                else if (inputLine.trim().endsWith("singletons")) {
                    String [] fields = inputLine.split("\t");
                    if (fields.length==2) {
                        String field = fields[1];
                        if (KEY) {
                            averageKeySingletons+= Double.parseDouble(field);
                        }
                        if (RESPONSE) {
                            averageResponseSingletons+= Double.parseDouble(field);
                        }
                    }
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
