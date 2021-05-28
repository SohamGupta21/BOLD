package sample;

import java.util.ArrayList;
import java.util.Arrays;

public class Player {
    String name = "";
    ArrayList<Card> powerups = new ArrayList<>();
    // the order of the 4 are multiplier, second chance, pattern, double or nothing
    int[] num_of_each = new int[4];
    boolean powerupEnabled = false;
    public Player(String n){
        name = n;
    }
    public String getName(){
        return name;
    }
    public void enablePowerup(){
        powerupEnabled = true;
    }
    public void disablePowerup(){
        powerupEnabled = false;
    }
    public boolean getPowerupEnabled(){
        return powerupEnabled;
    }
    public void addPowerup(Card c){
        System.out.println("a powerup is being added");
        powerups.add(0, c);
        if(c.getAttributes()[3].equals("M")){
            System.out.println("Do I ever run");
            num_of_each[0] += 1;
        }
        if(c.getAttributes()[3].equals("S")){
            num_of_each[1] += 1;
        }
        if(c.getAttributes()[3].equals("P")){
            num_of_each[2] += 1;
        }
        if(c.getAttributes()[3].equals("D")){
            num_of_each[3] += 1;
        }
    }
    public void removePowerup(String c){
        for(Card power: powerups){
            if(power.getAttributes()[3].equals(c.substring(3))){
                if(power.getAttributes()[3].equals("M")){
                    num_of_each[0] -= 1;
                }
                if(power.getAttributes()[3].equals("S")){
                    num_of_each[1] -= 1;
                }
                if(power.getAttributes()[3].equals("P")){
                    num_of_each[2] -= 1;
                }
                if(power.getAttributes()[3].equals("D")){
                    num_of_each[3] -= 1;
                }
                powerups.remove(power);
                break;
            }
        }
    }
//    public void arrangePowerups(){
//        for(int i: num_of_each){
//            i = 0;
//        }
//        for(Card c: powerups){
//            if(c.getAttributes()[3] == "M"){
//                num_of_each[0] += 1;
//            }
//            if(c.getAttributes()[3] == "S"){
//                num_of_each[1] += 1;
//            }
//            if(c.getAttributes()[3] == "P"){
//                num_of_each[2] += 1;
//            }
//            if(c.getAttributes()[3] == "D"){
//                num_of_each[3] += 1;
//            }
//        }
//    }
    public int[] getPowerups(){
        return num_of_each;
    }
}
