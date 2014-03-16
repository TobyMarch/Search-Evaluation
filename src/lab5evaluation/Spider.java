package lab5evaluation;

//import java.lang.String;
import java.io.BufferedReader;
import java.io.BufferedWriter;
//import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import java.io.File;
import java.net.*;
import java.io.*;


/**
 *
 * @author tmarch1
 */
public class Spider {

    /*  Name: Spider()
     *  Purpose: constructor- sets up directory structure
     *  Parameters:
     *      String homeDirectory - typically /Users/tmarch1/NetBeansProjects/490/490 Lab5-Evaluation
     *  Return Value: none
     */
    Spider(String hdIn){
        String dataDirectory = "/data";
        String[] subDirectories = {"/item","/header","/raw","/clean"};

        try{
            String homeDirectory = hdIn;
            for(int i = 0; i<4;i++){
            File data = new File(homeDirectory + dataDirectory + subDirectories[i]);
            data.mkdirs();
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

    }

    /*  Name: getParsedText
     *  Purpose: gets the html-cleaned content from a webpage
     *  Parameters:
     *      String pageIn: url of page to be read
     *  Return Values:
     *      List listOut: List of words taken from raw HTML file
     */
   public List getParsedText(String pageIn){
       String pageTitle = "";
       String parsedDoc = "";
       String ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.122 Safari/534.30";
       List<String> listOut = new ArrayList<String>();
       try {

            Document htmlDoc = Jsoup.connect(pageIn).userAgent(ua).get();
            //Extract the title
            pageTitle = htmlDoc.title();
            System.out.println("Page Title: " + pageTitle);
            //Strip out the HTML
            parsedDoc = Jsoup.clean(htmlDoc.text(), Whitelist.relaxed());
            //Writing out the tokens
            FileWriter fstream = new FileWriter("out.txt");
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(parsedDoc);
            out.close();

        } catch (IOException ex) {
            Logger.getLogger(Spider.class.getName()).log(Level.SEVERE, null, ex);
        }
            //System.out.println(parsedDoc);
            //parsedDoc.replaceAll("[^\\w\\-\\@]"," ");
            parsedDoc = parsedDoc.replaceAll("<.*>", "");
            Scanner scan = new Scanner(parsedDoc);
            while (scan.hasNext()) {
                listOut.add(scan.next());
            }
            return listOut;

   }

    /*
     * Title: lower()
     * Purpose: formats all tokens into the lower case
     * Parameters:
     *      ArrayList<String> listIn: the list of newly html-free tokens
     * Return Values:
     *      ArrayList<String> listOut: the newly single-case list
     */
    public List<String> lower(List<String> listIn){
        List<String> listOut = new ArrayList<String>();
        for(String st:listIn){// we step through the list and convert each word to the lower case
            listOut.add(st.toLowerCase());// pretty self-explanatory, really.
        }

        return listOut;
    }

    /*
     * Title: stem()
     * Purpose: feeds all tokens in the page through the Porter Stemmer
     * Parameters:
     *      ArrayList<String> listIn: the list of token Strings
     * Return Value:
     *      ArrayList<String> listOut: the newly stemmed list
     */
    public List<String> stem(List<String> listIn){
        List<String> listOut = new ArrayList<String>();

        //The porter stemmer manipulates each word individually
        for(String st:listIn){//So we read through the list
            Stemmer spiderStemmer = new Stemmer();
            for(int i = 0;i<st.length();i++){//Then read through each word to add it into a new stemmer
                spiderStemmer.add(st.charAt(i));
            }
            spiderStemmer.stem();// The stemmer then does its thing

            listOut.add(spiderStemmer.toString());// and returns the stemmed word back to the list
        }
        return listOut;
    }

    /*
     *
     */
    public List getTerms(List<String> listIn){
        List<String> listOut = new ArrayList<String>();
        HashSet uniqueTerms = new HashSet();

        //The list is converted to a set to remove repeats, and then dumped to a new list
        uniqueTerms.addAll(listIn);
        listOut.addAll(uniqueTerms);

        //Now sort that shit. Using a convenient library.
        Collections.sort(listOut);
        return listOut;
    }


    /*  Function: getHeader()
     *  Purpose: Goes to the specified webpage and copies the page header
     *
     */
    public List getHeader(String urlIn){
        List<String> listOut = new ArrayList<String>();
        try{
            URL url = new URL(urlIn);
            URLConnection conn = url.openConnection();

            for (int i=0; ; i++){
                String name = conn.getHeaderFieldKey(i);
                String value = conn.getHeaderField(i);
                if (name == null && value == null){
                    break;
                }
                if (name == null){
                    listOut.add("Server HTTP version, Response code: " + value);
                }
                else{
                listOut.add(name + "=" + value);
                }
            }

        } catch (Exception e) {}
        return listOut;
    }

    /*
     *
     */
    public void fetch(String urlIn,String itemType, SearchDB base, int item){
        try{
                /*  Oh my God, stop trying to make fetch() happen.
                *  ... I realize that was a terrible joke. Sorry
                */
            String title = "";
            String fileID = "";
            System.out.println(" - Fetching Page...");
            String ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.122 Safari/534.30";
            Document htmlDoc = Jsoup.connect(urlIn).userAgent(ua).get();
            title = htmlDoc.title();
            System.out.println(title);
            base.insertUrltoItem(item);
            base.insertURL(urlIn,itemType,title);

            fileID = base.getUrl(urlIn);
            String fileName = "" + fileID + ".txt";
            System.out.println("Writing to file " + fileName);
            //base.insertUrltoItem();
            String[] rawArray = htmlDoc.toString().split(" ");


            //Get the Raw text (NOT HTML) of the passed URL
            File rawFile = new File("/Users/tmarch1/Development/NetBeansProjects/490/490 Lab5-Evaluation/data/raw/" + fileName);
            BufferedWriter rawOutput = new BufferedWriter(new FileWriter(rawFile));
            //rawOutput.write(htmlDoc.);
            for(int i = 0;i<rawArray.length;i++){
                rawOutput.write(rawArray[i]);
                rawOutput.newLine();
            }
            rawOutput.close();

            //get the cleaned (lowered) document
            String parsedDoc = Jsoup.clean(htmlDoc.text(), Whitelist.relaxed());
            //parsedDoc = parsedDoc.replaceAll("<.*>", "");
            parsedDoc = parsedDoc.replaceAll(",", "");
            parsedDoc = parsedDoc.replaceAll(":", "");
            parsedDoc = parsedDoc.replaceAll("\\.", "");
            parsedDoc = parsedDoc.replaceAll(";", "");
            parsedDoc = parsedDoc.replaceAll("-", "");
            parsedDoc = parsedDoc.replaceAll("&amp", "");
            parsedDoc = parsedDoc.replaceAll("&quot", "");
            parsedDoc = parsedDoc.toLowerCase();
            String[] parsedArray = parsedDoc.split(" ");

            /*File cleanFile = new File("/Users/tmarch1/NetBeansProjects/490/490 Lab2-Web_Crawling/data/clean/" + fileName);
            FileWriter fstream = new FileWriter(cleanFile);
            BufferedWriter out = new BufferedWriter(fstream);
            System.out.println(parsedDoc);
            //out.write(parsedDoc);
            out.write("Please Work!");
            out.close();*/
            
            File cleanFile = new File("/Users/tmarch1/Development/NetBeansProjects/490/490 Lab5-Evaluation/data/clean/" + fileName);
            BufferedWriter cleanOutput = new BufferedWriter(new FileWriter(cleanFile));
            for(int i = 0;i < parsedArray.length;i++){
                cleanOutput.write(parsedArray[i]);
                cleanOutput.newLine();
            }
            cleanOutput.close();
            
            //get the header
            List pageHeader = this.getHeader(urlIn);
            String[] headerArray = new String[pageHeader.size()];
            pageHeader.toArray(headerArray);

            File headerFile = new File("/Users/tmarch1/Development/NetBeansProjects/490/490 Lab5-Evaluation/data/header/" + fileName);
            BufferedWriter headerOutput = new BufferedWriter(new FileWriter(headerFile));
            //headerOutput.write(pageHeader.toString());
            for(int i =0;i<headerArray.length;i++){
                headerOutput.write(headerArray[i]);
                headerOutput.newLine();
            }
            headerOutput.close();

            //return urlnum;
        } catch (IOException ex) {
            Logger.getLogger(Spider.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }

    


}