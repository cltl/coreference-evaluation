package eu.newsreader.result;

import eu.newsreader.util.Util;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by piek on 12/16/14.
 */
public class CollectResults {


    static double recallTotalMentions = 0;
    static double precisionTotalMentions = 0;
    static double f1TotalMentions = 0;
    static double recallTotalCoreference = 0;
    static double precisionTotalCoreference = 0;
    static double f1TotalCoreference = 0;

    static int totalKeyMentions = 0;
    static int totalResponseMentions = 0;

    static int totalInventedMentions = 0;
    static int totalMissedMentions = 0;

    static int totalStrictlyCorrect = 0;
    static int totalPartiallyCorrect = 0;

    static public void main (String[] args) {
        String pathToResponseFolder = "";
        String extension = "";
        pathToResponseFolder = "/Users/piek/Desktop/NWR/NWR-benchmark/coreference/corpus_CONLL/corpus_airbus/events/response";
        extension = ".result";
        String pathToResultFile = new File(pathToResponseFolder).getParent()+"/"+"results.csv";
        ArrayList<File> resultFiles = Util.makeFlatFileList(new File(pathToResponseFolder), extension);
        for (int i = 0; i < resultFiles.size(); i++) {
            File resultFile = resultFiles.get(i);
            readResultFile(resultFile);
        }
        String str = "";
        str +="Total key mentions\t"+totalKeyMentions+"\n";
        str +="Total response mentions\t"+totalResponseMentions+"\n";
        str +="Total missed mentions\t"+totalInventedMentions+"\n";
        str +="Total invented mentions\t"+totalMissedMentions+"\n";
        str += "\n";
        double mentionRecall = recallTotalMentions/resultFiles.size();
        double mentionPrecision = precisionTotalMentions/resultFiles.size();
        double mentionF = f1TotalMentions/resultFiles.size();
        double coreferenceRecall = recallTotalCoreference/resultFiles.size();
        double coreferencePrecision = precisionTotalCoreference/resultFiles.size();
        double coreferenceF = f1TotalCoreference/resultFiles.size();
        str += "Macro average\n";
        str += "\tRecall\tPrecision\tF1\n";
        str += "Identification of Mentions\t"+mentionRecall+"\t"+mentionPrecision+"\t"+mentionF+"\n";
        str += "Coreference\t"+coreferenceRecall+"\t"+coreferencePrecision+"\t"+coreferenceF+"\n";
        str += "\n";

        mentionRecall = (totalKeyMentions-totalMissedMentions)/totalKeyMentions;
        mentionPrecision = (totalResponseMentions-totalInventedMentions)/totalResponseMentions;
        mentionF = f1TotalMentions/resultFiles.size();
        coreferenceRecall = recallTotalCoreference/resultFiles.size();
        coreferencePrecision = precisionTotalCoreference/resultFiles.size();
        coreferenceF = f1TotalCoreference/resultFiles.size();

        str += "Micro average\n";
        str += "\tRecall\tPrecision\tF1\n";
        str += "Identification of Mentions\t"+mentionRecall+"\t"+mentionPrecision+"\t"+mentionF+"\n";
        str += "Coreference\t"+coreferenceRecall+"\t"+coreferencePrecision+"\t"+coreferenceF+"\n";
        try {
            OutputStream fos = new FileOutputStream(pathToResultFile);
            fos.write(str.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    static public void readResultFile(File file) {
        if (file.exists() ) {
            try {
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                String currentSentence = "";
                while (in.ready()&&(inputLine = in.readLine()) != null) {
                    /**
                     * Total key mentions: 12
                     Total response mentions: 14
                     Strictly correct identified mentions: 9
                     Partially correct identified mentions: 0
                     No identified: 3
                     Invented: 5
                     Recall: (1 / 2) 50%	Precision: (1 / 4) 25%	F1: 33.33%
                     --------------------------------------------------------------------------

                     ====== TOTALS =======
                     Identification of Mentions: Recall: (9 / 12) 75%	Precision: (9 / 14) 64.28%	F1: 69.23%
                     --------------------------------------------------------------------------
                     Coreference: Recall: (1 / 2) 50%	Precision: (1 / 4) 25%	F1: 33.33%
                     --------------------------------------------------------------------------
                     */

                    if (inputLine.trim().length()>0) {
                        if (inputLine.startsWith("Total key mentions")) {
                            int idx = inputLine.lastIndexOf(":");
                            if (idx>-1) {
                                Integer cnt = null;
                                try {
                                    cnt = Integer.parseInt(inputLine.substring(idx+1).trim());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                totalKeyMentions += cnt;
                            }
                        }
                        else if (inputLine.startsWith("Total response mentions")) {
                            int idx = inputLine.lastIndexOf(":");
                            if (idx>-1) {
                                Integer cnt = null;
                                try {
                                    cnt = Integer.parseInt(inputLine.substring(idx+1).trim());
                                } catch (NumberFormatException e) {
                                    System.out.println("inputLine = " + inputLine);
                                    e.printStackTrace();
                                }
                                totalResponseMentions += cnt;
                            }
                        }
                        else if (inputLine.startsWith("No identified")) {
                            int idx = inputLine.lastIndexOf(":");
                            if (idx>-1) {
                                Integer cnt = null;
                                try {
                                    cnt = Integer.parseInt(inputLine.substring(idx+1).trim());
                                } catch (NumberFormatException e) {
                                    System.out.println("inputLine = " + inputLine);
                                    e.printStackTrace();
                                }
                                totalMissedMentions += cnt;
                            }
                        }
                        else if (inputLine.startsWith("Invented")) {
                            int idx = inputLine.lastIndexOf(":");
                            if (idx>-1) {
                                Integer cnt = null;
                                try {
                                    cnt = Integer.parseInt(inputLine.substring(idx+1).trim());
                                } catch (NumberFormatException e) {
                                    System.out.println("inputLine = " + inputLine);
                                    e.printStackTrace();
                                }
                                totalInventedMentions += cnt;
                            }
                        }
                        else if (inputLine.startsWith("Identification of Mentions:")) {
                            // Identification of Mentions: Recall: (9 / 12) 75%	Precision: (9 / 14) 64.28%	F1: 69.23%
                            String [] fields = inputLine.split("%");
                            if (fields.length>=3) {
                                int idx = fields[0].indexOf(")");
                                if (idx>-1) {
                                    Double cnt = null;
                                    try {
                                        cnt = Double.parseDouble(fields[0].substring(idx + 1).trim());
                                    } catch (NumberFormatException e) {
                                        System.out.println("inputLine = " + inputLine);
                                        e.printStackTrace();
                                    }
                                    recallTotalMentions += cnt;
                                }
                                idx = fields[1].indexOf(")");
                                if (idx>-1) {
                                    Double cnt = null;
                                    try {
                                        cnt = Double.parseDouble(fields[1].substring(idx + 1).trim());
                                    } catch (NumberFormatException e) {
                                        System.out.println("inputLine = " + inputLine);
                                        e.printStackTrace();
                                    }
                                    precisionTotalMentions += cnt;
                                }
                                idx = fields[2].lastIndexOf(":");
                                if (idx>-1) {
                                    Double cnt = null;
                                    try {
                                        cnt = Double.parseDouble(fields[2].substring(idx + 1).trim());
                                    } catch (NumberFormatException e) {
                                        System.out.println("inputLine = " + inputLine);
                                        e.printStackTrace();
                                    }
                                    f1TotalMentions += cnt;
                                }
                            }
                        }
                        else if (inputLine.startsWith("Coreference:")) {
                            //Coreference: Recall: (1 / 2) 50%	Precision: (1 / 4) 25%	F1: 33.33%
                            String [] fields = inputLine.split("%");
                            if (fields.length>=3) {
                                int idx = fields[0].indexOf(")");
                                if (idx>-1) {
                                    Double cnt = null;
                                    try {
                                        cnt = Double.parseDouble(fields[0].substring(idx+1).trim());
                                    } catch (NumberFormatException e) {
                                        System.out.println("inputLine = " + inputLine);
                                        e.printStackTrace();
                                    }
                                    recallTotalCoreference += cnt;
                                }
                                idx = fields[1].indexOf(")");
                                if (idx>-1) {
                                    Double cnt = null;
                                    try {
                                        cnt = Double.parseDouble(fields[1].substring(idx+1).trim());
                                    } catch (NumberFormatException e) {
                                        System.out.println("inputLine = " + inputLine);
                                        e.printStackTrace();
                                    }
                                    precisionTotalCoreference += cnt;
                                }
                                idx = fields[2].lastIndexOf(":");
                                if (idx>-1) {
                                    Double cnt = null;
                                    try {
                                        cnt = Double.parseDouble(fields[2].substring(idx + 1).trim());
                                    } catch (NumberFormatException e) {
                                        System.out.println("inputLine = " + inputLine);
                                        e.printStackTrace();
                                    }
                                    f1TotalCoreference += cnt;
                                }
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

    /**
     * version: 8.01 /Users/piek/Desktop/NWR/NWR-benchmark/coreference/reference-coreference-scorers/v8.01/lib/CorScorer.pm
     ====> (8951_World_largest_passenger_airliner_makes_first_flight);:
     File (8951_World_largest_passenger_airliner_makes_first_flight);:
     Entity 0: (5,5)
     Entity 1: (7,7) (30,30)
     Entity 2: (11,11)
     Entity 3: (27,27)
     Entity 4: (32,33) (40,40)
     Entity 5: (46,46)
     Entity 6: (50,51)
     Entity 7: (58,58)
     Entity 8: (71,71)
     Entity 9: (76,76)
     ====> (8951_World_largest_passenger_airliner_makes_first_flight);:
     File (8951_World_largest_passenger_airliner_makes_first_flight);:
     Entity 0: (4,4)
     Entity 1: (5,5) (27,27) (50,51)
     Entity 2: (7,7) (30,30) (58,58)
     Entity 3: (11,11)
     Entity 4: (32,32)
     Entity 5: (66,66)
     Entity 6: (71,71)
     Entity 7: (76,76)
     Entity 8: (80,80)
     Entity 9: (83,83)
     (8951_World_largest_passenger_airliner_makes_first_flight);:
     Total key mentions: 12
     Total response mentions: 14
     Strictly correct identified mentions: 9
     Partially correct identified mentions: 0
     No identified: 3
     Invented: 5
     Recall: (1 / 2) 50%	Precision: (1 / 4) 25%	F1: 33.33%
     --------------------------------------------------------------------------

     ====== TOTALS =======
     Identification of Mentions: Recall: (9 / 12) 75%	Precision: (9 / 14) 64.28%	F1: 69.23%
     --------------------------------------------------------------------------
     Coreference: Recall: (1 / 2) 50%	Precision: (1 / 4) 25%	F1: 33.33%
     --------------------------------------------------------------------------

     */
}
