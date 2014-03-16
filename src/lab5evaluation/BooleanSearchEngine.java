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
public class BooleanSearchEngine {
    public void Run(){
        int commandIn;
        Scanner commandScan  = new Scanner(System.in);

        //User input, because I don't know what else to write at the moment
        Index positionIndex = new Index();
        System.out.println("Command Options:");
        System.out.println("1) Token Query");
        System.out.println("2) AND Query");
        System.out.println("3) OR Query");
        System.out.println("4) Phrase Query");
        System.out.println("5) Near Query");
        System.out.println("6) Quit");
        System.out.print("Please enter a Command: ");
        commandIn = commandScan.nextInt();

        while(commandIn != 6){
            String termA = "";
            String termB = "";

            switch(commandIn){
                case 1:
                    System.out.print("Please Enter a Single Word: ");
                    termA = commandScan.next();
                    positionIndex.singleQuery(termA);
                    break;
                case 2:
                    System.out.print("Please Enter the First Term: ");
                    termA = commandScan.next();
                    System.out.print("Please Enter the Second Term: ");
                    termB = commandScan.next();
                    positionIndex.andQuery(termA,termB);
                    break;
                case 3:
                    System.out.print("Please Enter the First Term: ");
                    termA = commandScan.next();
                    System.out.print("Please Enter the Second Term: ");
                    termB = commandScan.next();
                    positionIndex.orQuery(termA,termB);
                    break;
                case 4:
                    System.out.print("Please Enter the First Term: ");
                    termA = commandScan.next();
                    System.out.print("Please Enter the second Term: ");
                    termB = commandScan.next();
                    positionIndex.phraseQuery(termA,termB);
                    break;
                case 5:
                    System.out.print("Please Enter the First Term: ");
                    termA = commandScan.next();
                    System.out.print("Please Enter the Second Term: ");
                    termB = commandScan.next();
                    positionIndex.nearQuery(termA,termB);
                    break;
                default:
                    System.out.print("Invalid Command!");
                    break;

            }

            System.out.println("Command Options:");
            System.out.println("1) Token Query");
            System.out.println("2) AND Query");
            System.out.println("3) OR Query");
            System.out.println("4) Phrase Query");
            System.out.println("5) Near Query");
            System.out.println("6) Quit");
            System.out.println("Please enter a new Command: ");
            commandIn = commandScan.nextInt();

        }

    }

}
