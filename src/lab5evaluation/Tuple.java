package lab5evaluation;


import java.util.LinkedList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author tmarch1
 */
public class Tuple{
    public double weight;
    public LinkedList posList;

    public Tuple(double weightIn, LinkedList posListIn){
        this.weight = weightIn;
        this.posList = posListIn;
    }

    //Accessors
    public double getWeight(){
        return this.weight;
    }
    public LinkedList getPosList(){
        return this.posList;
    }

    //Mutators
    public void setWeight(double newWeight){
        this.weight = newWeight;
    }
    public void addPos(int newPos){
        this.posList.add(newPos);
    }

}
