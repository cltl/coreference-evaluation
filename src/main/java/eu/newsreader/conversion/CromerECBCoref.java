package eu.newsreader.conversion;

import eu.kyotoproject.kaf.CorefTarget;
import eu.kyotoproject.kaf.KafCoreferenceSet;
import eu.kyotoproject.kaf.KafTerm;
import eu.kyotoproject.kaf.KafWordForm;
import eu.newsreader.util.Util;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by piek on 12/15/14.
 */
public class CromerECBCoref extends DefaultHandler {

    /**
     * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     <co-ref-sets corpus="eecb1.0">
     <co-refs id="eecb1.0/44_52">
     <target termId="t102"/>
     <target termId="t99”/>
     </co-refs>
     <co-refs id="eecb1.0/44_51">
     <target termId="t43"/>
     <target termId="t66" />
     </co-refs>
     </co-ref-sets>

     */
    /*
   <Document doc_name="1_1ecb.xml" doc_id="DOC15653231215902085">
<token t_id="1" sentence="0" number="0">Another</token>
<token t_id="2" sentence="0" number="1">day</token>
<token t_id="3" sentence="0" number="2">in</token>
<token t_id="4" sentence="0" number="3">Hollywood</token>
<token t_id="5" sentence="0" number="4">;</token>
<token t_id="6" sentence="0" number="5">another</token>
<token t_id="7" sentence="0" number="6">star</token>
<token t_id="8" sentence="0" number="7">in</token>
<token t_id="9" sentence="0" number="8">rehab</token>
<token t_id="10" sentence="0" number="9">.</token>

<HUMAN_PART_PER m_id="32"  >
  <token_anchor t_id="81"/>
  <token_anchor t_id="82"/>
</HUMAN_PART_PER>
<HUMAN_PART_PER m_id="33"  >
  <token_anchor t_id="85"/>
</HUMAN_PART_PER>
<HUMAN_PART_PER m_id="18"  >
  <token_anchor t_id="22"/>
  <token_anchor t_id="23"/>
</HUMAN_PART_PER>
<HUMAN_PART_PER m_id="25"  >
  <token_anchor t_id="74"/>
</HUMAN_PART_PER>
<LOC_FAC m_id="37"  >
  <token_anchor t_id="88"/>
  <token_anchor t_id="89"/>
</LOC_FAC>
<LOC_FAC m_id="22"  >
  <token_anchor t_id="47"/>
  <token_anchor t_id="48"/>
  <token_anchor t_id="49"/>
  <token_anchor t_id="50"/>
  <token_anchor t_id="51"/>
  <token_anchor t_id="52"/>
  <token_anchor t_id="53"/>
  <token_anchor t_id="54"/>
</LOC_FAC>
<LOC_FAC m_id="23"  >
  <token_anchor t_id="58"/>
</LOC_FAC>
<NON_HUMAN_PART m_id="26"  >
  <token_anchor t_id="31"/>
  <token_anchor t_id="32"/>
</NON_HUMAN_PART>


<ACTION_OCCURRENCE m_id="28"  >
  <token_anchor t_id="35"/>
</ACTION_OCCURRENCE>
<ACTION_OCCURRENCE m_id="29"  >
  <token_anchor t_id="46"/>
</ACTION_OCCURRENCE>
<ACTION_OCCURRENCE m_id="35"  >
  <token_anchor t_id="87"/>
</ACTION_OCCURRENCE>
<ACTION_OCCURRENCE m_id="24"  >
  <token_anchor t_id="69"/>
</ACTION_OCCURRENCE>
<ACTION_REPORTING m_id="31"  >
  <token_anchor t_id="77"/>
</ACTION_REPORTING>
<ACTION_REPORTING m_id="20"  >
  <token_anchor t_id="12"/>
</ACTION_REPORTING>

<ACTION_OCCURRENCE m_id="50" RELATED_TO="" TAG_DESCRIPTOR="t1b_checking_in_promises" instance_id="ACT16236402809085484" />
<ACTION_OCCURRENCE m_id="49" RELATED_TO="" TAG_DESCRIPTOR="t1b_starred_american_pie" instance_id="ACT16284692120891174" />
<HUMAN_PART_PER m_id="48" RELATED_TO="" TAG_DESCRIPTOR="t1b_rep" instance_id="HUM16284637796168708" />
<HUMAN_PART_ORG m_id="47" RELATED_TO="" TAG_DESCRIPTOR="t1b_people" instance_id="HUM16236907954762763" />
<HUMAN_PART_PER m_id="46" RELATED_TO="" TAG_DESCRIPTOR="t1b_tara_reid" instance_id="HUM16236184328979740" />
<LOC_FAC m_id="45" RELATED_TO="" TAG_DESCRIPTOR="t1b_promises_maliby" instance_id="LOC16235213289813758" />
<ACTION_REPORTING m_id="44" RELATED_TO="" TAG_DESCRIPTOR="t1b_people_reports" instance_id="ACT16236892266224146" />
<ACTION_REPORTING m_id="43" RELATED_TO="" TAG_DESCRIPTOR="t1b_publicist_statement_reid_in_rehab_and_respect_privacy" instance_id="ACT16235311629112331" />
</Markables>

<Relations>
<CROSS_DOC_COREF r_id="37538" note="ACT16235311629112331" >
  <source m_id="41" />
  <target m_id="43" />
</CROSS_DOC_COREF>

     */
    String value = "";
    static public HashMap<String, String> crossDocIdMap;
    static public HashMap<String, KafCoreferenceSet> coreferenceHashMap;
    static public ArrayList<KafCoreferenceSet> kafCoreferenceSetArrayList;
    ArrayList<CorefTarget> corefTargetArrayList;
    KafCoreferenceSet kafCoreferenceSet;
    CorefTarget corefTarget;
    HashMap<String, String> eventMentionTypeMap;
    KafWordForm kafWordForm;
    static public ArrayList<KafWordForm> kafWordFormArrayList;
    KafTerm kafTerm;
    static public ArrayList<KafTerm> kafTermArrayList;
    ArrayList<String> spans;
    String span = "";
    String crossDocId = "";
    String target = "";
    boolean REFERSTO = false;

