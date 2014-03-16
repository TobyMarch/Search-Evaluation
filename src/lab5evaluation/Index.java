package lab5evaluation;

import java.util.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.Math;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author tmarch1
 */
public class Index {
    HashMap<String,HashMap> termDict = new HashMap<String,HashMap>();
    HashMap<String,Double> queryDict = new HashMap<String,Double>();
    SearchDB myDatabase = new SearchDB();
    int nearReq = 5;// Seriously, though: no constants?
    
    
    public Index(){
        //Harcoded test to see if method works
        //myDatabase.findURL_byID(18);
        
        File directory = new File("/Users/tmarch1/Development/NetBeansProjects/490/490 Lab5-Evaluation/data/clean/");
        String files;

        File[] listOfFiles = directory.listFiles();

        for (int i = 0; i < listOfFiles.length; i++){

            if(listOfFiles[i].isFile()){
                files = listOfFiles[i].getName();
                if (files.endsWith(".txt") || files.endsWith(".TXT")){
                    this.Read(files);
                }
            }
        }
        
    }

    public void Read(String fileIn){

        try {
            String filePath = "/Users/tmarch1/Development/NetBeansProjects/490/490 Lab5-Evaluation/data/clean/" + fileIn;
            Scanner fileScanner = new Scanner(new BufferedReader(new FileReader(filePath)));
            String docName = fileIn;
            int filePosition = 0;

            docName = docName.substring(0, docName.length() - 4); //removes file type
            //System.out.println("Inserting File to Index...");
            while (fileScanner.hasNext()) {
                //System.out.println("Inserting: " + docName);
                this.Insert(fileScanner.next(), docName, filePosition);
                filePosition++;
            }
            fileScanner.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void Insert(String term,String docName, int termPosition){

        //System.out.println(term + " " + docName + " " + termPosition);
        //LinkedList<Integer> newList = (LinkedList<Integer>)this.termDict.get(term).get(docName);
        //If the term isn't found in the top HashMap
        if(!this.termDict.containsKey(term)){
            LinkedList<Integer> posList = new LinkedList<Integer>();
            posList.add(termPosition);
            Tuple dataPair = new Tuple(0.0,posList);

            /*HashMap<String,LinkedList<Integer>> docDict = new HashMap<String,LinkedList<Integer>>();
            docDict.put(docName,posList);
            termDict.put(term,docDict);*/
            HashMap<String,Tuple> docDict = new HashMap<String,Tuple>();
            docDict.put(docName,dataPair);
            termDict.put(term,docDict);

        }

        //If the term is found, but the docName isn't
        else if((this.termDict.containsKey(term)) && (!this.termDict.get(term).containsKey(docName))){
            LinkedList<Integer> posList = new LinkedList<Integer>();
            posList.add(termPosition);
            //HashMap<String,LinkedList<Integer>> docDict = new HashMap<String,LinkedList<Integer>>();
            //termDict.get(term).put(docName,docDict);
            /*termDict.get(term).put(docName,posList);*/
            Tuple dataPair = new Tuple(0.0,posList);
            termDict.get(term).put(docName,dataPair);
        }

        //If the term and docName are found, but termPosition isn't
        else{
            /*if((this.termDict.containsKey(term)) && (this.termDict.get(term).containsKey(docName)) &&
                (!this.termDict.get(term).get(docName).contains(termPosition))){*/

            HashMap termMap= termDict.get(term);
            /*LinkedList posList = (LinkedList<Integer>)termMap.get(docName);
            posList.add(termPosition);*/
            ((Tuple)termMap.get(docName)).addPos(termPosition);
        }

    }

    //Boolean Search Functions(NOT UPDATED FOR RR)
    public void singleQuery(String termIn){
        int numResults = 0;
        if(termDict.containsKey(termIn)){
            //((HashMap)termDict.get(termIn)).keySet();
            Set<Map.Entry<String,LinkedList>> entries = termDict.get(termIn).entrySet();
            for(Map.Entry<String,LinkedList> entry : entries){
                String documentName = entry.getKey();
                //System.out.println(documentName);
                //documentName = documentName.substring(0, documentName.length());
                int documentInt = Integer.parseInt(documentName);
                System.out.print(++numResults+"): ");
                myDatabase.findURL_byID(documentInt);
                System.out.println("");
            }
        }
        else{
            System.out.println("Term Not Found");
        }
    }
    public void andQuery(String termA,String termB){
        int numResults = 0;
        if((termDict.containsKey(termA) && (termDict.containsKey(termB)))){
            Set<String> setA = termDict.get(termA).keySet();
            Set<String> setB = termDict.get(termB).keySet();

            //Create an Intersection set (all common entries of setA and setB)
            setA.retainAll(setB);
            
            //Check to see that there actually is anything in the Intersection set
            if(!setA.isEmpty()){
                for(String intersectEntry : setA){
                    String documentName = intersectEntry;
                    int documentInt = Integer.parseInt(documentName);
                    System.out.print(++numResults+"): ");
                    myDatabase.findURL_byID(documentInt);
                    System.out.println("");
                }
            }
            else{
                System.out.println("Intersecting Documents Not Found");
            }
        }
        else{
            System.out.println("One or Both Terms Not Found");
        }

    }
    public void orQuery(String termA,String termB){
        int numResults = 0;
        if(termDict.containsKey(termA) && termDict.containsKey(termB)){
            Set<String> setA = termDict.get(termA).keySet();
            Set<String> setB = termDict.get(termB).keySet();

            //Create a Union set (all entries of setA and setB)
            HashSet<String> unionSet = new HashSet(setA);
            for(String bDoc: setB){
                unionSet.add(bDoc);
            }
            //Check to make sure Union set isn't empty (unlikely, but you never know)
            if(!unionSet.isEmpty()){
                for(String unionEntry : unionSet){
                    String documentName = unionEntry;
                    int documentInt = Integer.parseInt(documentName);
                    System.out.print(++numResults + "): ");
                    myDatabase.findURL_byID(documentInt);
                    System.out.println("");
                }
            }
        }
        else if(termDict.containsKey(termA) && !termDict.containsKey(termB)){
            this.singleQuery(termA);
        }
        else if(!termDict.containsKey(termA) && termDict.containsKey(termB)){
            this.singleQuery(termB);
        }
        else{
            System.out.println("Neither Term Found");
        }
    }
    public void phraseQuery(String termA,String termB){
        int numResults = 0;
        if((termDict.containsKey(termA) && (termDict.containsKey(termB)))){
            Set<String> setA = termDict.get(termA).keySet();
            Set<String> setB = termDict.get(termB).keySet();
            
            //Create an Intersection set (all common entries of setA and setB)
            setA.retainAll(setB);

            //Check to see that there actually is anything in the Intersection set
            if(!setA.isEmpty()){
                for(String intersectEntry : setA){
                    //Store the name of the Document for later calling
                    String documentName = intersectEntry;
                    
                    //Access the posLists of the intersecting docDict for each term
                    LinkedList listA = (LinkedList<Integer>)termDict.get(termA).get(documentName);
                    LinkedList listB = (LinkedList<Integer>)termDict.get(termB).get(documentName);
                    //Walk through each LinkedList manually
                    int indexA = 0;
                    int indexB = 0;
                    boolean found = false;

                    //ListIterator iterateA = ((LinkedList<Integer>)termDict.get(termA).get(documentName)).listIterator();
                    //ListIterator iterateB = ((LinkedList<Integer>)termDict.get(termB).get(documentName)).listIterator();

                    //Access the LinkedList within each entry of the set
                    while(indexA < listA.size() && indexB < listB.size()){
                        //If the first term is immediately followed by the second (MATCH)
                        if((Integer)listA.get(indexA) == (Integer)listB.get(indexB) - 1){
                            //This line will eventually be replaced by a call to the database.
                            //System.out.println(documentName);
                            //documentName = documentName.substring(0, documentName.length()-4);
                            int documentInt = Integer.parseInt(documentName);
                            System.out.print(++numResults + "): ");
                            myDatabase.findURL_byID(documentInt);
                            System.out.println("");
                            found = true;
                            indexA++;
                            break;//we only need one match, so break the loop
                        }
                        //If the current position of B is less than current position of A
                        else if((Integer)listB.get(indexB) <= (Integer)listA.get(indexA)){
                            indexB++;
                        }
                        //If current position of A is less than Current B, but not close enough to be a match
                        else{
                            indexA++;
                        }
                    }
                    if(!found)
                        System.out.println("No Phrase Matches Found");
                }
            }
            else{
                System.out.println("Intersecting Documents Not Found");
            }
        }
        else{
            System.out.println("One or Both Terms Not Found");
        }
    }
    public void nearQuery(String termA,String termB){
        int numResults = 0;
        if((termDict.containsKey(termA) && (termDict.containsKey(termB)))){
            Set<String> setA = termDict.get(termA).keySet();
            Set<String> setB = termDict.get(termB).keySet();

            //Create an Intersection set (all common entries of setA and setB)
            setA.retainAll(setB);

            //Check to see that there actually is anything in the Intersection set
            if(!setA.isEmpty()){
                for(String intersectEntry : setA){

                    String documentName = intersectEntry;
                    //Access the posLists of the intersecting docDict for each term
                    LinkedList listA = (LinkedList<Integer>)termDict.get(termA).get(documentName);
                    LinkedList listB = (LinkedList<Integer>)termDict.get(termB).get(documentName);
                    //Walk through each LinkedList manually
                    int indexA = 0;
                    int indexB = 0;
                    boolean found =false;

                    while(indexA < listA.size() && indexB < listB.size()){
                        //If the two terms are found in proximity to each other (MATCH)
                        if(Math.abs((Integer)listA.get(indexA)-(Integer)listB.get(indexB)) < nearReq){
                            //This line will eventually be replaced by a call to the database.
                            //System.out.println(documentName);
                            
                            //documentName = documentName.substring(0, documentName.length()-4);
                            int documentInt = Integer.parseInt(documentName);
                            System.out.print(++numResults + "): ");
                            myDatabase.findURL_byID(documentInt);
                            System.out.println("");
                            found = true;
                            if((Integer)listA.get(indexA) > (Integer)listB.get(indexB)){
                                indexB++;
                            }
                            else{
                                indexA++;
                            }
                            break;
                        }
                        else if((Integer)listB.get(indexB) <= (Integer)listA.get(indexA) - 6){
                            indexB++;
                        }
                        else{
                            indexA++;
                        }
                    }
                    if(!found){
                        //System.out.println("No Near Results Found");
                    }
                }
            }
            else{
                System.out.println("Intersecting Documents Not Found");
            }
        }
        else{
            System.out.println("Term Combination Not Found");
        }
    }

    //Pass Functions
    public void passOne(char weightType){
        //Accesses the Weight of each Term within each Document
        for(String term : termDict.keySet()){
            for(String docName : (Set<String>)termDict.get(term).keySet()){
                double oldVal = ((Tuple)termDict.get(term).get(docName)).posList.size();
                double newVal = 0.0;
                if(weightType == 'l')
                    newVal = 1 + Math.log10(oldVal);
                else
                    newVal = oldVal;
                ((Tuple)termDict.get(term).get(docName)).setWeight(newVal);
            }
        }
    }
    public void passTwo(char weightType){
        if(weightType == 't'){
            for(String term : termDict.keySet()){
                for(String docName : ((Set<String>)termDict.get(term).keySet())){
                    int DF = termDict.get(term).size();
                    int N = Integer.parseInt(myDatabase.numOfDocs());
                    double IDF = Math.log10(N/DF);
                    double oldVal = ((Tuple)termDict.get(term).get(docName)).getWeight();

                    ((Tuple)termDict.get(term).get(docName)).setWeight(oldVal * IDF);
                }
            }
        }
    }
    public void passThree(char weightType){
        int corpusSize = Integer.parseInt(myDatabase.numOfDocs());
        double[] normalizer = new double[corpusSize + 1];
        if(weightType == 'c'){
            //Part 1: sum of TFIDF^2
            for(String term : termDict.keySet()){
                for(String docName : ((Set<String>)termDict.get(term).keySet())){
                    //System.out.println(docName);
                    if(!docName.isEmpty()){//Temp fix, to counteract null strings in KeySet
                        int docNum = Integer.parseInt(docName);
                        double oldVal = ((Tuple)termDict.get(term).get(docName)).getWeight();
                        normalizer[docNum] += Math.pow(oldVal, 2);
                    }
                }
            }
            //Part 2: sqrt of sum of TFIDF^2
            for(int i = 0;i < corpusSize;i++){
                double oldVal = normalizer[i];
                normalizer[i] = Math.sqrt(oldVal);
            }
            //part 3: Normalize TFIDF
            for(String term : termDict.keySet()){
                for(String docName : ((Set<String>)termDict.get(term).keySet())){
                    if(!docName.isEmpty()){
                        int docNum = Integer.parseInt(docName);
                        double oldVal = ((Tuple)termDict.get(term).get(docName)).getWeight();
                        double newVal = oldVal/normalizer[docNum];
                        ((Tuple)termDict.get(term).get(docName)).setWeight(newVal);
                    }
                }
            }
        }
    }

    public void rankDocs(String docScheme){
        this.passOne(docScheme.charAt(0));
        this.passTwo(docScheme.charAt(1));
        this.passThree(docScheme.charAt(2));
    }

    public void rankQuery(String query, String queryScheme){
        //Create
        queryDict = new HashMap<String,Double>();
        String[] queryTerms = query.split(" ");
        //Insert terms into query HashMap, incrementing frequency as necessary
        for(String term : queryTerms){
            if(queryDict.containsKey(term)){
                double oldVal = queryDict.get(term);
                queryDict.put(term, oldVal++);
            }
            else{
                queryDict.put(term, 1.0);
            }
        }
        /*for(String queryTerm : queryDict.keySet()){
            System.out.println(queryTerm);
        }*/
        
        //Weight the terms within the Query
        //Pass One:
        if(queryScheme.charAt(0) == 'l'){
            for(String term : queryDict.keySet()){
                double oldVal = 1 + Math.log10(queryDict.get(term));
                queryDict.put(term, oldVal);
            }
        }

        //Pass Two
        if(queryScheme.charAt(1) == 't'){
            for(String term : queryDict.keySet()){
                if(termDict.containsKey(term)){
                    //IDF = number of documents that contain the term
                    int DF = termDict.get(term).size();
                    int N = Integer.parseInt(myDatabase.numOfDocs());
                    double IDF = Math.log10(N/DF);
                    double TFIDF = queryDict.get(term) * IDF;
                    queryDict.put(term, TFIDF);
                }
            }
        }

        //Pass Three
        if(queryScheme.charAt(2) == 'c'){
            //Calculate normalizing value
            double normalizer = 0.0;
            for(String term : queryDict.keySet()){
                normalizer += Math.pow(queryDict.get(term),2);
            }
            normalizer = Math.sqrt(normalizer);
            //Normalize TFIDF
            for(String term : queryDict.keySet()){
                double oldVal = queryDict.get(term);
                queryDict.put(term, (oldVal/normalizer));
            }
        }
        //System.out.println("Query Weighting Complete!");
    }

    public void compare(){
        System.out.println("\nBeginning Comparison");
        HashMap cloneMap = (HashMap)termDict.clone();
        int arrSize = Integer.parseInt(myDatabase.numOfDocs())+1;
        //HashMap and array to track highest search item score
        HashMap<String,Double> itemRank = new HashMap<String,Double>();
        double[] itemArr = new double[39];

        //double arrays automatically initialized to 0.0
        double[] unsortedArr = new double[arrSize];
        double[] sortedArr = new double[arrSize];

        //Create intersection set of query terms and doc terms
        //System.out.println("Size of cloneMap: " + cloneMap.size());
        Set<String> setA = cloneMap.keySet();
        Set<String> setB = queryDict.keySet();


        //Create an set of intersecting terms in the query and the index
        setA.retainAll(setB);
        
        if(!setA.isEmpty()){
            //ForEach loop to compare all documents containing the intersect terms
            for(String intersectEntry : setA){
                String rankTerm = intersectEntry;
                //System.out.print("Checking term: '"+ rankTerm);
                //Fill the bucketArray with termWeights from all relevant documents
                for(String docName : (Set<String>)termDict.get(rankTerm).keySet()){
                    if(!docName.isEmpty()){
                        //System.out.print("' in document: "+ docName);
                        double termInDocWeight = ((Tuple)termDict.get(rankTerm).get(docName)).getWeight();
                        int documentInt = Integer.parseInt(docName);
                        double compScore = queryDict.get(rankTerm) * termInDocWeight;
                        String itemName = myDatabase.getSearchTerm_ByDocID(docName);

                        //For the dot-product of each document by multiply corresponding weights, then adding
                        //System.out.println(" from search term: "+itemName+" - Score equals: "+compScore);
                        sortedArr[documentInt] += compScore;
                        unsortedArr[documentInt] += compScore;
                        //Updates the HashMap of search term with new scores
                        //System.out.print("Updating HashMap ");
                        if(!itemRank.containsKey(itemName)){
                            itemRank.put(itemName, compScore);
                            //System.out.println(itemRank.get(itemName));
                        }
                        else{
                            double oldVal = itemRank.get(itemName);
                            double newVal = oldVal + compScore;
                            itemRank.remove(itemName);
                            itemRank.put(itemName, newVal);
                            //System.out.print("Should update to:"+ newVal);
                            //System.out.println(", but stays at " + itemRank.get(itemName));
                        }
                        //System.out.println("Updating HashMap: "+itemName+" "+itemRank.get(itemName));
                    }
                }
            }
            System.out.println("Sorting results...");
            System.out.println("\nTop ITEM Results");
            //Sort item scores, and find top 3
            int itemPos = 0;
            for(String itemName : itemRank.keySet()){
                //System.out.println("ITEM: " + itemName + "VALUE: " + itemRank.get(itemName));
                itemArr[itemPos] = itemRank.get(itemName);
                itemPos++;
            }
            Arrays.sort(itemArr);
            for(int i = itemArr.length-1; i > 35; i--){
                //System.out.println(itemArr[i]);
                for(String itemName : itemRank.keySet()){
                    if(itemRank.get(itemName) == itemArr[i]){
                        System.out.println(itemName + " " + itemRank.get(itemName));
                    }
                }
            }
            //Sort one array and compare to find the top 3 DOCUMENT scores
            System.out.println("\nTop DOCUMENT Results:");
            Arrays.sort(sortedArr);
            for(int i = arrSize - 1;i > arrSize - 4;i--){
                int originDoc = 0;
                while(unsortedArr[originDoc] != sortedArr[i]){originDoc++;}
                System.out.print(arrSize - i +")");
                myDatabase.findURL_byID(originDoc);
            }   
        }
        else
            System.out.println("Pages Matching Query Not Found!");
    }
    /* Method: comparisonArray
     * Purpose: returns the unsorted of document comparisons
     */
    public double[] comparisonArray(){
        //System.out.println("\nBeginning Comparison");
        HashMap cloneMap = (HashMap)termDict.clone();
        int arrSize = Integer.parseInt(myDatabase.numOfDocs())+1;

        //double array automatically initialized to 0.0
        double[] unsortedArr = new double[arrSize];

        //Create intersection set of query terms and doc terms
        Set<String> setA = cloneMap.keySet();
        Set<String> setB = queryDict.keySet();

        //Create an set of intersecting terms in the query and the index
        setA.retainAll(setB);

        if(!setA.isEmpty()){
            //ForEach loop to compare all documents containing the intersect terms
            for(String intersectEntry : setA){
                String rankTerm = intersectEntry;
                //Fill the bucketArray with termWeights from all relevant documents
                for(String docName : (Set<String>)termDict.get(rankTerm).keySet()){
                    if(!docName.isEmpty()){
                        double termInDocWeight = ((Tuple)termDict.get(rankTerm).get(docName)).getWeight();
                        int documentInt = Integer.parseInt(docName);
                        double compScore = queryDict.get(rankTerm) * termInDocWeight;
                        String itemName = myDatabase.getSearchTerm_ByDocID(docName);

                        //For the dot-product of each document by multiply corresponding weights, then adding
                        unsortedArr[documentInt] += compScore;

                    }
                }
            }
        }
        else
            System.out.println("Pages Matching Query Not Found!");
        return unsortedArr;

    }

}