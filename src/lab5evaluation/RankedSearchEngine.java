package lab5evaluation;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.*;
/**
 *
 * @author tmarch1
 */
public class RankedSearchEngine {
    public void run(){
        Scanner commandScan = new Scanner(System.in);
        String docScheme = "";
        String queryScheme = "";
        //String query = "";

        System.out.print("Building Positional Index...");
        Index positionIndex = new Index();
        System.out.println("Done!\n");

        System.out.print("Please enter a DOCUMENT weighting scheme (ex. ltc, nnn): ");
        docScheme = commandScan.next();
        while(!docScheme.matches("^[nl][nt][nc]$")){
            System.out.print("Invalid scheme! Please input a new weighting Scheme: ");
            queryScheme = commandScan.next();
        }
        System.out.print("Please enter a QUERY weighting scheme (ex. ltc,nnn): ");
        queryScheme = commandScan.next();
        while(!queryScheme.matches("^[nl][nt][nc]$")){
            System.out.print("Invalid scheme! Please input a new weighting Scheme: ");
            queryScheme = commandScan.next();
        }


        //Call to positionIndex to weight the documents (pass docScheme)
        positionIndex.rankDocs(docScheme);
        //System.out.println("Document Weighting: "+docScheme);
        //System.out.println("Query Weighting: "+queryScheme);
        System.out.println("\n...Schemes Implemented.");

        String query = commandScan.nextLine();
        System.out.println("Please Enter a Query (Enter 'QUIT' to end search): ");
        query = commandScan.nextLine();
        //query = "revolutionary";
        while (!query.equals("QUIT")){
            System.out.println(query);
            query = query.toLowerCase();
            //Call to positionIndex to create Query index, weight (pass query, queryScheme)
            //System.out.println("Query to be Searched: " + query);
            positionIndex.rankQuery(query, queryScheme);
            positionIndex.compare();

            System.out.println("Please Enter a Query (Enter 'QUIT' to end search): ");
            query = commandScan.nextLine();
        }
    }

}
