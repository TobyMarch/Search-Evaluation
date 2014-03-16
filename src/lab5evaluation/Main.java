/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lab5evaluation;
import java.io.IOException;

/**
 *
 * @author tmarch1
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        //Spider testSpider = new Spider("/Users/tmarch1/NetBeansProjects/490/490 Lab5-Evaluation/");
        //testSpider.getParsedText("http://etext.virginia.edu/toc/modeng/public/DicTale.html");

        //use for Building the Database
        //Crawler webCrawler = new Crawler();
        //webCrawler.getQueries("movie.txt", 1);
        //webCrawler.getQueries("music.txt", 15);
        //webCrawler.getQueries("book.txt", 28);

        //Use for Building and Searching the Index
        //BooleanSearchEngine mySearch = new BooleanSearchEngine();
        //mySearch.Run();

        //Use for Building the Ranked Index
        //RankedSearchEngine mySearch = new RankedSearchEngine();
        //mySearch.run();

        //Evalutation
        Evaluator myEval = new Evaluator();
        myEval.run();
    }

}
