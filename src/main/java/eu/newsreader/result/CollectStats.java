package eu.newsreader.result;

import eu.newsreader.util.Util;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by piek on 12/16/14.
 */
public class CollectStats {

    public static class Stats {

        private int keyReferenceSets = 0;
        private int keyNoreferenceSets = 0;
        private int keyCoreferenceSets = 0;
        private double keyAverageMentions = 0;
        private double keyAverageLemmas = 0;

        private int responseReferenceSets = 0;
        private int responseNoreferenceSets = 0;
        private int responseCoreferenceSets = 0;
        private double responseAverageMentions = 0;
        private double responseAverageLemmas = 0;

        public Stats() {
            keyReferenceSets = 0;
            keyNoreferenceSets = 0;
            keyCoreferenceSets = 0;
            keyAverageMentions = 0;
            keyAverageLemmas = 0;

            responseReferenceSets = 0;
            responseNoreferenceSets = 0;
            responseCoreferenceSets = 0;
            responseAverageMentions = 0;
            responseAverageLemmas = 0;
        }

        public double getKeyAverageLemmas() {
            return keyAverageLemmas;
        }

        public void setKeyAverageLemmas(String keyAverageLemmas) {
            this.keyAverageLemmas = Double.parseDouble(keyAverageLemmas);
        }

        public double getKeyAverageMentions() {
            return keyAverageMentions;
        }

        public void setKeyAverageMentions(String keyAverageMentions) {
            this.keyAverageMentions = Double.parseDouble(keyAverageMentions);
        }

        public int getKeyCoreferenceSets() {
            return keyCoreferenceSets;
        }

        public void setKeyCoreferenceSets(String keyCoreferenceSets) {
            this.keyCoreferenceSets = Integer.parseInt(keyCoreferenceSets);
        }

        public int getKeyNoreferenceSets() {
            return keyNoreferenceSets;
        }

        public void setKeyNoreferenceSets(String keyNoreferenceSets) {
            this.keyNoreferenceSets = Integer.parseInt(keyNoreferenceSets);
        }

        public int getKeyReferenceSets() {
            return keyReferenceSets;
        }

        public void setKeyReferenceSets(String keyReferenceSets) {
            this.keyReferenceSets = Integer.parseInt(keyReferenceSets);
        }

        public double getResponseAverageLemmas() {
            return responseAverageLemmas;
        }

        public void setResponseAverageLemmas(String responseAverageLemmas) {
            this.responseAverageLemmas = Double.parseDouble(responseAverageLemmas);
        }

        public double getResponseAverageMentions() {
            return responseAverageMentions;
        }

        public void setResponseAverageMentions(String responseAverageMentions) {
            this.responseAverageMentions = Double.parseDouble(responseAverageMentions);
        }

        public int getResponseCoreferenceSets() {
            return responseCoreferenceSets;
        }

        public void setResponseCoreferenceSets(String responseCoreferenceSets) {
            this.responseCoreferenceSets = Integer.parseInt(responseCoreferenceSets);
        }

        public int getResponseNoreferenceSets() {
            return responseNoreferenceSets;
        }

        public void setResponseNoreferenceSets(String responseNoreferenceSets) {
            this.responseNoreferenceSets = Integer.parseInt(responseNoreferenceSets);
        }

        public int getResponseReferenceSets() {
            return responseReferenceSets;
        }

        public void setResponseReferenceSets(String responseReferenceSets) {
            this.responseReferenceSets = Integer.parseInt(responseReferenceSets);
        }
    }

    /**
     * KEY:
     KEY # coref sets	18
     KEY # singletons	1
     KEY # multitons	17
     KEY Average mentions	9,5294117647
     KEY Average lemmas	4,5882352941
     RESPONSE:
     RESPONSE # coref sets	97
     RESPONSE # singletons	37
     RESPONSE # multitons	60
     RESPONSE Average mentions	5,5666666667
     RESPONSE Average lemmas	4,9833333333
     */

