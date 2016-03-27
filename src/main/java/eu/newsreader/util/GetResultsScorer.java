package eu.newsreader.util;

import java.io.*;

/**
 * Created by piek on 27/03/16.
 */
public class GetResultsScorer {

    //ecb_pip.gold.chain4+topic+sw.result.bcub
    static public void main (String[] args) {
        String file = args[0];
        String str = getLastLineResults(new File (file)).replace(".", ",");
        System.out.println(str);
    }


    static String getLastLineResults (File file) {
        String result = "";
        if (file.exists() ) {
            try {
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                while (in.ready()&&(inputLine = in.readLine()) != null) {
                    if (inputLine.indexOf("BLANC: Recall:")>-1) {
                         result = getResult(inputLine);
                    }
                    if (inputLine.indexOf("Coreference: Recall:")>-1) {
                         result = getResult(inputLine);
                    }
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    static String getResult (String inputLine) {
        /// This format is used by other scores than BLANC
        //Coreference: Recall: (1 / 2) 50%	Precision: (1 / 4) 25%	F1: 33.33%
        String recall = "";
        String precision = "";
        String f = "";
        String[] fields = inputLine.split("%");
        if (fields.length >= 3) {
            int idx = fields[0].indexOf(")");
            if (idx > -1) {
                recall =fields[0].substring(idx + 1).trim();
            }

            idx = fields[1].indexOf(")");
            if (idx > -1) {
                Double cnt = null;
                precision = fields[1].substring(idx + 1).trim();
            }

            idx = fields[2].lastIndexOf(":");
            if (idx > -1) {
                f = fields[2].substring(idx + 1).trim();
            }
        }
        String result = recall+"\t"+precision+"\t"+f;
        return result;
    }

    /**
     *====== TOTALS =======
     Identification of Mentions: Recall: (3477 / 3483) 99.82%	Precision: (3477 / 3482) 99.85%	F1: 99.84%
     --------------------------------------------------------------------------

     Coreference:
     Coreference links: Recall: (2449 / 14944) 16.38%	Precision: (2449 / 4658) 52.57%	F1: 24.98%
     --------------------------------------------------------------------------
     Non-coreference links: Recall: (312295 / 314997) 99.14%	Precision: (312295 / 325475) 95.95%	F1: 97.52%
     --------------------------------------------------------------------------
     BLANC: Recall: (0.577650310092855 / 1) 57.76%	Precision: (0.742633734010513 / 1) 74.26%	F1: 61.25%
     --------------------------------------------------------------------------

     */
    /**
     ====== TOTALS =======
     Identification of Mentions: Recall: (3477 / 3483) 99.82%	Precision: (3477 / 3482) 99.85%	F1: 99.84%
     --------------------------------------------------------------------------
     Coreference: Recall: (751 / 2049) 36.65%	Precision: (751 / 1005) 74.72%	F1: 49.18%
     --------------------------------------------------------------------------

     *
     */
}
