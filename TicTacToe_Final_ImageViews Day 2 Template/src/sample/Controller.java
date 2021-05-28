package sample;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;



public class Controller {
    Image xx = new Image("resources/xMark.png");
    Image oo = new Image("resources/oMark2.png");
    Image blank = new Image("resources/blankSpot2.png");

    @FXML
    Label lblTurn, lblResult, lblTaken;
    @FXML
    Button btnStart;
    @FXML
    GridPane gdpPlayGrid;
    //Creates a 2D array of the GridSpot Class that represent one spot on a Tic Tac Toe Board
    private GridSpot [][] board = new GridSpot[3][3];
    //Creates a 2D array of ImageViews
    private ImageView [][] boardSpotsIMG = new ImageView [3][3];
    //turn = which player's turn
    int turn, rowIndex, colIndex;
    //tie is used to check if there is a tie or a when
    private boolean tie = false;
    @FXML
    private void start(){
        //turn is a random number: either 1 or 2 which represents the first or second player
        turn = (int) Math.round(Math.random()*1)+1;
        lblTurn.setText("It is Player " + (turn%2 + 1) + "'s Turn!" );
        btnStart.setDisable(true);
        for (int i = 0; i < boardSpotsIMG.length; i++) {
            for (int j = 0; j < boardSpotsIMG.length; j++) {
                //initializes each of the indexes in the ImageView array with empty ImageView
                boardSpotsIMG[i][j] = new ImageView();
                //sets each ImageView in the array to a blank square png found in the resources directory
                boardSpotsIMG[i][j].setImage(blank);//*NOTE* name of the image must match name in the directory
                //each ImageView is a 100 by 100 square
                boardSpotsIMG[i][j].setFitHeight(100);
                boardSpotsIMG[i][j].setFitWidth(100);
                //similar to initializing a new class but for each spot in the array
                board[i][j] = new GridSpot();//calls constructor
                //Parameters:  object, columns, rows
                //adds each of the ImageViews to the GridPane in javafx at a specific spot
                //Paramters:  object, columns, rows
                gdpPlayGrid.add(boardSpotsIMG[i][j], j, i);

            }
        }
        //this is the mouse event: same as if you were adding it in scenebuilder but this lets you do it dynamically
        EventHandler z = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                //gets the row index of which image you clicked on
                rowIndex = GridPane.getRowIndex(((ImageView) t.getSource()));
                //gets the column index of which image you clicked on
                colIndex = GridPane.getColumnIndex(((ImageView) t.getSource()));
                System.out.println(rowIndex);
                System.out.println(colIndex);
                lblTaken.setText("");
                display(rowIndex, colIndex, t);
                if(!checkDone()) {
                    System.out.println(turn % 2 + 1);
                    lblResult.setText("Result:");
                    switchTurn();
                }else{
                    //changes the image to the correct image (an x or o)
//                    ((ImageView) t.getSource()).setImage(board[rowIndex][colIndex].getResourceLink());
                    if(tie){
                        lblResult.setText("Result: The game resulted in a tie!");
                    }else{
                        lblResult.setText("Result: Player " + (turn%2 + 1) + ", you win!");
                    }
                    reset();
                }

            }
        };

        for (int i = 0; i < boardSpotsIMG.length; i++) {
            for (int j = 0; j < boardSpotsIMG.length; j++) {
                //setting the onMouseClicked property for each of the ImageViews to call z (the event handler)
                boardSpotsIMG[i][j].setOnMouseClicked(z);
            }
        }
    }
    public boolean checkDone(){
        if(turn>4 && turn<10) {
            for (GridSpot[] row : board) {
                if (row[0].getSpot() == row[1].getSpot() && row[1].getSpot() == row[2].getSpot() && row[0].getSpot() != 0) {
                    return true;
                }
            }
            for (int i = 0; i < board[0].length; i++) {
                if (board[0][i].getSpot() == board[1][i].getSpot() && board[1][i].getSpot() == board[2][i].getSpot() && board[0][i].getSpot() != 0) {
                    return true;
                }
            }
            if (board[0][0].getSpot() == board[1][1].getSpot() && board[1][1].getSpot() == board[2][2].getSpot() && board[0][0].getSpot() != 0) {
                return true;
            }
            if (board[0][2].getSpot() == board[1][1].getSpot() && board[1][1].getSpot() == board[2][0].getSpot() && board[0][2].getSpot() != 0) {
                return true;
            }
            if(turn == 9 || turn == 10){
                tie = true;
                return true;
            }
            //THE ONLY PROBLEM WITH THIS IS
        }
        return false;

    }
    public void switchTurn(){
        //switches between player 1 and 2
        turn++;
        lblTurn.setText("It is Player " + (turn%2+1) + "'s Turn!" );

    }

    //row = row index; col = column index; t = the source at where the image was clicked
    public void display(int row, int col, MouseEvent t){
        //checking if there is already a value at the position where the user clicked
        if(board[row][col].getSpot()!=0){
            lblTaken.setText("Result: Pick a new spot");
            System.out.println("Taken");
            //turn is subtracted because the person gets another try
            turn--;
        }else{
            //changes board to the turn of the player (1 or 2)
            board[row][col].clickedSpot(turn%2+1);
            //gets the resource link of the image based on the turn and sets the image in ImageView 2D array and teh javafx GridPane
            boardSpotsIMG[row][col].setImage(board[row][col].getResourceLink());
//            ((ImageView) t.getSource()).setImage(new Image(board[k][j].getResourceLink()));

        }
        //using board which is initialized with 0's, sout board to make sure it all works
        for(int k = 0;k<board.length;k++){
            for(int j = 0; j<board.length;j++){
                System.out.print(board[k][j].getSpot() + " ");
            }
            System.out.println("");
        }
    }
    public void reset(){
        //this code needs to reset all of the 
    }
}