    /**
     *
     * @param args
     */
    static public void main (String[] args) {
        String pathToResponseFolder = "";
        String extension = "";
        String label = "";

        pathToResponseFolder = "/Users/piek/Desktop/NWR/NWR-benchmark/ecb/cross-document";
        extension = ".mention-instance-stats.csv";
        label = "ecb-nwr";
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

        ArrayList<Stats> topicStats = new ArrayList<Stats>();
        String pathToResultFile = new File(pathToResponseFolder).getParent()+"/"+label+".stats.csv";
        ArrayList<File> resultFiles = Util.makeRecursiveFileList(new File(pathToResponseFolder), extension);
        for (int i = 0; i < resultFiles.size(); i++) {
            File resultFile = resultFiles.get(i);
            readStatFile(resultFile, topicStats);
        }
        Date date = new Date();
        String str = "";
        str = new File(pathToResponseFolder).getParent()+"\t"+label+"\n";
        str += "Date\t"+date.toString()+"\n\n";
        str += "Nr. of stat files\t"+resultFiles.size()+"\n";

        for (int i = 0; i < resultFiles.size(); i++) {
            File file = resultFiles.get(i);
            str += "\t"+file.getName();
        }
        str += "\n";
        str += "KEY # coref sets";
        for (int i = 0; i < topicStats.size(); i++) {
            Stats stats = topicStats.get(i);
            str += "\t"+stats.getKeyReferenceSets();
        }
        str += "\n";
        str += "KEY # singletons";
        for (int i = 0; i < topicStats.size(); i++) {
            Stats stats = topicStats.get(i);
            str += "\t"+stats.getKeyNoreferenceSets();
        }
        str += "\n";

        str += "KEY # multitons";
        for (int i = 0; i < topicStats.size(); i++) {
            Stats stats = topicStats.get(i);
            str += "\t"+stats.getKeyCoreferenceSets();
        }
        str += "\n";

        str += "KEY Average mentions";
        for (int i = 0; i < topicStats.size(); i++) {
            Stats stats = topicStats.get(i);
            str += "\t"+stats.getKeyAverageMentions();
        }
        str += "\n";
        str += "KEY Average lemmas";
        for (int i = 0; i < topicStats.size(); i++) {
            Stats stats = topicStats.get(i);
            str += "\t"+stats.getKeyAverageLemmas();
        }
        str += "\n";
        str += "RESPONSE # coref sets";
        for (int i = 0; i < topicStats.size(); i++) {
            Stats stats = topicStats.get(i);
            str += "\t"+stats.getResponseReferenceSets();
        }
        str += "\n";
        str += "RESPONSE # singletons";
        for (int i = 0; i < topicStats.size(); i++) {
            Stats stats = topicStats.get(i);
            str += "\t"+stats.getResponseNoreferenceSets();
        }
        str += "\n";
        str += "RESPONSE # multitons";
        for (int i = 0; i < topicStats.size(); i++) {
            Stats stats = topicStats.get(i);
            str += "\t"+stats.getResponseCoreferenceSets();
        }
        str += "\n";
        str += "RESPONSE Average mentions";
        for (int i = 0; i < topicStats.size(); i++) {
            Stats stats = topicStats.get(i);
            str += "\t"+stats.getResponseAverageMentions();
        }
        str += "\n";
        str += "RESPONSE Average lemmas";
        for (int i = 0; i < topicStats.size(); i++) {
            Stats stats = topicStats.get(i);
            str += "\t"+stats.getResponseAverageLemmas();
        }
        str += "\n";

        try {
            OutputStream fos = new FileOutputStream(pathToResultFile);
            fos.write(str.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    static public void readStatFile(File file, ArrayList<Stats> topicStats) {
        Stats topicStat = new Stats();
        if (file.exists() ) {
            try {
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                while (in.ready()&&(inputLine = in.readLine()) != null) {
                    /**
                     /**
                     * KEY:
                     KEY # coref sets	18
                     KEY # singletons	1
                     KEY # multitons	17
                     KEY Average mentions	9,5294117647
                     KEY Average lemmas	4,5882352941
                     RESPONSE:
                     RESPONSE # coref sets	97
                     RESPONSE # singletons	37
                     RESPONSE # multitons	60
                     RESPONSE Average mentions	5,5666666667
                     RESPONSE Average lemmas	4,9833333333
                     */
                    if (inputLine.startsWith("KEY # coref sets")) {
                       topicStat.setKeyReferenceSets(inputLine.substring(inputLine.indexOf("\t") + 1));
                    }
                    else if (inputLine.startsWith("KEY # singletons")) {
                       topicStat.setKeyNoreferenceSets(inputLine.substring(inputLine.indexOf("\t") + 1));
                    }
                    else if (inputLine.startsWith("KEY # multitons")) {
                       topicStat.setKeyCoreferenceSets(inputLine.substring(inputLine.indexOf("\t") + 1));
                    }
                    else if (inputLine.startsWith("KEY Average mentions")) {
                       topicStat.setKeyAverageMentions(inputLine.substring(inputLine.indexOf("\t") + 1));
                    }
                    else if (inputLine.startsWith("KEY Average lemmas")) {
                       topicStat.setKeyAverageLemmas(inputLine.substring(inputLine.indexOf("\t")+1));
                    }
                    else if (inputLine.startsWith("RESPONSE # coref sets")) {
                        topicStat.setResponseReferenceSets(inputLine.substring(inputLine.indexOf("\t") + 1));
                    }
                    else if (inputLine.startsWith("RESPONSE # singletons")) {
                       topicStat.setResponseNoreferenceSets(inputLine.substring(inputLine.indexOf("\t") + 1));
                    }
                    else if (inputLine.startsWith("RESPONSE # multitons")) {
                       topicStat.setResponseCoreferenceSets(inputLine.substring(inputLine.indexOf("\t") + 1));
                    }
                    else if (inputLine.startsWith("RESPONSE Average mentions")) {
                       topicStat.setResponseAverageMentions(inputLine.substring(inputLine.indexOf("\t") + 1));
                    }
                    else if (inputLine.startsWith("RESPONSE Average lemmas")) {
                       topicStat.setResponseAverageLemmas(inputLine.substring(inputLine.indexOf("\t")+1));
                    }
                    else {
                       // System.out.println("inputLine = " + inputLine);
                    }
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        topicStats.add(topicStat);
    }

}
