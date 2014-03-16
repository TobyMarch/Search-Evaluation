/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lab5evaluation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author tmarch1
 */
public class Evaluator {
    Index myIndex = new Index();
    SearchDB myDatabase = new SearchDB();
    QuickSort sorter = new QuickSort();
    HashMap<String,List<String>> relevanceMap = new HashMap<String,List<String>>();
    int arrSize = Integer.parseInt(myDatabase.numOfDocs()) + 1;

    public String tokenize(String searchTerm){
        searchTerm = searchTerm.replaceAll(",", "");
        searchTerm = searchTerm.replaceAll(":", "");
        searchTerm = searchTerm.replaceAll("\\.", "");
        searchTerm = searchTerm.replaceAll(";", "");
        searchTerm = searchTerm.replaceAll("-", "");
        searchTerm = searchTerm.replaceAll("&amp", "");
        searchTerm = searchTerm.replaceAll("&quot", "");
        searchTerm = searchTerm.toLowerCase();

        return searchTerm;
    }
    
    public Boolean[] getRelevance(String searchTerm, double[] unsortedArr){
        int[] indexArr = new int[arrSize];
        Boolean[] relevanceArr = new Boolean[arrSize];
        for(int i = 0;i<relevanceArr.length;i++){
            relevanceArr[i] = false;
        }
        for(int i = 0; i < indexArr.length;i++){
            indexArr[i] = i;
        }

        //System.out.println("Sorting...");
        sorter.quicksort(unsortedArr, indexArr);
        //System.out.println("Top Scores:");
        for(int i = indexArr.length-1;i > indexArr.length-11;i--){
            //System.out.println("indexArr["+i+"]: Document " + indexArr[i] + ": " + unsortedArr[i]);
        }
        //System.out.println();

        //System.out.println("Relevant documents: ");
        for(int i = indexArr.length-1;i > 0;i--){
            if(relevanceMap.get(searchTerm).contains(Integer.toString(indexArr[i]))){
                relevanceArr[i] = true;
                //System.out.println("indexArr[" + i + "]: Document " +indexArr[i]+": " + unsortedArr[i]);
            }
        }
        //System.out.println();

        //Reverse the order of relevanceArr
        Collections.reverse(Arrays.asList(relevanceArr));
        
        


        return relevanceArr;
    }


    public double AveragePrecision(Boolean[] ranking) {
        double doc = 1.0;
        double total = 0.0;
        double position = 0.0;

        for (int i = 0; i < ranking.length; i++){
            position++;
            //System.out.println(position);
            //System.out.println(i+": "+ranking[i]);

            if (Boolean.valueOf(ranking[i]) == true){
                double AvgPre = (doc/position);
                //System.out.println(AvgPre);
                total = total + AvgPre;
                doc++;
                //System.out.println(i+": "+ranking[i]);
            }
        }
        double totalAP = total/doc;
        return totalAP;
    }
    public double R_Precision(Boolean[] ranking){

        //int relDocs = ranking.length;
        double relDocsNum = 0;
        double relPrecision = 0;

        for (int i=0; i < ranking.length; i++){
            if (Boolean.valueOf(ranking[i]) == true){
                relDocsNum++;
            }
        }

        for (int i=0; i < relDocsNum; i++){
            if (Boolean.valueOf(ranking[i]) == true){
                relPrecision++;
            }
        }
        double precision = (relPrecision/relDocsNum);
        return precision;
    }
    public double K_Precision(Boolean[] ranking, double x){
        double relDocs = 0;

        for (int i=0; i < x; i ++){
            if (Boolean.valueOf(ranking[i]) == true){
                relDocs++;
            }
        }
        double precision = relDocs/x;
        return precision;
    }
    public double aucPrecision(Boolean[] ranking){

        double docs = ranking.length;
        double tpCount = 0;
        double fpCount = 0;

        for (int i = 0; i < docs; i++){
            if (Boolean.valueOf(ranking[i]) == true){
                tpCount++;
            }
            else if (Boolean.valueOf(ranking[i]) == false){
                fpCount++;
            }
        }

        double fp = 1/fpCount;
        double tp = 0.0;
        double tpRate = 0.0;
        double total = 0.0;
        double finalAUC = 0.0;

        for (int i = 0; i < docs; i++){
            if (Boolean.valueOf(ranking[i]) == true){
                tp++;
            }
            else if(Boolean.valueOf(ranking[i]) == false){
                tpRate = (tp/tpCount);
                total = total + tpRate;
            }
        }
        finalAUC = fp*total;
        //System.out.println("fp " + tpCount);
        return finalAUC;
    }

    public void run(){
        //Create HashMap of search queries and relevant results
        List<String> termList = myDatabase.getSearchTerms();
        Collections.shuffle(termList);
        String[] termArr = {"A Tale of Two Cities","The Avengers","Lolita","The Beatles","Finding Nemo"};
        String[] docSchemes = {"nnn","ltc"};
        String[] querySchemes = {"nnn","ltc"};
        double[] unsortedArr = new double[arrSize];

        //Create Data structure to hold search terms and numbers of relevant documents
        for(String searchTerm : termList){
            List<String> pages = new ArrayList<String>();
            pages = myDatabase.getSearchResults(searchTerm);
            relevanceMap.put(searchTerm, pages);
        }

        System.out.println("P@10    P@R     MAP     AUC");
        for(int random = 0; random < 2;random++){
            int endVal = 2;
            if (random > 0)
                endVal = 1;
            else
                endVal = 2;
            for(int i = 0;i < endVal;i++){
                String dScheme = docSchemes[i];
                myIndex.rankDocs(dScheme);
                for(int j = 0;j < endVal;j++){
                    String qScheme = querySchemes[j];
                    double KPrecisionAvg = 0.0;
                    double RPrecisionAvg = 0.0;
                    double MAPAvg = 0.0;
                    double AUCAvg = 0.0;

                    if(random > 0)
                        System.out.println("Random Performance:");
                    else
                        System.out.println("Evaluating: " + dScheme + "." + qScheme);
                    for(String searchTerm : termList){
                        //System.out.println("Term: " + searchTerm);

                        //Create a separate version of the title for ranked searching
                        String searchToken = this.tokenize(searchTerm);
                        myIndex.rankQuery(searchToken, qScheme);

                        unsortedArr = myIndex.comparisonArray();
                        Boolean[] relevanceArr = this.getRelevance(searchTerm, unsortedArr);

                        if(random > 0)
                            Collections.shuffle(Arrays.asList(relevanceArr));

                        KPrecisionAvg += this.K_Precision(relevanceArr, 10.0)/(double)termList.size();
                        RPrecisionAvg += this.R_Precision(relevanceArr)/(double)termList.size();
                        MAPAvg += this.AveragePrecision(relevanceArr)/(double)termList.size();
                        AUCAvg += this.aucPrecision(relevanceArr)/(double)termList.size();
                        //System.out.println(termList.size());

                    }
                    double kpRound = (double)Math.round(KPrecisionAvg * 1000)/1000;
                    double rpRound = (double)Math.round(RPrecisionAvg * 1000)/1000;
                    double mapRound = (double)Math.round(MAPAvg * 1000)/1000;
                    double aucRound = (double)Math.round(AUCAvg * 1000)/1000;
                    System.out.println(kpRound + "   " + rpRound + "   " + mapRound + "   " + aucRound);
                }
            }
        }   
    }


}
