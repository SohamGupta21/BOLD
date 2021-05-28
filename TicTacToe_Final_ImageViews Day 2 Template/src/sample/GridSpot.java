package sample;

import javafx.scene.image.Image;

public class GridSpot {
    Image xx = new Image("resources/xMark.png");
    Image oo = new Image("resources/oMark2.png");
    Image pick;
    //represents the turn of the player which is their spot
    int spot;
    String resourceLink = "";
    public GridSpot(){
        spot = 0;
    }
    public void clickedSpot(int turn){
        //turn is passed in as a 1 or a 2
        spot = turn;
        if(spot == 1){
            //the picture would be an x for player 1
            pick = xx;
        }else{
            //the picture would be an 0 for player 2
            pick = oo;
        }
    }
    public int getSpot(){
        return spot;
    }
    public void setZero(){
        spot = 0;
    }
    public Image getResourceLink() {
        return pick;
    }
}
