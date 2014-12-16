coreference-evaluation
======================

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
   CatCoref
Example
   java -cp ../lib/coreference-evaluation-1.0-SNAPSHOT-jar-with-dependencies.jar eu.newsreader.conversion.CatCoref --coref-type EVENT --file-extension .xml --folder ../../corpus_CAT_GS_201412/corpus_stock_market --format conll


2. Conversion of NewsReader NAF coreference to CoNLL2011/2012 format for coreference


3. Reduce the key file to sentences with annotations
4. Reduce the response file to the sentences of the key file
5. Make the scripts to compare key files with response files in CoNLL coreference format using CorScorer.
6. Run the scripts that call the CorScorer for pairs of files
7. Collect the results of each file in an overview spreadsheet with micro/macro averages


Function
   ReduceConllResponse
Example
   java -cp ../lib/coreference-evaluation-1.0-SNAPSHOT-jar-with-dependencies.jar eu.newsreader.conversion.ReduceConllResponse --key ../../corpus_CONLL/corpus_apple/entities/key --response ../../corpus_CONLL/corpus_apple/entities/response