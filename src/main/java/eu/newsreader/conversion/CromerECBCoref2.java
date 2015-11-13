package eu.newsreader.conversion;

import eu.kyotoproject.kaf.KafTerm;
import eu.kyotoproject.kaf.KafWordForm;
import eu.newsreader.util.Util;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by piek on 12/15/14.
 */
public class CromerECBCoref2 extends DefaultHandler {

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

<INTRA_DOC_COREF r_id="32120" >
<source m_id="33" />
<source m_id="35" />
<target m_id="6" />
</INTRA_DOC_COREF>

<CROSS_DOC_COREF r_id="37538" note="ACT16235311629112331" >
  <source m_id="41" />
  <target m_id="43" />
</CROSS_DOC_COREF>

     */


    String value = "";
    static public HashMap<String, ArrayList<String>> termToCorefMap;
    static public HashMap<String, ArrayList<String>> tokenToCorefMap;
    KafWordForm kafWordForm;
    static public ArrayList<KafWordForm> kafWordFormArrayList;
    KafTerm kafTerm;
    static public ArrayList<KafTerm> kafTermArrayList;
    String span = "";
    String crossDocId = "";
    boolean REFERSTO = false;

    String singletonId = "";

    void init() {
        value = "";
        singletonId = "";
        termToCorefMap = new HashMap<String, ArrayList<String>>();
        tokenToCorefMap = new HashMap<String, ArrayList<String>>();
        kafWordForm = new KafWordForm();
        kafWordFormArrayList = new ArrayList<KafWordForm>();
        kafTerm = new KafTerm();
        kafTermArrayList = new ArrayList<KafTerm>();
        REFERSTO = false;
    }

    public void initSingletonId (String filePath) {
        singletonId = getIntFromFileName(filePath);
    }

