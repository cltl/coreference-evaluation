package eu.newsreader.util;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by piek on 12/15/14.
 */
public class Util {

    static public String getTokenId (String mention) {
        //1_10ecbplus.xml.naf.fix.xml#char=592,597&word=w116&term=t116&sentence=5
        String token = "";
        int idx_s = mention.indexOf("word=");
        if (idx_s>-1) {
            int idx_e = mention.indexOf( "&", idx_s);
            if (idx_e>-1) {
                token= getNumericId(mention.substring(idx_s, idx_e));
            }
        }
        return token;
    }

    static public String getFileFromMention (String mention) {
        //1_10ecbplus.xml.naf.fix.xml#char=592,597&word=w116&term=t116&sentence=5
        String file = "";
        int idx_s = mention.indexOf(".");
        if (idx_s>-1) {
            file= mention.substring(0, idx_s);
        }
        return file;
    }

    static public String getSentence (String mention) {
        //1_10ecbplus.xml.naf.fix.xml#char=592,597&word=w116&term=t116&sentence=5
        String sentence = "";
        if (mention.indexOf("sentence=")>-1) {
            int idx_s = mention.lastIndexOf("=");
            if (idx_s > -1) {
                sentence = mention.substring(idx_s + 1);
            }
        }
        return sentence;
    }

    static public String getNumericId (String id) {
        //1_10ecbplus.xml.naf.fix.xml#ev27
        final String number="12334567890";
        String numString = "";
        for (int i = 0; i < id.length(); i++) {
            char c = id.charAt(i);
            if (number.indexOf(c)>-1) {
                numString+= c;
            }
        }
        return numString;
    }


    static public ArrayList<File> makeFolderList(File inputFile) {
        ArrayList<File> folderList = new ArrayList<File>();
        File[] theFileList = null;
        if ((inputFile.canRead()) && inputFile.isDirectory()) {
            theFileList = inputFile.listFiles();
            for (int i = 0; i < theFileList.length; i++) {
                File newFile = theFileList[i];
                if (newFile.isDirectory()) {
                    folderList.add(newFile);
                }
            }
        } else {
            System.out.println("Cannot access file:" + inputFile + "#");
            if (!inputFile.exists()) {
                System.out.println("File does not exist!");
            }
        }
        return folderList;
    }


    static public ArrayList<File> makeRecursiveFileList(File inputFile, String theFilter) {
        ArrayList<File> acceptedFileList = new ArrayList<File>();
        File[] theFileList = null;
        if ((inputFile.canRead())) {
            theFileList = inputFile.listFiles();
            for (int i = 0; i < theFileList.length; i++) {
                File newFile = theFileList[i];
                if (newFile.isDirectory()) {
                    ArrayList<File> nextFileList = makeRecursiveFileList(newFile, theFilter);
                    acceptedFileList.addAll(nextFileList);
                } else {
                    if (newFile.getName().endsWith(theFilter)) {
                        acceptedFileList.add(newFile);
                    }
                }
            }
        } else {
            System.out.println("Cannot access file:" + inputFile + "#");
            if (!inputFile.exists()) {
                System.out.println("File/folder does not exist!");
            }
        }
        return acceptedFileList;
    }

    static public ArrayList<File> makeRecursiveFileList(File inputFile) {
        ArrayList<File> acceptedFileList = new ArrayList<File>();
        File[] theFileList = null;
        if ((inputFile.canRead())) {
            theFileList = inputFile.listFiles();
            for (int i = 0; i < theFileList.length; i++) {
                File newFile = theFileList[i];
                if (newFile.isDirectory()) {
                    ArrayList<File> nextFileList = makeRecursiveFileList(newFile);
                    acceptedFileList.addAll(nextFileList);
                } else {
                    acceptedFileList.add(newFile);
                }
            }
        } else {
            System.out.println("Cannot access file:" + inputFile + "#");
            if (!inputFile.exists()) {
                System.out.println("File/folder does not exist!");
            }
        }
        return acceptedFileList;
    }

    static public ArrayList<File> makeFlatFileList(File inputFile, String theFilter) {
        ArrayList<File> acceptedFileList = new ArrayList<File>();
        File[] theFileList = null;
        if ((inputFile.canRead()) && inputFile.isDirectory()&&!inputFile.isHidden()) {
            theFileList = inputFile.listFiles();
            for (int i = 0; i < theFileList.length; i++) {
                File newFile = theFileList[i];
                if (!newFile.isDirectory()) {
                    if (newFile.getName().endsWith(theFilter)) {
                        acceptedFileList.add(newFile);
                    }
                }
            }
        } else {
            System.out.println("Cannot access file:" + inputFile + "#");
            if (!inputFile.exists()) {
                System.out.println("File does not exist!");
            }
        }
        return acceptedFileList;
    }

