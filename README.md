coreference-evaluation
======================
Copyright (c) VU University Amsterdam, Piek Vossen, email: piek.vossen@vu.nl

Evaluation package for event coreference using the reference-scorer

Event coreference evaluation module

Package of functions for evaluating event coreference:
- conversion of NewsReader CAT annotations to CoNLL2011/2012 format for coreference
- conversion of NewsReader NAF coreference to CoNLL2011/2012 format for coreference
- function to reduce the key file to sentences with annotations
- function to reduce the response file to the sentences of the key file
- function to generate the scripts to compare key files with response files in CoNLL coreference format using CorScorer.
   Emili Sapena, Universitat Politècnica de Catalunya, http://www.lsi.upc.edu/~esapena, esapena <at> lsi.upc.edu
   Sameer Pradhan, sameer.pradhan <at> childrens.harvard.edu
   Sebastian Martschat, sebastian.martschat <at> h-its.org
   Xiaoqiang Luo, xql <at> google.com

To carry out the coreference evaluation in NewsReader you need to have news articles annotated for coreference using the CAT tool
and the same articles processed through the NewsReader pipeline. Evaluation is done as follows

1. Conversion of NewsReader CAT annotations to CoNLL2011/2012 format for coreference

Function
   eu.newsreader.conversion.CatCoref
Example
   java -cp ../lib/coreference-evaluation-1.0-SNAPSHOT-jar-with-dependencies.jar eu.newsreader.conversion.CatCoref --coref-type EVENT --file-extension .xml --folder ../../corpus_CAT_GS_201412/corpus_stock_market --format conll


2. Conversion of NewsReader NAF coreference to CoNLL2011/2012 format for coreference

Function
   eu.newsreader.conversion.NafCoref
Example
   java -cp ../lib/coreference-evaluation-1.0-SNAPSHOT-jar-with-dependencies.jar eu.newsreader.conversion.NafCoref --coref-type EVENT --file-extension .naf --folder ../../corpus_NAF_output_141214/corpus_stock_market --format conll

3. Reduce the key file to sentences with annotations

Function
   eu.newsreader.conversion.ReduceConllKey
Example
   java -cp ../lib/coreference-evaluation-1.0-SNAPSHOT-jar-with-dependencies.jar eu.newsreader.conversion.ReduceConllKey --key /Users/piek/Desktop/NWR/NWR-benchmark/coreference/corpus_CONLL/corpus_apple/entities/key

4. Reduce the response file to the sentences of the key file

Function
   eu.newsreader.conversion.ReduceConllResponse
Example
   java -cp ../lib/coreference-evaluation-1.0-SNAPSHOT-jar-with-dependencies.jar eu.newsreader.conversion.ReduceConllResponse --key ../../corpus_CONLL/corpus_apple/entities/key --response ../../corpus_CONLL/corpus_apple/entities/response

5. Make the scripts to compare key files with response files in CoNLL coreference format using CorScorer.

Function
   eu.newsreader.script.MakeScoreScript
Example
   java -cp ../lib/coreference-evaluation-1.0-SNAPSHOT-jar-with-dependencies.jar eu.newsreader.script.MakeScoreScript --key ../../corpus_CONLL/corpus_apple/entities/key --response ../../corpus_CONLL/corpus_apple/entities/response --corpus corpus_apple_entities --method blanc

6. Run the scripts that call the CorScorer for pairs of files

Function
   perl ./v8.01/scorer.pl blanc
Example
   perl ./v8.01/scorer.pl blanc ../../corpus_CONLL/corpus_airbus/events/key/100911_Northrop_Grumman_and_Airbus_parent_EADS_defeat_Boeing.txt.xml.EVENT.key ../../corpus_CONLL/corpus_airbus/events/response/100911_Northrop_Grumman_and_Airbus_parent_EADS_defeat_Boeing.naf.EVENT.response > ../../corpus_CONLL/corpus_airbus/events/response/100911_Northrop_Grumman_and_Airbus_parent_EADS_defeat_Boeing.naf.EVENT.response.blanc.result

7. Collect the results of each file in an overview spreadsheet with micro/macro averages

Function
   eu.newsreader.result.CollectResults
Example
   java -cp ../lib/coreference-evaluation-1.0-SNAPSHOT-jar-with-dependencies.jar eu.newsreader.result.CollectResults --result-folder ../../corpus_CONLL/corpus_apple/events/response --extension .result

