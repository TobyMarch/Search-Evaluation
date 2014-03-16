/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lab5evaluation;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.regex.*;
import java.net.URL;
import java.io.InputStreamReader;
import org.jsoup.nodes.Element;

/**
 *
 * @author tmarch1
 */
public class Crawler {

    SearchDB base = new SearchDB();
    
    public static int termCount(List<String> listIn){
        int numTerms = 0;

        for(String st:listIn){
            numTerms++;
        }
        return numTerms;
    }
    /* Title: processWebPage
     * Purpose: Read the content of a page and counts the number of tokens (Lab 1 specific)
     * Parameters:
     *      String fileIn: Name of file or site to be processed
     * Return Value: none
     */
    public static void processWebpage(String fileIn) throws IOException{
        //Scanner scanner = new Scanner(System.in);
        System.out.println("Reading WebPage...");

        Spider testSpider = new Spider("/Users/tmarch1/Development/NetBeansProjects/490/490 Lab4-Ranked_Retrieval/");
        //List<String> tokens = testSpider.parser(fileIn);
        List<String> tokens = testSpider.getParsedText(fileIn);
        System.out.println("Number of Tokens: " + termCount(tokens));
        System.out.println("Number of Terms: "+ termCount(testSpider.getTerms(tokens)));
        tokens = testSpider.lower(tokens);
        System.out.println("Number of Terms after lowercase: " + termCount(testSpider.getTerms(tokens)));
        tokens = testSpider.stem(tokens);
        System.out.println("Number of Terms after Porter Stemmer: " + termCount(testSpider.getTerms(tokens)));
    }
    
    /*  Title: processResults
     *  Purpose: scans the given results page and returns the urls of the top ten results
     *  Parameters
     *      String urlIn - the web address of the search query
     *  Return Value:
     */
    public void processResults(String urlIn,String itemType, SearchDB base, String name, int item){

        int pageNum = 1;
        int numResults = 0;
        int resultReq = 10;// How the hell does Java not support constants?!
        String[] resultArr = new String[resultReq];
        Spider testSpider = new Spider("/Users/tmarch1/Development/NetBeansProjects/490/490 Lab5-Evaluation/");
        base.insertItem(name, itemType);
        try {
            //String nextPage = doc.select("div.pgc a").first().attr("href");
            String url = urlIn;
            while(numResults < resultReq){
                Document page = Jsoup.connect(url).get();
                for(Element link : page.select("a")){
                    if(link.attr("id").startsWith("r")){
                        if(numResults < resultReq){
                            //System.out.println(" - " + link.attr("href"));
                            //testSpider.fetch(link.attr("href"));
                            resultArr[numResults] = link.attr("href");
                            numResults++;
                            
                        }
                        else
                            break;
                    }
                }
                pageNum++;
                url = url.concat("&page=" + pageNum);
            }
            for(int i = 0;i < resultReq; i++){
                System.out.println(" - " + resultArr[i]);
                testSpider.fetch(resultArr[i],itemType, base, item);
            }
        } catch (IOException ex) {
            Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*  
     *  Title: getQueries
     *  Purpose: reads a list document and assembles urls with search queries
     *  Parameters:
     *      String fileIn - the name of the file containing the list
     *  Return Values: none
     */
    public void getQueries(String fileIn, int item) throws FileNotFoundException, IOException{
        Scanner fileScan = new Scanner(new BufferedReader(new FileReader(fileIn)));
        
        String itemType = fileIn;
        System.out.println("Reading File...");

        //Uses the file name to get the type of items in the list
        itemType = itemType.substring(0, itemType.length()-4);
        
        while(fileScan.hasNextLine()){
            String name = fileScan.nextLine();
            String query = "\"" + name + "\" " + itemType;
            String queryURL = "http://www.ask.com/web?q=";
            //System.out.println(query);
            query = query.replaceAll("\\s", "+");
            queryURL = queryURL.concat(query);
            //System.out.println(queryURL);
            processResults(queryURL, itemType, base, name, item);
            item++;
        }
        fileScan.close();
    }
}
