package eu.newsreader.util;

import eu.kyotoproject.kaf.KafSaxParser;
import eu.kyotoproject.kaf.KafWordForm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by piek on 12/07/15.
 */
public class DecreaseSentenceNumbers {


    static public void main (String[] args) {
        String pathToFolder = "";
        pathToFolder = "/Users/piek/Desktop/NWR/NWR-benchmark/ecb/data/ecb_pip";
        KafSaxParser kafSaxParser = new KafSaxParser();
        ArrayList<File> files = Util.makeRecursiveFileList(new File(pathToFolder), ".xml");
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            kafSaxParser.parseFile(file);
            for (int j = 0; j < kafSaxParser.getKafWordFormList().size(); j++) {
                KafWordForm kafWordForm = kafSaxParser.getKafWordFormList().get(j);
                Integer sentenceNr = Integer.parseInt(kafWordForm.getSent());
                sentenceNr--;
                kafWordForm.setSent(sentenceNr.toString());
            }
            try {
                String filePath = file.getParent();
                String fileName = file.getName();
                int idx = fileName.indexOf(".fix");
                fileName = fileName.substring(0, idx);
                filePath += "/"+fileName;
                OutputStream fos = new FileOutputStream(filePath);
                kafSaxParser.writeNafToStream(fos);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
