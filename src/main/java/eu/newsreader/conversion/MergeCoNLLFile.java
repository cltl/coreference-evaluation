package eu.newsreader.conversion;

import eu.newsreader.util.Util;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by piek on 16/10/15.
 */
public class MergeCoNLLFile {


    static public void main (String[] args) {


        String pathToInPutFolder = "";
        String pathToOutputFile = "";
        String extension = "";
        ArrayList<File> files = Util.makeFlatFileList(new File(pathToInPutFolder), extension);
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);

        }

    }
}
