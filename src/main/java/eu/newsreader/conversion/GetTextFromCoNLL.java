package eu.newsreader.conversion;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by piek on 22/10/2017.
 */
public class GetTextFromCoNLL {



    static public void main (String[] args) {
        String path = "/Users/piek/Desktop/gwc-referencenet/data/Piek/773797.conll";
        String incident = path;
        int idx = path.lastIndexOf(".");
        if (idx>-1) incident = path.substring(0, idx);
        int tokColumn=2;
        readCoNLLFile(path, incident, tokColumn);
    }

    static void readCoNLLFile (String coNLLfile, String incident, int tokenColumn) {
        /*
        1_10ecb	0	1	Perennial	-
        1_10ecb	0	2	party	-
        1_10ecb	0	3	girl	-
        1_10ecb	0	4	Tara	-
        1_10ecb	0	5	Reid	-
        1_10ecb	0	6	checked	(132016236402809085484)
        1_10ecb	0	7	herself	-
        1_10ecb	0	8	into	(132016236402809085484)


        #begin document (abc4c58e9b7621b10a4732a98dc273b3);
abc4c58e9b7621b10a4732a98dc273b3	DCT	2017-02-15	DCT	-
abc4c58e9b7621b10a4732a98dc273b3	1.0	Kenosha	TITLE	-
abc4c58e9b7621b10a4732a98dc273b3	1.1	fatal	TITLE	d.1
#end document
#begin document (ea781ee5a57a46b285d834708fee8c0d);


         */
        // System.out.println("kafFile.getName() = " + kafFile.getName());
        HashMap<String, CoNLLdata> conllDataMap = new HashMap<String,CoNLLdata>();
        try {
            FileInputStream fis = new FileInputStream(coNLLfile);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader in = new BufferedReader(isr);
            String inputLine = "";
            HashMap<String, String> fileContentMap = new HashMap<String, String>();
            while (in.ready() && (inputLine = in.readLine()) != null) {
                String[] fields = inputLine.split("\t");
                if (fields.length > tokenColumn) {
                    String fileName = fields[0];
                    String token = fields[tokenColumn];
                    if (token.equals("NEWLINE")) token="\n";
                    if (fileContentMap.containsKey(fileName)) {
                        String content = fileContentMap.get(fileName);
                        content += " "+token;
                        fileContentMap.put(fileName, content);
                    }
                    else {
                        fileContentMap.put(fileName, token);
                    }
                }
            }
            in.close();
            Set keySet = fileContentMap.keySet();
            Iterator<String> keys = keySet.iterator();
            while (keys.hasNext()) {
                String file = keys.next();
                String content = fileContentMap.get(file);
                String filePath = incident+ "---"+file+".txt";
                OutputStream fos = new FileOutputStream(filePath);
                fos.write(content.getBytes());
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
