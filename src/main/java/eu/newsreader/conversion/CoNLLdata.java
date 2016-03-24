package eu.newsreader.conversion;

/**
 * Created by piek on 02/03/16.
 */
public class CoNLLdata {
    /*
    1_10ecb	0	1	Perennial	-
1_10ecb	0	2	party	-
1_10ecb	0	3	girl	-
1_10ecb	0	4	Tara	-
1_10ecb	0	5	Reid	-
1_10ecb	0	6	checked	(132016236402809085484)
1_10ecb	0	7	herself	-
1_10ecb	0	8	into	(132016236402809085484)

     */

    private String fileName;
    private String sentence;
    private String tokenId;
    private String word;
    private String tag;

    public CoNLLdata() {
        this.fileName = "";
        this.sentence = "";
        this.tag = "";
        this.tokenId = "";
        this.word = "";
    }

    public CoNLLdata(String fileName, String sentence, String tokenId, String word, String tag) {
        this.fileName = fileName;
        this.sentence = sentence;
        this.tag = tag;
        this.tokenId = tokenId;
        this.word = word;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
