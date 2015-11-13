package eu.newsreader.util;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by piek on 04/11/15.
 */
public class GetElementStats extends DefaultHandler{
    public String value = "";
    public HashMap<String, Integer> map = new HashMap<String, Integer>();

    public GetElementStats() {
        this.map = map = new HashMap<String, Integer>();
    }

    static public void main (String[] args) {
        String folder = "/Users/piek/Desktop/NWR/ECB/ECB+_LREC2014/ECB+CoNLL";
        GetElementStats parser = new GetElementStats();

        ArrayList<File> files = Util.makeRecursiveFileList(new File(folder), ".xml");
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            parser.parseFile(file.getAbsolutePath());
        }
        parser.printMap();
    }

    public void parseFile(String filePath) {
        String myerror = "";
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            SAXParser parser = factory.newSAXParser();
            InputSource inp = new InputSource (new FileReader(filePath));
            parser.parse(inp, this);
        } catch (SAXParseException err) {
            myerror = "\n** Parsing error" + ", line " + err.getLineNumber()
                    + ", uri " + err.getSystemId();
            myerror += "\n" + err.getMessage();
            System.out.println("myerror = " + myerror);
        } catch (SAXException e) {
            Exception x = e;
            if (e.getException() != null)
                x = e.getException();
            myerror += "\nSAXException --" + x.getMessage();
            System.out.println("myerror = " + myerror);
        } catch (Exception eee) {
            eee.printStackTrace();
            myerror += "\nException --" + eee.getMessage();
            System.out.println("myerror = " + myerror);
        }
        //System.out.println("myerror = " + myerror);
    }//--c


    void printMap () {
        Set keySet = map.keySet();
        Iterator<String> keys = keySet.iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            Integer cnt = map.get(key);
            System.out.println(key+"\t"+cnt);
        }
    }
    public void startElement(String uri, String localName,
                             String qName, Attributes attributes)
            throws SAXException {
        if (map.containsKey(qName)) {
            Integer cnt = map.get(qName);
            cnt++;
            map.put(qName, cnt);
        }
        else {
            map.put(qName, 1);
        }

        value = "";
    }//--startElement



    public void endElement(String uri, String localName, String qName)
            throws SAXException {
    }

    public void characters(char ch[], int start, int length)
            throws SAXException {
        value += new String(ch, start, length);
        // System.out.println("tagValue:"+value);
    }
}
