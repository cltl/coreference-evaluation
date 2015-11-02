package eu.newsreader.result;

import eu.newsreader.util.Util;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

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

    static double recallTotalNoCoreference = 0;
    static double precisionTotalNoCoreference = 0;
    static double f1TotalNoCoreference = 0;

    static double recallTotalBlanc = 0;
    static double precisionTotalBlanc = 0;
    static double f1TotalBlanc = 0;

    static int totalKeyMentions = 0;
    static int totalResponseMentions = 0;

    static int totalInventedMentions = 0;
    static int totalMissedMentions = 0;

    static int totalStrictlyCorrect = 0;
    static int totalPartiallyCorrect = 0;

    static double totalCorrectCoreferences = 0;
    static double totalKeyCoreferenceSets = 0;
    static double totalResponseCoreferenceSets = 0;

    static double totalCorrectCoreferenceLinks = 0;
    static double totalKeyCoreferenceLinks = 0;
    static double totalResponseCoreferenceLinks = 0;

    static double totalCorrectNonCoreferenceLinks = 0;
    static double totalKeyNonCoreferenceLinks = 0;
    static double totalResponseNonCoreferenceLinks = 0;

    static public void main (String[] args) {
        String pathToResponseFolder = "";
        String extension = "";
        String label = "";
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("--result-folder") && (args.length>(i+1))) {
                pathToResponseFolder = args[i+1];
            }
            else if (arg.equals("--extension") && (args.length>(i+1))) {
                extension = args[i+1];
            }
            else if (arg.equals("--label") && (args.length>(i+1))) {
                label = args[i+1];
            }
        }
        String pathToResultFile = new File(pathToResponseFolder).getParent()+"/"+label+".results.csv";
        ArrayList<File> resultFiles = Util.makeRecursiveFileList(new File(pathToResponseFolder), extension);
        for (int i = 0; i < resultFiles.size(); i++) {
            File resultFile = resultFiles.get(i);
            readResultFile(resultFile);
        }
        Date date = new Date();
        String str = "";
        str = new File(pathToResponseFolder).getParent()+"\t"+label+"\n";
        str += "Date\t"+date.toString()+"\n\n";
        str += "Nr. of result files\t"+resultFiles.size()+"\n";
        str +="MENTIONS (key mentions="+totalKeyMentions+")\n";
        str +="# response mentions\t"+totalResponseMentions+"\n";
        str +="# missed mentions\t"+totalMissedMentions+"\n";
        str +="# invented mentions\t"+totalInventedMentions+"\n";
        str +="# strictly correct identified mentions\t"+totalStrictlyCorrect+"\n";
        str +="# partially correct identified mentions\t"+totalPartiallyCorrect+"\n";
        double recall = (double) 100*totalStrictlyCorrect/totalKeyMentions;
        double precision = (double) 100*totalStrictlyCorrect/totalResponseMentions;
        double f1 = (double) 2*(recall*precision)/(recall+precision);
        str +=" recall\t"+recall +"%\n";
        str +=" precision\t"+precision+"%\n";
        str +=" f1\t"+f1+"%\n\n";

        str += "REFERENCE (BLANC)\n";
        str +="# reference links\t"+totalKeyCoreferenceSets+"\n";
        str +="# response reference links\t"+totalResponseCoreferenceSets+"\n";
        str +="# correct reference links\t"+ totalCorrectCoreferences+"\n";
        recall = (double) 100*totalCorrectCoreferences/(double)totalKeyCoreferenceSets;
        precision = (double) 100*totalCorrectCoreferences/(double)totalResponseCoreferenceSets;
        f1 = (double) 2*(recall*precision)/(recall+precision);
        str +=" recall\t"+recall +"%\n";
        str +=" precision\t"+precision+"%\n";
        str +=" f1\t"+f1+"%\n\n";

        str += "COREFERENCE\n";
        str +="# key coreference links\t"+totalKeyCoreferenceLinks+"\n";
        str +="# response coreference links\t"+totalResponseCoreferenceLinks+"\n";
        str +="# correct coreference links\t"+ totalCorrectCoreferenceLinks+"\n";
        recall = (double) 100*totalCorrectCoreferenceLinks/(double)totalKeyCoreferenceLinks;
        precision = (double) 100*totalCorrectCoreferenceLinks/(double)totalResponseCoreferenceLinks;
        f1 = (double) 2*(recall*precision)/(recall+precision);
        str +=" recall\t"+recall +"%\n";
        str +=" precision\t"+precision+"%\n";
        str +=" f1\t"+f1+"%\n\n";

        str += "NOREFERENCE\n";
        str +="# key non-coreference links\t"+totalKeyNonCoreferenceLinks+"\n";
        str +="# response non-coreference links\t"+totalResponseNonCoreferenceLinks+"\n";
        str +="# correct non-coreference links\t"+ totalCorrectNonCoreferenceLinks+"\n";
        recall = (double) 100*totalCorrectNonCoreferenceLinks/(double)totalKeyNonCoreferenceLinks;
        precision =  (double)100*totalCorrectNonCoreferenceLinks/(double)totalResponseNonCoreferenceLinks;
        f1 = (double) 2*(recall*precision)/(recall+precision);
        str +=" recall\t"+recall +"%\n";
        str +=" precision\t"+precision+"%\n";
        str +=" f1\t"+f1+"%\n\n";

        str += "\n";
        double mentionRecall = recallTotalMentions/resultFiles.size();
        double mentionPrecision = precisionTotalMentions/resultFiles.size();
        double mentionF = f1TotalMentions/resultFiles.size();

        double coreferenceRecall = recallTotalCoreference/resultFiles.size();
        double coreferencePrecision = precisionTotalCoreference/resultFiles.size();
        double coreferenceF = f1TotalCoreference/resultFiles.size();
        double nocoreferenceRecall = recallTotalNoCoreference/resultFiles.size();
        double nocoreferencePrecision = precisionTotalNoCoreference/resultFiles.size();
        double nocoreferenceF = f1TotalNoCoreference/resultFiles.size();

        double blancRecall = recallTotalBlanc/resultFiles.size();
        double blancPrecision = precisionTotalBlanc/resultFiles.size();
        double blancF = f1TotalBlanc/resultFiles.size();


        str += "Macro average over all result files reported by the coref scorer:\n";
        str += "Macro average identification of Mentions\n";
        str += "recall\t"+mentionRecall+"%\n";
        str += "precision\t"+mentionPrecision+"%\n";
        str += "f1\t"+mentionF+"%\n\n";
        str += "Macro average blanc\n";
        str += "recall\t"+blancRecall+"%\n";
        str += "precision\t"+blancPrecision+"%\n";
        str += "f1\t"+blancF+"%\n";
        str += "\n";

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
                        else if (inputLine.startsWith("Strictly correct identified mentions:")) {
                            int idx = inputLine.lastIndexOf(":");
                            if (idx>-1) {
                                Integer cnt = null;
                                try {
                                    cnt = Integer.parseInt(inputLine.substring(idx+1).trim());
                                } catch (NumberFormatException e) {
                                    System.out.println("inputLine = " + inputLine);
                                    e.printStackTrace();
                                }
                                totalStrictlyCorrect += cnt;
                            }
                        }
                        else if (inputLine.startsWith("Partially correct identified mentions:")) {
                            int idx = inputLine.lastIndexOf(":");
                            if (idx>-1) {
                                Integer cnt = null;
                                try {
                                    cnt = Integer.parseInt(inputLine.substring(idx+1).trim());
                                } catch (NumberFormatException e) {
                                    System.out.println("inputLine = " + inputLine);
                                    e.printStackTrace();
                                }
                                totalPartiallyCorrect += cnt;
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
                        else if (inputLine.startsWith("Coreference:")
                                ) {
                            /// This format is used by other scores than BLANC
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
                                int idx_s = fields[0].indexOf("(");
                                int idx_div = fields[0].indexOf("/");
                                if (idx>idx_div & idx_div>idx_s & idx_s>-1) {
                                    String correct = fields[0].substring(idx_s+1, idx_div).trim();
                                    String recall = fields[0].substring(idx_div+1, idx).trim();
                                    //System.out.println("correct = " + correct);
                                    //System.out.println("recall = " + recall);
                                    try {
                                        totalCorrectCoreferences += Integer.parseInt(correct);
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        totalKeyCoreferenceSets += Integer.parseInt(recall);
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }

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
                                idx_s = fields[1].indexOf("(");
                                idx_div = fields[1].indexOf("/");
                                if (idx>idx_div & idx_div>idx_s & idx_s>-1) {
                                    String precision = fields[1].substring(idx_div + 1, idx).trim();
                                    //System.out.println("precision = " + precision);
                                    try {
                                        totalResponseCoreferenceSets += Integer.parseInt(precision);
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }

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
                        else if (
                                inputLine.startsWith("BLANC:")
                                ) {
                            //BLANC: Recall: (0.851851851851852 / 1) 85.18%	Precision: (0.183632543926662 / 1) 18.36%	F1: 27%

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
                                    recallTotalBlanc += cnt;
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
                                    precisionTotalBlanc += cnt;
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
                                    f1TotalBlanc += cnt;
                                }
                            }
                        }
                        else if (inputLine.startsWith("Coreference links:") //// IN CASE OF BLANC WE GET DIFFERENT OUTPUT
                                ||
                                inputLine.startsWith("Non-coreference links:")
                                ) {
                            //Coreference links: Recall: (1 / 1) 100%	Precision: (1 / 17) 5.88%	F1: 11.11%
                            //Non-coreference links: Recall: (95 / 135) 70.37%	Precision: (95 / 308) 30.84%	F1: 42.88%

                            String [] fields = inputLine.split("%");
                            if (fields.length>=3) {
                                int idx = fields[0].indexOf(")");
                                int idx_s = fields[0].indexOf("(");
                                int idx_div = fields[0].indexOf("/");
                                if (idx>idx_div & idx_div>idx_s & idx_s>-1) {
                                    String correct = fields[0].substring(idx_s+1, idx_div).trim();
                                    String recall = fields[0].substring(idx_div+1, idx).trim();
                                    //System.out.println("correct = " + correct);
                                    //System.out.println("recall = " + recall);
                                    try {
                                        totalCorrectCoreferences += Double.parseDouble(correct);
                                        if (inputLine.startsWith("Coreference links:")) {
                                           totalCorrectCoreferenceLinks +=  Double.parseDouble(correct);
                                        }
                                        else if (inputLine.startsWith("Non-coreference links:")) {
                                            totalCorrectNonCoreferenceLinks +=  Double.parseDouble(correct);
                                        }
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        totalKeyCoreferenceSets += Double.parseDouble(recall);
                                        if (inputLine.startsWith("Coreference links:")) {
                                            totalKeyCoreferenceLinks +=  Double.parseDouble(recall);
                                        }
                                        else if (inputLine.startsWith("Non-coreference links:")) {
                                            totalKeyNonCoreferenceLinks +=  Double.parseDouble(recall);
                                        }
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }

                                }
                                idx = fields[1].indexOf(")");
                                idx_s = fields[1].indexOf("(");
                                idx_div = fields[1].indexOf("/");
                                if (idx>idx_div & idx_div>idx_s & idx_s>-1) {
                                    String precision = fields[1].substring(idx_div + 1, idx).trim();
                                    //System.out.println("precision = " + precision);
                                    try {
                                        totalResponseCoreferenceSets += Double.parseDouble(precision);
                                        if (inputLine.startsWith("Coreference links:")) {
                                            totalResponseCoreferenceLinks +=  Double.parseDouble(precision);
                                        }
                                        else if (inputLine.startsWith("Non-coreference links:")) {
                                            totalResponseNonCoreferenceLinks +=  Double.parseDouble(precision);
                                        }
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }

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


     ====== TOTALS =======
     Identification of Mentions: Recall: (15 / 17) 88.23%	Precision: (15 / 26) 57.69%	F1: 69.76%
     --------------------------------------------------------------------------

     Coreference:
     Coreference links: Recall: (1 / 1) 100%	Precision: (1 / 17) 5.88%	F1: 11.11%
     --------------------------------------------------------------------------
     Non-coreference links: Recall: (95 / 135) 70.37%	Precision: (95 / 308) 30.84%	F1: 42.88%
     --------------------------------------------------------------------------
     BLANC: Recall: (0.851851851851852 / 1) 85.18%	Precision: (0.183632543926662 / 1) 18.36%	F1: 27%
     */
}