    void init() {
        value = "";
        crossDocIdMap = new HashMap<String, String>();
        coreferenceHashMap = new HashMap<String, KafCoreferenceSet>();
        kafCoreferenceSetArrayList = new ArrayList<KafCoreferenceSet>();
        corefTargetArrayList = new ArrayList<CorefTarget>();
        kafCoreferenceSet = new KafCoreferenceSet();
        corefTarget = new CorefTarget();
        eventMentionTypeMap = new HashMap<String, String>();
        kafWordForm = new KafWordForm();
        kafWordFormArrayList = new ArrayList<KafWordForm>();
        kafTerm = new KafTerm();
        kafTermArrayList = new ArrayList<KafTerm>();
        REFERSTO = false;
        target = "";
    }

    public void parseFile(String filePath) {
        String myerror = "";
        init();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            SAXParser parser = factory.newSAXParser();
            InputSource inp = new InputSource (new FileReader(filePath));
            parser.parse(inp, this);
            switchToTokenIds();
            useCrossDocIds();

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



    public void startElement(String uri, String localName,
                             String qName, Attributes attributes)
            throws SAXException {

        if (qName.equalsIgnoreCase("token")) {
            kafWordForm = new KafWordForm();
            kafWordForm.setWid(attributes.getValue("t_id"));
            Integer sentenceInt = Integer.parseInt(attributes.getValue("sentence"));
            kafWordForm.setSent(sentenceInt.toString());
        }
        else if (qName.startsWith("ACTION_")) {
            String type = qName;
            String mention = attributes.getValue("m_id");
            if (type!=null && mention!=null && !type.isEmpty() && !mention.isEmpty()) {
                eventMentionTypeMap.put(mention, type);
            }
            kafTerm = new KafTerm();
            kafTerm.setType("EVENT");
            kafTerm.setTid(attributes.getValue("m_id"));
        }
        else if (qName.indexOf("_PAR_")>-1) {
            kafTerm = new KafTerm();
            kafTerm.setType("ENTITY");
            kafTerm.setTid(attributes.getValue("m_id"));
        }
        else if (qName.equalsIgnoreCase("source")) {
            //// sources are mentions that match with the same target. The same target can occur in different refers to mappings
            if (REFERSTO) {
                /// sources and targets can also occur for other relations than refersto
                corefTarget = new CorefTarget();
                corefTarget.setId(attributes.getValue("m_id"));
                corefTargetArrayList.add(corefTarget);
            }
        }
        else if (qName.equalsIgnoreCase("target")) {
            if (REFERSTO) {
                /// sources and targets can also occur for other relations than refersto
                target = attributes.getValue("m_id");
                if (!crossDocId.isEmpty()) {
                   crossDocIdMap.put(target, crossDocId);
                }
            }
        }
        else if (qName.equalsIgnoreCase("token_anchor")) {
            span = attributes.getValue("t_id");
            kafTerm.addSpans(span);
        }
        else if (qName.equalsIgnoreCase("CROSS_DOC_COREF") ||
                 qName.equalsIgnoreCase("INTRA_DOC_COREF")) {
            REFERSTO = true;
            corefTargetArrayList = new ArrayList<CorefTarget>();
            target = "";
            if (qName.equalsIgnoreCase("CROSS_DOC_COREF")) {
                crossDocId = fixCrossDocId(attributes.getValue("note"));
            }
            else {
                crossDocId = "";
            }
        }
        value = "";
    }//--startElement

    String fixCrossDocId (String id) {
        final String charString = "abcdefghijklmnopqrstuvwxyz";
        String str = "";
        for (int i = 0; i < id.length(); i++) {
            char c = id.toLowerCase().charAt(i);
            int idx = charString.indexOf(c);
            if (idx>-1) {
                str += idx+1;
            }
            else {
               str += c;
            }
        }
        //System.out.println("str = " + str);
        return str;
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (qName.equalsIgnoreCase("token")) {
            kafWordForm.setWf(value.trim());
            kafWordFormArrayList.add(kafWordForm);
            kafWordForm = new KafWordForm();
        }
        else if (qName.startsWith("ACTION_")) {
            kafTermArrayList.add(kafTerm);
            kafTerm = new KafTerm();
        }
        else if (qName.indexOf("_PAR_")>-1) {
            kafTermArrayList.add(kafTerm);
            kafTerm = new KafTerm();
        }
        else if (qName.equalsIgnoreCase("CROSS_DOC_COREF") ||
                qName.equalsIgnoreCase("INTRA_DOC_COREF")) {
            REFERSTO = false;
            String type = "";
            if (eventMentionTypeMap.containsKey(target)) {
                type = eventMentionTypeMap.get(target);
            }
            if (!target.isEmpty()) {
                if (coreferenceHashMap.containsKey(target)) {
                    KafCoreferenceSet set = coreferenceHashMap.get(target);
                    set.addSetsOfSpans(corefTargetArrayList);
                    coreferenceHashMap.put(target, set);
                }
                else {
                    KafCoreferenceSet set = new KafCoreferenceSet();
                    set.setCoid(target);
                    set.setType(type);
                    set.addSetsOfSpans(corefTargetArrayList);
                    coreferenceHashMap.put(target, set);
                }
            }

//            corefTargetArrayList = new ArrayList<CorefTarget>();
//            kafCoreferenceSetArrayList.add(kafCoreferenceSet);
            corefTargetArrayList = new ArrayList<CorefTarget>();
            kafCoreferenceSet = new KafCoreferenceSet();
            target = "";
        }
    }

    public void useCrossDocIds () {
        for (int i = 0; i < kafCoreferenceSetArrayList.size(); i++) {
            KafCoreferenceSet coreferenceSet = kafCoreferenceSetArrayList.get(i);
            if (crossDocIdMap.containsKey(coreferenceSet.getCoid())) {
                crossDocId = crossDocIdMap.get(coreferenceSet.getCoid());
                coreferenceSet.setCoid(crossDocId);
            }
        }
    }

    public void switchToTokenIds () {
        Set keySet = coreferenceHashMap.keySet();
        Iterator<String> keys = keySet.iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            KafCoreferenceSet coreferenceSet = coreferenceHashMap.get(key);
            KafCoreferenceSet newCorefSet = new KafCoreferenceSet();
            newCorefSet.setCoid(key);
            //   System.out.println("newCorefSet.getCoid() = " + newCorefSet.getCoid());
            for (int j = 0; j < coreferenceSet.getSetsOfSpans().size(); j++) {
                ArrayList<CorefTarget> corefTargets = coreferenceSet.getSetsOfSpans().get(j);
                ArrayList<CorefTarget> newCorefTargets = new ArrayList<CorefTarget>();
                for (int k = 0; k < corefTargets.size(); k++) {
                    CorefTarget target = corefTargets.get(k);
                    // System.out.println("target.getId() = " + target.getId());
                    for (int l = 0; l < kafTermArrayList.size(); l++) {
                        KafTerm term = kafTermArrayList.get(l);
                        if (term.getTid().equals(target.getId())) {
                            /// we found the term that includes the target id
                            /// now we should take all the tokens of the term to create the span
                            for (int m = 0; m < term.getSpans().size(); m++) {
                                String span = term.getSpans().get(m);
                                CorefTarget newTarget = new CorefTarget();
                                newTarget.setId(span);
                                /// just to get the word
                                for (int w = 0; w < kafWordFormArrayList.size(); w++) {
                                    KafWordForm wordForm = kafWordFormArrayList.get(w);
                                    if (wordForm.getWid().equals(span)) {
                                        newTarget.setTokenString(wordForm.getWf());
                                        break;
                                    }
                                }
                                newCorefTargets.add(newTarget);
                            }
                            if (newCorefSet.getType().isEmpty()) {
                                if (!term.getType().isEmpty())  {
                                    newCorefSet.setType(term.getType());
                                }
                            }
                        }
                    }

                }
                // System.out.println("newCorefSet.getType() = " + newCorefSet.getType());
                newCorefSet.addSetsOfSpans(newCorefTargets);
            }
            kafCoreferenceSetArrayList.add(newCorefSet);
        }
    }

