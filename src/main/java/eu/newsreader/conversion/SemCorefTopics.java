package eu.newsreader.conversion;

import eu.newsreader.util.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by piek on 23/12/15.
 */
public class SemCorefTopics {


    static public void main (String[] args) {
        try {
            OutputStream fosMissed = null;
            OutputStream fosInvented = null;

            String trigfolder ="";
            String conllPath = "";
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg.equals("--trig-folders") && args.length>(i+1)) {
                    trigfolder = args[i+1];
                }
                else if (arg.equals("--conll-files") && args.length>(i+1)) {
                    conllPath = args[i+1];
                }
                else if (arg.equals("--key-events")) {
                    SemCoref.KEYEVENTS = true;
                }
            }
            System.out.println("trigfolder = " + trigfolder);
            File conllFile = new File (conllPath);
            File outputMissed = new File (conllFile.getParentFile()+"/"+"response.missed.txt");
            File outputInvented = new File (conllFile.getParentFile()+"/"+"response.invented.txt");
            if (outputMissed.exists()) {
                fosMissed = new FileOutputStream(outputMissed, true);
            }
            else {
                fosMissed = new FileOutputStream(outputMissed);
            }
            if (outputInvented.exists()) {
                fosInvented = new FileOutputStream(outputInvented, true);
            }
            else {
                fosInvented = new FileOutputStream(outputInvented);
            }

            ArrayList<File> folders = Util.makeFolderList(new File (trigfolder));
            for (int i = 0; i < folders.size(); i++) {
                File folder = folders.get(i);
                String topic = folder.getName();
                File key = new File(conllFile.getAbsoluteFile()+"/"+"topic_"+topic+".key");
                if (key.exists()) {
                    HashMap<String, String> tokenIdMap = new HashMap<String, String>();
                    ArrayList<String> eventIdentifierArray = new ArrayList<String>();
                    tokenIdMap = SemCoref.readSemTrig(eventIdentifierArray,folder.getAbsolutePath());
                    SemCoref.addSemToCoNLL(fosMissed, fosInvented, key, tokenIdMap);
                }
            }


            fosInvented.close();
            fosMissed.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