    public String getIntFromFileName (String filePath) {
        String intString = "";
        String dig = "1234567890";
        File file = new File (filePath);
        String name = file.getName();
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (dig.indexOf(c)>-1) {
                intString+=c;
            }
        }
        if (intString.length() < 4) {
            for (int i = intString.length(); i < 4; i++) {
                intString +="0";
            }
        }
        if (name.indexOf("plus")>-1) {
            intString += "99";
        }
        else {
            intString += "88";
        }
      //  System.out.println("intString = " + intString);
        return intString;
    }

    public void parseFile(String filePath) {
        String myerror = "";
        init();
        initSingletonId(filePath);
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            SAXParser parser = factory.newSAXParser();
            InputSource inp = new InputSource (new FileReader(filePath));
            parser.parse(inp, this);
            addSingletons();
            makeTokenCorefMap();
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
        else if (qName.toLowerCase().startsWith("action_")) {
            String type = qName;
            String mention = attributes.getValue("m_id");
            if (type!=null && mention!=null && !type.isEmpty() && !mention.isEmpty()) {
                kafTerm = new KafTerm();
                kafTerm.setType("EVENT");
                kafTerm.setTid(mention);
            }
        }
        else if (qName.toLowerCase().indexOf("_par_")>-1) {
            kafTerm = new KafTerm();
            kafTerm.setType("ENTITY");
            kafTerm.setTid(attributes.getValue("m_id"));
        }
        else if (qName.equalsIgnoreCase("token_anchor")) {
            span = attributes.getValue("t_id");
            kafTerm.addSpans(span);
        }
        else if (qName.equalsIgnoreCase("CROSS_DOC_COREF") ||
                qName.equalsIgnoreCase("INTRA_DOC_COREF")) {
            REFERSTO = true;
            if (qName.equalsIgnoreCase("CROSS_DOC_COREF")) {
                crossDocId = fixCrossDocId(attributes.getValue("note"));
            }
            else if (qName.equalsIgnoreCase("INTRA_DOC_COREF")) {
                crossDocId = singletonId+fixCrossDocId(attributes.getValue("r_id"));
            }
            else {
                crossDocId = "";
            }
        }
        else if (qName.equalsIgnoreCase("source")) {
            //// sources are mentions that match with the same target. The same target can occur in different refers to mappings
            if (REFERSTO) {
                /// sources and targets can also occur for other relations than refersto
                String termId = attributes.getValue("m_id");
                if (!crossDocId.isEmpty()) {
                    if (termToCorefMap.containsKey(termId)) {
                        ArrayList<String> corefIds = termToCorefMap.get(termId);
                        if (!corefIds.contains(crossDocId)) {
                            corefIds.add(crossDocId);
                            termToCorefMap.put(termId, corefIds);
                        }
                    }
                    else {
                        ArrayList<String> corefIds = new ArrayList<String>();
                        corefIds.add(crossDocId);
                        termToCorefMap.put(termId, corefIds);
                    }
                }
            }
        }
        else if (qName.equalsIgnoreCase("target")) {
           /* if (REFERSTO) {
                /// sources and targets can also occur for other relations than refersto
                String termId = attributes.getValue("m_id");
                if (!crossDocId.isEmpty()) {
                    if (termToCorefMap.containsKey(termId)) {
                        ArrayList<String> corefIds = termToCorefMap.get(termId);
                        if (!corefIds.contains(crossDocId)) {
                            corefIds.add(crossDocId);
                            termToCorefMap.put(termId, corefIds);
                        }
                    }
                    else {
                        ArrayList<String> corefIds = new ArrayList<String>();
                        corefIds.add(crossDocId);
                        termToCorefMap.put(termId, corefIds);
                    }
                }
            }*/
        }

        value = "";
    }//--startElement


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
        }
    }


    public void characters(char ch[], int start, int length)
            throws SAXException {
        value += new String(ch, start, length);
        // System.out.println("tagValue:"+value);
    }

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

    public void addSingletons() {
        for (int i = 0; i < kafTermArrayList.size(); i++) {
            KafTerm term = kafTermArrayList.get(i);
            String termId = term.getTid();
            if (!termToCorefMap.containsKey(termId)) {
                ArrayList<String> corefIds = new ArrayList<String>();
                String id  = "";
                for (int j = 0; j < term.getSpans().size(); j++) {
                    String span =  term.getSpans().get(j);
                    KafWordForm kafWordForm = getKafWordForm(span);
                    if (kafWordForm!=null)
                    {
                        id+= kafWordForm.getSent()+kafWordForm.getWid();
                    }
                }
                if (id.isEmpty()) {
                    id = singletonId+term.getTid();
                }
                else {
                    id = singletonId + id;
                }
                corefIds.add(id);
                termToCorefMap.put(termId, corefIds);
            }
        }
    }

    public void makeTokenCorefMap() {
        for (int i = 0; i < kafTermArrayList.size(); i++) {
            KafTerm term = kafTermArrayList.get(i);
            String termId = term.getTid();
            if (termToCorefMap.containsKey(termId)) {
                ArrayList<String> corefIds = termToCorefMap.get(termId);
                for (int j = 0; j < term.getSpans().size(); j++) {
                    String span = term.getSpans().get(j);
                    if (tokenToCorefMap.containsKey(span)) {
                        ArrayList<String> tokenCoreIds = tokenToCorefMap.get(span);
                        for (int k = 0; k < corefIds.size(); k++) {
                            String s = corefIds.get(k);
                            if (!tokenCoreIds.contains(s))
                                tokenCoreIds.add(s);
                        }
                        tokenToCorefMap.put(span, tokenCoreIds);
                    }
                    else {
                        tokenToCorefMap.put(span, corefIds);
                    }
                }
            }
        }
    }

    public KafWordForm getKafWordForm (String tokenId) {
        for (int i = 0; i < kafWordFormArrayList.size(); i++) {
            KafWordForm wordForm = kafWordFormArrayList.get(i);
            if (wordForm.getWid().equals(tokenId)) {
                return wordForm;
            }
        }
        return null;
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
        type = "EVENT";
        //pathToCatFile = "/Users/piek/Desktop/NWR/ECB/ECB+_LREC2014/ECB+CoNLL/9/9_9ecb.xml";
        folder = "/Users/piek/Desktop/NWR/ECB/ECB+_LREC2014/ECB+";
        //folder = "/Users/piek/Desktop/NWR/NWR-benchmark/coreference/corpus_CAT_GS_201412/corpus_apple/";
        fileExtension = ".xml";
        format = "conll";
        CromerECBCoref2 catCoref = new CromerECBCoref2();
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
                if (type.isEmpty()) {
                    fos = new FileOutputStream(pathToCatFile + ".key");
                }
                else {
                    fos = new FileOutputStream(pathToCatFile + "." + type + ".key");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String fileName = new File(pathToCatFile).getName();
            int idx = fileName.indexOf(".");
            if (idx > -1) {
                fileName = fileName.substring(0, idx);
            }

            if (format.equals("coref")) {
            } else if (format.equals("conll")) {
                serializeToCoNLL(fos, fileName);
            }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (!folder.isEmpty()) {
            ArrayList<File> files = Util.makeRecursiveFileList(new File(folder), fileExtension);
            for (int i = 0; i < files.size(); i++) {
                File file = files.get(i);
                //System.out.println("file.getName() = " + file.getName());
                catCoref.parseFile(file.getAbsolutePath());

                String fileName = file.getName();
                int idx = fileName.indexOf(".");
                if (idx > -1) {
                    fileName = fileName.substring(0, idx);
                }
                String parentFolderPath = file.getParent()+"/"+"key";
                File parentFolder = new File (parentFolderPath);
                if (!parentFolder.exists()) parentFolder.mkdir();
                if (parentFolder.exists()) {
                    // System.out.println("fileName = " + fileName);
                    try {
                        OutputStream fos = null;
                        if (type.isEmpty()) {
                            fos = new FileOutputStream(parentFolderPath+"/"+file.getName() + ".key");
                        } else {
                            fos = new FileOutputStream(parentFolderPath+"/"+file.getName() + "." + type + ".key");
                        }
                        if (format.equals("coref")) {
                        }
                        else if (format.equals("conll")) {
                            serializeToCoNLL(fos, fileName);
                        }

                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    static public String getCoreferenceSetId(String wid) {
        String id = "";
        for (int i = 0; i < kafTermArrayList.size(); i++) {
            KafTerm kafTerm = kafTermArrayList.get(i);
            for (int j = 0; j < kafTerm.getSpans().size(); j++) {
                String span = kafTerm.getSpans().get(j);
                if (wid.equals(span)) {
                    if (termToCorefMap.containsKey(kafTerm.getTid())) {
                         ArrayList<String> ids = termToCorefMap.get(kafTerm.getTid());
                        if (ids.size()>1) {
                            System.out.println("ids.toString() = " + ids.toString());
                        }
                        for (int k = 0; k < ids.size(); k++) {
                            id += ids.get(k);
                        }
                    }
                }
            }
        }
        return id;
    }

    /**
     *
     * @param stream
     * @param fileName
     */
     static public void serializeToCoNLL (OutputStream stream,  String fileName) {
        try {
            String str = "#begin document ("+fileName+");";
            stream.write(str.getBytes());
            str  = "";
            boolean COREFERRING = false;
            boolean NEWSENTENCE = false;
            String currentSentence = "";
            String currentReference = "";

            for (int i = 0; i < kafWordFormArrayList.size(); i++) {
                KafWordForm kafWordForm = kafWordFormArrayList.get(i);
                /// insert sentence separator
                if (!currentSentence.isEmpty() && !currentSentence.equals(kafWordForm.getSent()))  {
                    /// we have a different sentence number
                    NEWSENTENCE = true;
                    currentSentence = kafWordForm.getSent();
                }
                else if (currentSentence.isEmpty()) {
                    /// first sentence
                    currentSentence = kafWordForm.getSent();
                }
                else {
                    /// we are in the same sentence
                    NEWSENTENCE = false;
                }
                String corefId = "";
                if (tokenToCorefMap.containsKey(kafWordForm.getWid())) {
                    ArrayList<String> corefIDS = tokenToCorefMap.get(kafWordForm.getWid());
                    for (int j = 0; j < corefIDS.size(); j++) {
                        String s = corefIDS.get(j);
                        corefId+= s;
                    }
                }
                //System.out.println(kafWordForm.getWid()+":" + corefId);
                //// First we need to handle the previous line if any
                //// After that we can process the current
                /// check previous conditions and terminate properly

                if (corefId.isEmpty()) {
                    //// current is not a coreferring token
                    if (COREFERRING) {
                        //// previous was coreferring so we need to terminate the previous with ")"
                        str += ")";
                    }
                    /// always terminate the previous token
                    str += "\n";
                    COREFERRING = false;

                    /// If we started a new sentence so we insert a blank line
                    if (NEWSENTENCE) str+= "\n";
                    /// add the info for the current token
                    String tokenId = kafWordForm.getWid();
                    if (tokenId.startsWith("w")) {
                        tokenId = tokenId.substring(1);
                    }
                    str += fileName+"\t"+kafWordForm.getSent()+"\t"+tokenId+"\t"+kafWordForm.getWf() +"\t"+"-";
                }
                else {
                    if (NEWSENTENCE) {
                        if (COREFERRING) {
                            /// end of sentence implies ending coreference as well
                            str += ")\n";
                        }
                        else {
                            str += "\n";
                        }
                        /// If we started a new sentence so we insert a blank line
                        str+= "\n";
                    }
                    else {
                        /// we are in the same sentence
                        if (COREFERRING && !currentReference.equals(corefId))  {
                            /// we were coreferring but the id is different
                            /// close the previous
                            str += ")\n";
                        }
                        else {
                            /// we can close the last line
                            str += "\n";
                        }
                    }
                    /// add the info for the current token
                    String tokenId = kafWordForm.getWid();
                    if (tokenId.startsWith("w")) {
                        tokenId = tokenId.substring(1);
                    }
                    str += fileName+"\t"+kafWordForm.getSent()+"\t"+tokenId+"\t"+kafWordForm.getWf() +"\t";
                    if (!COREFERRING) {
                        /// we were not coreferring so we start this now
                        str += "("+corefId;
                        COREFERRING = true;
                    }
                    else {
                        if (!currentReference.equals(corefId)) {
                            /// previous id is different so we need to start a new annotation
                            str += "("+corefId;
                        }
                        else {
                            /// we simply continue
                            str += corefId;
                        }
                    }
                    currentReference = corefId;
                }
            }
            ///check the status of the last token
            if (COREFERRING) {
                str += ")\n";
            }
            else {
                str += "\n";
            }
            stream.write(str.getBytes());
            str = "#end document\n";
            stream.write(str.getBytes());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param stream
     * @param fileName
     */
     static public void serializeTermsToCoNLL (OutputStream stream,  String fileName) {
        try {
            String str = "#begin document ("+fileName+");";
            stream.write(str.getBytes());
            str  = "";
            boolean COREFERRING = false;
            boolean NEWSENTENCE = false;
            String currentSentence = "";
            String currentReference = "";

            for (int i = 0; i < kafWordFormArrayList.size(); i++) {
                KafWordForm kafWordForm = kafWordFormArrayList.get(i);
                /// insert sentence separator
                if (!currentSentence.isEmpty() && !currentSentence.equals(kafWordForm.getSent()))  {
                    /// we have a different sentence number
                    NEWSENTENCE = true;
                    currentSentence = kafWordForm.getSent();
                }
                else if (currentSentence.isEmpty()) {
                    /// first sentence
                    currentSentence = kafWordForm.getSent();
                }
                else {
                    /// we are in the same sentence
                    NEWSENTENCE = false;
                }
                String corefId = getCoreferenceSetId(kafWordForm.getWid());
                //System.out.println(kafWordForm.getWid()+":" + corefId);
                //// First we need to handle the previous line if any
                //// After that we can process the current
                /// check previous conditions and terminate properly

                if (corefId.isEmpty()) {
                    //// current is not a coreferring token
                    if (COREFERRING) {
                        //// previous was coreferring so we need to terminate the previous with ")"
                        str += ")";
                    }
                    /// always terminate the previous token
                    str += "\n";
                    COREFERRING = false;
                    /// we started a new sentence so we insert a blank line
                    if (NEWSENTENCE) str+= "\n";
                    /// add the info for the current token
                    String tokenId = kafWordForm.getWid();
                    if (tokenId.startsWith("w")) {
                        tokenId = tokenId.substring(1);
                    }
                    str += fileName+"\t"+kafWordForm.getSent()+"\t"+tokenId+"\t"+kafWordForm.getWf() +"\t"+"-";
                }
                else {
                    if (NEWSENTENCE) {
                        /// we started a new sentence so we insert a blank line
                        if (COREFERRING) {
                            /// end of sentence implies ending coreference as well
                            str += ")\n";
                        }
                        else {
                            str += "\n";
                        }
                        str+= "\n";
                    }
                    else {
                        if (COREFERRING && !currentReference.equals(corefId))  {
                            str += ")\n";
                        }
                        else {
                            str += "\n";
                        }
                    }
                    /// add the info for the current token
                    String tokenId = kafWordForm.getWid();
                    if (tokenId.startsWith("w")) {
                        tokenId = tokenId.substring(1);
                    }
                    str += fileName+"\t"+kafWordForm.getSent()+"\t"+tokenId+"\t"+kafWordForm.getWf() +"\t";
                    if (!COREFERRING) {
                        str += "("+corefId;
                        COREFERRING = true;
                    }
                    else {
                        if (!currentReference.equals(corefId)) {
                            str += "("+corefId;
                        }
                        else {
                            str += corefId;
                        }
                    }
                    currentReference = corefId;
                }
            }
            ///check the status of the last token
            if (COREFERRING) {
                str += ")\n";
            }
            else {
                str += "\n";
            }
            stream.write(str.getBytes());
            str = "#end document\n";
            stream.write(str.getBytes());

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