    public void characters(char ch[], int start, int length)
            throws SAXException {
        value += new String(ch, start, length);
        // System.out.println("tagValue:"+value);
    }

    /**
        NEED TO CHECK THIS
    */
    public void serializeToCorefSet (OutputStream stream, String corpus, String type) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation impl = builder.getDOMImplementation();

            Document xmldoc = impl.createDocument(null, "co-ref-sets", null);
            xmldoc.setXmlStandalone(false);
            Element root = xmldoc.getDocumentElement();
            root.setAttribute("corpus", corpus);
            if (kafCoreferenceSetArrayList.size()>0) {
                Element coreferences = xmldoc.createElement("coreferences");
                for (int i = 0; i < this.kafCoreferenceSetArrayList.size(); i++) {
                    KafCoreferenceSet kaf  = kafCoreferenceSetArrayList.get(i);
                    if (kaf.getType().equalsIgnoreCase(type)) {
                        coreferences.appendChild(kaf.toNafXML(xmldoc));
                    }
                    else {
                        //  System.out.println("coref.getType() = " + kaf.getType());
                    }
                }
                root.appendChild(coreferences);
            }

            DOMSource domSource = new DOMSource(xmldoc);
            TransformerFactory tf = TransformerFactory.newInstance();
            //tf.setAttribute("indent-number", 4);
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty(OutputKeys.INDENT,"yes");
            serializer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            StreamResult streamResult = new StreamResult(new OutputStreamWriter(stream));
            serializer.transform(domSource, streamResult);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    static public void main (String[] args) {
        String type = "";
        String format = "";
        String folder = "";
        String pathToCatFile = "";
        String fileExtension = "";
        // type = "EVENT-GRAMMATICAL";
        // type = "EVENT-SPEECH_COGNITIVE";
        // type = "EVENT-OTHER";
        // type = "ENTITY";
        //type = "EVENT";
        //pathToCatFile = "/Users/piek/Desktop/NWR/ECB+_LREC2014/ECB+/1/1_1ecb.xml";
        //folder = "/Users/piek/Desktop/NWR/NWR-benchmark/coreference/corpus_CAT_GS_201412/corpus_apple/";
        //fileExtension = ".xml";
        //format = "conll";
        CromerECBCoref catCoref = new CromerECBCoref();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equalsIgnoreCase("--cat-file") && args.length > (i + 1)) {
                pathToCatFile = args[i + 1];
            } else if (arg.equalsIgnoreCase("--folder") && args.length > (i + 1)) {
                folder = args[i + 1];
            } else if (arg.equalsIgnoreCase("--file-extension") && args.length > (i + 1)) {
                fileExtension = args[i + 1];
            } else if (arg.equalsIgnoreCase("--coref-type") && args.length > (i + 1)) {
                type = args[i + 1];

            } else if (arg.equalsIgnoreCase("--format") && args.length > (i + 1)) {
                format = args[i + 1];
            }
        }
        System.out.println("format = " + format);
        System.out.println("type = " + type);
        System.out.println("fileExtension = " + fileExtension);
        System.out.println("folder = " + folder);
        if (!pathToCatFile.isEmpty()) {
            catCoref.parseFile(pathToCatFile);
            OutputStream fos = null;
            try {
                fos = new FileOutputStream(pathToCatFile + "." + type + ".key");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String fileName = new File(pathToCatFile).getName();
            int idx = fileName.indexOf(".");
            if (idx > -1) {
                fileName = fileName.substring(0, idx);
            }

            if (format.equals("coref")) {
                catCoref.serializeToCorefSet(fos, fileName, type);
                //catCoref.serializeToCorefSet(System.out, new File (pathToCatFile).getName(), type);
            } else if (format.equals("conll")) {
                CoNLL.serializeToCoNLL(fos, fileName, type, kafWordFormArrayList, kafCoreferenceSetArrayList);
                // CoNLL.serializeToCoNLL(System.out, new File(pathToCatFile).getName(), type, kafWordFormArrayList, kafCoreferenceSetArrayList);
            }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (!folder.isEmpty()) {
            ArrayList<File> files = Util.makeFlatFileList(new File(folder), fileExtension);
            for (int i = 0; i < files.size(); i++) {
                File file = files.get(i);
                //System.out.println("file.getName() = " + file.getName());
                catCoref.parseFile(file.getAbsolutePath());

                String fileName = file.getName();
                int idx = fileName.indexOf(".");
                if (idx > -1) {
                    fileName = fileName.substring(0, idx);
                }
                // System.out.println("fileName = " + fileName);
                try {
                    OutputStream fos = new FileOutputStream(file.getAbsolutePath() + "." + type + ".key");
                    if (format.equals("coref")) {
                        catCoref.serializeToCorefSet(fos, fileName, type);
                    } else if (format.equals("conll")) {
                        CoNLL.serializeToCoNLL(fos, fileName, type, kafWordFormArrayList, kafCoreferenceSetArrayList);
                    }

                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