    static public ArrayList<File> makeFlatFileList(File inputFile) {
        ArrayList<File> acceptedFileList = new ArrayList<File>();
        File[] theFileList = null;
        if ((inputFile.canRead()) && inputFile.isDirectory() &&!inputFile.isHidden()) {
            theFileList = inputFile.listFiles();
            for (int i = 0; i < theFileList.length; i++) {
                File newFile = theFileList[i];
                if (!newFile.isDirectory()) {
                    if (!newFile.getName().startsWith(".")) {
                        acceptedFileList.add(newFile);
                    }
                }
            }
        } else {
            System.out.println("Cannot access file:" + inputFile + "#");
            if (!inputFile.exists()) {
                System.out.println("File does not exist!");
            }
        }
        return acceptedFileList;
    }

    static public ArrayList<String> getSentenceIdsConllFile(File file) {
        ArrayList<String> sentenceIds = new ArrayList<String>();
        if (file.exists() ) {
            try {
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                while (in.ready()&&(inputLine = in.readLine()) != null) {
                    //System.out.println(inputLine);
                    if (inputLine.trim().length()>0) {
                        String[] fields = inputLine.split("\t");
                        if (fields.length==5) {
                            String currentSentence = fields[1];
                            if (!sentenceIds.contains(currentSentence)) {
                                sentenceIds.add(currentSentence);
                            }
                        }
                    }
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sentenceIds;
    }


    static public String reduceFileToCoreferenceAnnotatedSentences(File file) {
        String buffer = "";
        if (file.exists() ) {
            try {
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                boolean annotated = false;
                String sentenceBuffer = "";
                String previousSentence = "";
                String currentSentence = "";
                while (in.ready()&&(inputLine = in.readLine()) != null) {
                    //System.out.println(inputLine);
                    if (inputLine.trim().length()>0) {
                        String[] fields = inputLine.split("\t");
                        if (fields.length==5) {
                            currentSentence = fields[1];
                            if (currentSentence.equals(previousSentence)) {
                               sentenceBuffer += inputLine+"\n";
                               if (!fields[4].equals("-")) {
                                  annotated = true;
                               }
                            }
                            else {
                                //// new sentence

                                //// Next should not happen if empty lines separate sentences, however just in case.....
                                if (annotated) {
                                    /// store previous sentenceBuffer because it was annotated
                                    buffer += sentenceBuffer+"\n";
                                    sentenceBuffer = "";
                                    annotated = false;
                                }
                                /// proceed with the new sentence
                                sentenceBuffer = inputLine+"\n";
                                if (!fields[4].equals("-")) {
                                    annotated = true;
                                }
                                previousSentence = currentSentence;
                            }
                        }
                        else {
                            /// either beginning or ending line or empty lines separating sentences
                            if (annotated) {
                                /// store previous sentenceBuffer because it was annotated
                                buffer += sentenceBuffer+"\n";
                                annotated = false;
                                sentenceBuffer = "";
                            }
                            buffer += inputLine+"\n";
                            annotated = false;
                        }
                    }
                }
                /// we assume the last line is the end of document line so there is no sentenceBuffer to check
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer;
    }

    static public String reduceConllFileForSentenceIds(File file, ArrayList<String> sentenceIds) {
        String buffer = "";
        if (file.exists() ) {
            try {
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                String currentSentence = "";
                while (in.ready()&&(inputLine = in.readLine()) != null) {
                    if (inputLine.trim().length()>0) {
                        String[] fields = inputLine.split("\t");
                        if (fields.length==5) {
                            currentSentence = fields[1];
                            if (sentenceIds.contains(currentSentence))  {
                               buffer += inputLine+"\n";
                            }
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


    static public String readFirstSentence(File file) {
        String s1 = "";
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                while (in.ready() && (inputLine = in.readLine()) != null) {
                    if (inputLine.startsWith("#")) {
                        s1 = inputLine;
                        break;
                    }
                }
            }
            catch (IOException e) {

            }
        }
        return s1;
    }
                    /**
                     * 9549_Reactions_to_Apple	6	143	Entirely	-
                     9549_Reactions_to_Apple	6	144	new	-
                     9549_Reactions_to_Apple	6	145	features	-
                     9549_Reactions_to_Apple	6	146	have	-
                     9549_Reactions_to_Apple	6	147	also	-
                     9549_Reactions_to_Apple	6	148	been	-
                     9549_Reactions_to_Apple	6	149	rolled	-

                     */
}
