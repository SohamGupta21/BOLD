package sample;
//imports

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    //these the arrays for the 3 grid panes with imageviews in the project, representing the board, the powerups that a person has and the grid pane that highlights matches
    private final ImageView[][] boardSpotsIMG = new ImageView[4][5];
    private final ImageView[] powerImages = new ImageView[4];
    private final ImageView[][] matchCards = new ImageView[4][3];
    //this is the array for images of the pictures that show what matches between cards
    private final String[][] matchImages = {{"dots.png", "stripes.png", "stars.png"}, {"bottle.jpg", "glass.jpg", "carton.jpg"}, {"blue.png", "orange.jpg", "yellow.png"}, {"big.png", "medium.png", "small.png"}};
    //this is an array for the cards
    private final ArrayList<Card> deck = new ArrayList<>(81);
    //this is a 2D array of the board
    private final Card[][] board = new Card[4][5];
    //this is the temporary array that stores all the cards that a user has clicked in their turn
    private final ArrayList<String[]> temp = new ArrayList<>();
    //this represents the indexes of all of the cards that have not been used in the game, this means all the cards that have not been taken by a user
    private final ArrayList<Integer> unused_cards = new ArrayList<>();
    //this is array of all of the players
    private final ArrayList<Player> names = new ArrayList<>();
    //all of the FXML objects
    @FXML
    Label lblTurn, lblResult, score1, score2, score3, score4, message,chooserLabel, numMult,numSecond, numPat, numDouble, patternInstructions;
    @FXML
    Button btnStart, takePts, notUsingPowerup, accept, resetBtn;
    @FXML
    Text matches, gameOver;
    @FXML
    CheckBox powerupCheckBox, cheat;
    @FXML
    TextField p1name, p2name, p3name, p4name, patternField, multField, secondField, patField, doubleField;
    @FXML
    GridPane gdpPlayGrid, highlightMatch, powerGrid;
    @FXML
    Rectangle nameRect, titleRect, gameRect, matchRect, resultRect;
    //this is the Image that is shown before the cards are flipped over
    Image back = new Image("Back.jpg");
    //these variables show the turn, number of player, row and column index for a clicked card, the score of the players and the points that the current player has earned on their current turn
    private int turn, numPlayers, rowIndex, colIndex, p1score, p2score, p3score, p4score, currentPlayerPoints;
    //this identifies if the user is using powerups and whether or not to set the game up with the cheat layout
    private boolean usingPowerups, cheatLayout = false;
    //these variables allow the code to apply powerups throughout th
    private int pMult = 1;
    private boolean pSecond, pDouble;
    private boolean usedSecondorDoublePowerup;
    private String pPattern = "";
    //this is just a instance field that allows me to use the getTurnready function with an fxml function (since the getTurnReady function has a parameter)
    private boolean temp_time;
    private int count = 0;
    private int[] last_selected = new int[2];
    @FXML
    private void enableStart(){
        if(!p1name.getText().equals(null)){
            btnStart.setDisable(false);
            p3name.setDisable(false);
            p4name.setDisable(false);
        }
    }
    @FXML
    //runs when the start button is clicked, sets ups the game play
    private void start() {
        //checks if the user is playing with powerups, if they are it changes the use powerups boolean and setsup the gridpane for powerups
        if (powerupCheckBox.isSelected()) {
            usingPowerups = true;
            showPowerupScreen();
        } else {
            setUpGame();
        }

    }
    //allows the user to set up their powerups
    private void showPowerupScreen(){
        //makes unnecessary things false
        p1name.setVisible(false);
        p2name.setVisible(false);
        p3name.setVisible(false);
        p4name.setVisible(false);
        btnStart.setVisible(false);
        powerupCheckBox.setVisible(false);
        cheat.setVisible(false);
        //makes needed things true
        System.out.println("VISIBLLLEEEEEE YOU");
        chooserLabel.setVisible(true);
        numMult.setVisible(true);
        numSecond.setVisible(true);
        numPat.setVisible(true);
        numDouble.setVisible(true);
        multField.setVisible(true);
        secondField.setVisible(true);
        patField.setVisible(true);
        doubleField.setVisible(true);
        accept.setVisible(true);
    }
    //sets up the actual game
    private void setUpGame(){
        //makes powerup items invisible
        chooserLabel.setVisible(false);
        numMult.setVisible(false);
        numSecond.setVisible(false);
        numPat.setVisible(false);
        numDouble.setVisible(false);
        multField.setVisible(false);
        secondField.setVisible(false);
        patField.setVisible(false);
        doubleField.setVisible(false);
        accept.setVisible(false);
        //sees to use the cheat layout
        if (cheat.isSelected()) {
            cheatLayout = true;
        }
        //adds all the names of the players, these were types in by the user
        addNames();
        numPlayers = names.size();
        //shows the game board and hides the initial setup screen
        makeGameVisible();
        //tells the user who's turn it is
        lblTurn.setText("Turn: " + (turn % numPlayers + 1));
        //generate all of the cards using the 4 for loops
        if(usingPowerups){
            loadCards(Integer.parseInt(multField.getText()),Integer.parseInt(secondField.getText()),Integer.parseInt(patField.getText()),Integer.parseInt(doubleField.getText()));
        } else {
            loadCards();
        }

        //runs the function to create imageviews for the main game board
        createImageViewsforCards();
        //puts spaces between the images that highlight what attributes match
        createImageViewsforMatchCards();
        //this unused cards array keeps track of what cards have been put into the game board and what hasn't
        handleUnusedCards();
        //fills the board array that corresponds to the main board and tells which cards go where in the main grid pane
        fillBoardArray();
        //this is the mouse event: same as if you were adding it in scenebuilder but this lets you do it dynamically

    }
    //moves on from the powerup chooser screen
    @FXML
    private void acceptPowerups(){
        //makes unnecessary things invisible
        System.out.println("INVISIBLEEEEEEEEEE ME");
        nameRect.setVisible(false);
        chooserLabel.setVisible(false);
        numMult.setVisible(false);
        numSecond.setVisible(false);
        numPat.setVisible(false);
        numDouble.setVisible(false);
        multField.setVisible(false);
        secondField.setVisible(false);
        patternField.setVisible(false);
        patternInstructions.setVisible(false);
        doubleField.setVisible(false);
        accept.setVisible(false);
        //sets up the grid for powerups
        createImageViewsforPowerUps();
        powerGrid.setVisible(true);

        setUpGame();
    }
    //this adds all of the names that the user types in
    private void addNames() {
        //adds the names that were typed in into the name array
        if (!p1name.getText().equals("")) {
            names.add(new Player(p1name.getText()));
        }
        if (!p2name.getText().equals("")) {
            names.add(new Player(p2name.getText()));
        }
        if (!p3name.getText().equals("")) {
            System.out.println("getting here");
            names.add(new Player(p3name.getText()));
        }
        if (!p4name.getText().equals("")) {
            names.add(new Player(p4name.getText()));
        }
    }
    //creates card for all of the pictures
    private void loadCards() {
        //uses the 4 for loops to fill the deck
        String[] background = {"D", "L", "S"};
        String[] container = {"B", "C", "J"};
        String[] color = {"B", "O", "Y"};
        String[] size = {"B", "M", "S"};
        int count = 0;
        for (String b : background) {
            for (String con : container) {
                for (String col : color) {
                    for (String s : size) {
                        //adds all of the cards to the deck
                        deck.add(new Card(b + con + col + s));
                        count++;
                    }
                }
            }
        }
        //keeps the card list to 20 cards if using the cheat layout
        if (cheatLayout) {
            for (int i = 0; i < 61; i++) {
                deck.remove(deck.size() - 1);
            }
        }
    }
    //second load cards method for powerups
    private void loadCards(int Mult, int Second, int Patt, int Doub) {
        //uses the 4 for loops to fill the deck
        String[] background = {"D", "L", "S"};
        String[] container = {"B", "C", "J"};
        String[] color = {"B", "O", "Y"};
        String[] size = {"B", "M", "S"};
        int count = 0;
        for (String b : background) {
            for (String con : container) {
                for (String col : color) {
                    for (String s : size) {
                        //adds all of the cards to the deck
                        deck.add(new Card(b + con + col + s));
                        count++;
                    }
                }
            }
        }
        //keeps the card list to 20 cards if using the cheat layout
        if (cheatLayout) {
            for (int i = 0; i < 61; i++) {
                deck.remove(deck.size() - 1);
            }
        }
        //generates all of the powerup cards if the user is playing with powerups
        if (usingPowerups) {
            for(int i = 0; i<Doub;i++){
                deck.add(0, new Card("000D"));
            }
            for(int i = 0; i<Patt;i++){
                deck.add(0, new Card("000P"));
            }
            for(int i = 0; i<Second;i++){
                deck.add(0, new Card("000S"));            }
            for(int i = 0; i<Mult;i++){
                deck.add(0, new Card("000M"));
            }

        }
    }
    //fills the board array that corresponds to the main play grid pane
    private void fillBoardArray() {
        //places the cards into the board array
        //this height is 4
        for (int i = 0; i < board.length; i++) {
            //this length is 5
            for (int j = 0; j < board[i].length; j++) {
                //this determines the index, in terms of whether it is going to take the first card off or whether it will be random
                int index = 0;
                if (!cheatLayout) {
                    index = (int) (Math.random() * unused_cards.size());
                }
                //remove this code for final product
                if (!cheatLayout && count < 5) {
                    index = 0;
                    count++;
                    for (Card c : deck) {
                        if (c.getAttributes()[0].equals("0")) {
                            System.out.println("there are still power card");
                        }
                    }
                }
                //actually removes the card the corresponds with the index picked earlier
                board[i][j] = deck.get(index);
                unused_cards.remove(index);
                deck.remove(index);
            }
        }
    }
    //fills the  main playing grid pane with image views
    private void createImageViewsforCards() {
        //this code creates all the imageviews for the cards
        for (int i = 0; i < boardSpotsIMG.length; i++) {
            for (int j = 0; j < boardSpotsIMG[i].length; j++) {
                //initializes each of the indexes in the ImageView array with empty ImageView
                boardSpotsIMG[i][j] = new ImageView();
                //this will rotate the imageviews to account for the images that were sideways
                boardSpotsIMG[i][j].setRotate(boardSpotsIMG[i][j].getRotate() + 90);
                //sets each ImageView in the array to the back of the card
                boardSpotsIMG[i][j].setImage(back);
                //each ImageView is a 75 by 60 rectangle
                boardSpotsIMG[i][j].setFitHeight(75);
                boardSpotsIMG[i][j].setFitWidth(60);
                //Parameters:  object, columns, rows
                //adds each of the ImageViews to the GridPane in javafx at a specific spot
                gdpPlayGrid.add(boardSpotsIMG[i][j], j, i);

            }
        }
        EventHandler z = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                //gets the row index of which image you clicked on
                rowIndex = GridPane.getRowIndex(((ImageView) t.getSource()));
                //gets the column index of which image you clicked on
                colIndex = GridPane.getColumnIndex(((ImageView) t.getSource()));
                last_selected[0] = rowIndex;
                last_selected[1] =colIndex;
                boardSpotsIMG[rowIndex][colIndex].setFitHeight(75);
                boardSpotsIMG[rowIndex][colIndex].setFitWidth(60);
                //clears the power image
                for (ImageView i : powerImages) {
                    i.imageProperty().set(null);
                }
                //if there is still a card visible in the image view chosen
                if (board[rowIndex][colIndex] != null) {
                    //shows the card that corresponds with the place
                    boardSpotsIMG[rowIndex][colIndex].setImage(new Image(board[rowIndex][colIndex].getPath()));
                    //if the card is not a powerup (which has a path in the form 000some-letter.jpg)
                    if (!board[rowIndex][colIndex].getAttributes()[0].equals("0")) {
                        for(ImageView[] i: boardSpotsIMG){
                            for(ImageView j: i){
                                j.setDisable(true);
                            }
                        }
                        //increases the possible point the player will earn
                        currentPlayerPoints++;
                        //the temp array contains the attributes of the cards that a person has flipped in this turn
                        temp.add(board[rowIndex][colIndex].getAttributes());
                        //compare function checks if there is a match
                        compare(temp);
                    } else {
                        boardSpotsIMG[rowIndex][colIndex].setRotate(boardSpotsIMG[rowIndex][colIndex].getRotate()-90);
                        //if the card is a powerup, add a powerup, and tell the user what powerup it is
                        names.get(turn % numPlayers).addPowerup(board[rowIndex][colIndex]);
                        printPowerupMessage(board[rowIndex][colIndex]);
                    }
                } else {
                    //if there is not a card there, then it notifies the user
                    message.setText("Sorry there is no card in that location");
                }

            }
        };
        //sets the event handler for the mouse click
        for (int i = 0; i < boardSpotsIMG.length; i++) {
            for (int j = 0; j < boardSpotsIMG[0].length; j++) {
                //setting the onMouseClicked property for each of the ImageViews to call z (the event handler)
                boardSpotsIMG[i][j].setOnMouseClicked(z);
            }
        }
    }
    //fills the grid pane used for identifying matches with image views
    private void createImageViewsforMatchCards() {
        //sets gaps for the highlight match gridpane
        highlightMatch.setVgap(5);
        highlightMatch.setHgap(5);
        //this code fills that grid on the right which will have all of the attributes that will match
        for (int i = 0; i < matchCards.length; i++) {
            for (int j = 0; j < matchCards[i].length; j++) {
                //initializes each of the indexes in the ImageView array with empty ImageView
                matchCards[i][j] = new ImageView();
                //shows all of the possible matches
                matchCards[i][j].setImage(new Image(matchImages[i][j]));
                //size of the images
                matchCards[i][j].setFitHeight(70);
                matchCards[i][j].setFitWidth(55);
                //Parameters:  object, columns, rows
                //adds each of the ImageViews to the GridPane in javafx at a specific spot
                highlightMatch.add(matchCards[i][j], j, i);
                //sets each ImageView in the array to a blank square png found in the resources directory

            }
        }
    }
    //fills the image view that tells the user what powerups they have with imageviews
    private void createImageViewsforPowerUps() {
        //the power image is only a 1D array because there is one row of powerups
        for (int i = 0; i < powerImages.length; i++) {
            //initializes each of the indexes in the ImageView array with empty ImageView
            powerImages[i] = new ImageView();
            //this will rotate the imageviews to account for the images that were sideways
            //sets each ImageView in the array to the back of the card
            powerImages[i].imageProperty().set(null);
            //each ImageView is a 75 by 60 rectangle
            powerImages[i].setFitHeight(75);
            powerImages[i].setFitWidth(60);
            //Parameters:  object, columns, rows
            //adds each of the ImageViews to the GridPane in javafx at a specific spot
            powerGrid.add(powerImages[i], i, 0);

        }

        EventHandler z = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                //clears the image views to get rid of the card the user clicks
                for (ImageView i : powerImages) {
                    i.imageProperty().set(null);
                }
                //gets the column index of which image you clicked on, there is not a row index, cause it is a 1D array
                colIndex = GridPane.getColumnIndex(((ImageView) t.getSource()));
                if (powerImages[colIndex] != null) {
                    //looks at the powerup that is being found and runs the function for that
                    if (colIndex == 0) {
                        powerMultiplier(names.get(turn % numPlayers));
                    } else if (colIndex == 1) {
                        powerSecondChance(names.get(turn % numPlayers));
                    } else if (colIndex == 2) {
                        powerPattern(names.get(turn % numPlayers));
                    } else if (colIndex == 3) {
                        powerDoubleOrNothing(names.get(turn % numPlayers));
                    }
                }
            }
        };
        //sets the event handler for the mouse click
        for (int i = 0; i < powerImages.length; i++) {
            //setting the onMouseClicked property for each of the ImageViews to call z (the event handler)
            powerImages[i].setOnMouseClicked(z);
        }
    }
    //fills the array that handles the unused cards, this array helps identify the indexes of the cards that haben't be used
    private void handleUnusedCards() {
        //adds only 20 cards to the unused cards if there is
        if (cheatLayout) {
            for (int k = 0; k < 20; k++) {
                unused_cards.add(k);
            }
        } else {
            //adds the entire deck to the unused cards array (this includes the 81 cards plus the powerups)
            for (int k = 0; k < deck.size(); k++) {
                unused_cards.add(k);
            }
        }
    }
    //this is the function that runs when a player decides to use
    private void powerMultiplier(Player p) {
        //removes the powerup from the player and sets the multiplication factor
        p.removePowerup("000M");
        pMult = 5;
    }
    //this function runs when a player uses the second chance powerup
    private void powerSecondChance(Player p) {
        //they have not redeemed the powerup yet so it is false
        usedSecondorDoublePowerup = true;
        //removes the last card (which didn't match the previous cards)
        boardSpotsIMG[last_selected[0]][last_selected[1]].setImage(back);
        temp.remove(temp.size() - 1);
        p.removePowerup("000S");
        //tells that the second chance powrup is active
        pSecond = true;
    }
    //this function is run when a player decides to use the Pattern powerup
    private void powerPattern(Player p) {
        //remvoe powerup from the player
        p.removePowerup("000P");
        //allow the player to type in their pattern
        patternField.setVisible(true);
        patternInstructions.setVisible(true);
        pPattern = "blue";
    }
    //allows the user to type in the pattern that they would like to identify
    @FXML
    private void setPattern() {
        //takes what the user type in and sets it to the pattern
        pPattern = patternField.getText();
        if(!pPattern.equals("blue") &&!pPattern.equals("yellow")&&!pPattern.equals("orange"))pPattern =  "blue";
        patternField.setVisible(false);
        patternInstructions.setVisible(false);
    }
    //this runs when a user decides to use the double or nothing powerup
    private void powerDoubleOrNothing(Player p) {
        //tells that the double powerup has not been redeemed
        usedSecondorDoublePowerup = true;
        //removes the last card from temp (as the powerup entails)
        temp.remove(temp.size() - 1);
        p.removePowerup("000D");
        pDouble = true;
    }
    //tells the user what powerup they have recieved, when they flip the powerup card over
    private void printPowerupMessage(Card power) {
        //finds what powerup is found and tells the user
        if (power.getAttributes()[3].equals("M")) {
            //this is a reverse
            message.setText("Congrats, you have received the multiplier card. This card can be used before a turn and allows you to get put a multiplier on the points that you receive for a turn.");
        } else if (power.getAttributes()[3].equals("S")) {
            //this is a loot
            message.setText("Congratulations, you have received the second chance card. When you flip over a card that doesn't match the others, you can use this card to get another shot.");
        } else if (power.getAttributes()[3].equals("P")) {
            //this is a second chance
            message.setText("Congratulations you have received the pattern card. You can use this before a turn. In this card, you get the chance to choose a color that you will try to match your cards with. If you end up matching all of your cards with that color, you will get a bonus.");
        } else if (power.getAttributes()[3].equals("D")) {
            //add ten
            message.setText("Congratulations you have received the double or nothing card.  If you pick a card that doesn't match, you can use this card after a turn. But wbe careful, if you use this and then don't get more matches, your score goes to zero. If you do get more matches, you get double the number of points for that turn.");
        }
    }
    //this makes the parts of the main game board visible
    private void makeGameVisible() {
        //makes the setup items invisible
        p1name.setVisible(false);
        p2name.setVisible(false);
        p3name.setVisible(false);
        p4name.setVisible(false);
        btnStart.setVisible(false);
        nameRect.setVisible(false);
        powerupCheckBox.setVisible(false);
        cheat.setVisible(false);
        //makes everything that is needed for the game visible
        gdpPlayGrid.setVisible(true);
        gameRect.setVisible(true);
        message.setVisible(true);
        matchRect.setVisible(true);
        resultRect.setVisible(true);
        score1.setVisible(true);
        score2.setVisible(true);
        //sees if there are 3 or 4 players and the labels for those players need to be displayed
        if (numPlayers > 2) {
            score3.setVisible(true);
        }
        if (numPlayers > 3) {
            score4.setVisible(true);
        }
        //makes more thing visible
        matches.setVisible(true);
        lblTurn.setVisible(true);
        lblResult.setVisible(true);
        takePts.setVisible(true);
        notUsingPowerup.setVisible(true);
    }
    //this function compares all of the cards that are flipped over and identifies the match, if any
    private void compare(ArrayList<String[]> list) {
        //creates a boolean array for the results of the comparison
        ArrayList<Boolean> comp = new ArrayList<>();
        //starts off the array with true
        for (int k = 1; k <= 4; k++) {
            comp.add(true);
        }
        //actually compares
        //MR. CORTEZ, HERE IS THE ACTUALLY COMPARISON, IF YOU WOULD LIKE TO SEE
        //outer array is for the attribute
        for (int i = 0; i < list.get(0).length; i++) {
            //this loops through all of the cards
            for (int j = 0; j < list.size(); j++) {
                //checks if everything is the same
                if (!list.get(j)[i].equals(list.get(0)[i])) {
                    comp.set(i, false);
                } else if (i == 0 && list.get(j)[0].equals("0")) {
                    //usePowerup.setVisible(true);
                }

            }
        }
        //comp is array that tells us if we have a match
        //if no matches
        if (!comp.contains(true)) {
            //if we are using the powerups that give the user a second chance
            if (usingPowerups && (pSecond || pDouble) && !usedSecondorDoublePowerup) {
                for(ImageView[] i: boardSpotsIMG){
                    for(ImageView j: i){
                        j.setDisable(false);
                    }
                }
                //probably make card that is invalid invisible
                usedSecondorDoublePowerup = true;
            } else {
                //sets the player score to 0 if they have messed up using the powerup (the "nothing" from double or nothing)
                if (pDouble) {
                    //identifies the player and changes their score
                    if (turn % numPlayers + 1 == 1) {
                        p1score = 0;
                        score1.setText(names.get(0).getName() + ": " + p1score + "");
                    } else if (turn % numPlayers + 1 == 2) {
                        p2score = 0;
                        score2.setText(names.get(1).getName() + ": " + p2score + "");
                    } else if (turn % numPlayers + 1 == 3) {
                        p3score = 0;
                        score3.setText(names.get(2).getName() + ": " + p3score + "");
                    } else if (turn % numPlayers + 1 == 4) {
                        p4score = 0;
                        score4.setText(names.get(3).getName() + ": " + p4score + "");
                    }
                    //use of powerup is over
                    pDouble = false;
                }
                //use of powerup is over
                if (pSecond) {
                    pSecond = false;
                }
                //switches the turn
                switchTurn(true);
                //clears the powerup array
                lblTurn.setText("Turn: " + (turn % numPlayers + 1));
                for (int i = 0; i < matchCards.length; i++) {
                    for (int j = 0; j < matchCards[i].length; j++) {
                        matchCards[i][j].imageProperty().set(null);
                    }
                }
            }
        //if there is a match
        } else {
            //makes all of the image views enabled
            for(ImageView[] i: boardSpotsIMG){
                for(ImageView j: i){
                    j.setDisable(false);
                }
            }
            //clears the matches so that new ones can be sho
            for (int i = 0; i < matchCards.length; i++) {
                for (int j = 0; j < matchCards[i].length; j++) {
                    matchCards[i][j].imageProperty().set(null);
                }
            }
            //loops through the comparisons, finds the attribute that is true
            for (int i = 0; i < comp.size(); i++) {
                String commonAttribute = "";
                //looks at all of the attributes with a value of true
                //if the comp is true, passes the value to the highlightCommon and hihglights what needs to be highlighted
                if (comp.get(i)) {
                    //make sure you know that
                    commonAttribute = list.get(0)[i];
                    //uses this to identify the position in the match cards array that needs to be highlighted
                    int[] common = highlightCommonAtt(i, commonAttribute);
                    //actaully highlights the position
                    displayMatches(common);
                }
            }
        }
    }
    //finds the integer positions of the matching attrributes
    private int[] highlightCommonAtt(int category, String att) {
        //take a category (size, color, etc.) and an attribute (blue, yellow, striopes, etc.)
        String[][] combinations = {{"D", "L", "S"}, {"B", "C", "J"}, {"B", "O", "Y"}, {"B", "M", "S"}};
        int[] answer = new int[2];
        int pos = 0;
        //this loops identifies the position of the attribute and returns that position
        for (int i = 0; i < combinations[category].length; i++) {
            if (combinations[category][i].equals(att)) {
                pos = i;
                answer[0] = category;
                answer[1] = pos;
            }
        }
        return answer;
    }
    //this takes an int array with the position that should be flipped over and flips that card over
    private void displayMatches(int[] item) {
        //takes in position of the matching attribute and makes that position visible
        matchCards[item[0]][item[1]].setImage(new Image(matchImages[item[0]][item[1]]));
        matchCards[item[0]][item[1]].setFitHeight(70);
        matchCards[item[0]][item[1]].setFitWidth(55);
    }
    //this happens when the user decides to take the points that they earned rather than flipping over more cards
    @FXML
    private void takePoints() {
        //try to abstract this to make it a separate function

        if (usingPowerups) {
            applyPowerupPoints();
        } else {
            currentPlayerPoints = currentPlayerPoints * currentPlayerPoints;
        }
        if (pDouble) {
            //takes one point off from the score since one card that didnt match was counted, multiplies by 2 (doubel part of double or nothing)
            currentPlayerPoints--;
            currentPlayerPoints *= 2;
        }
        //identifies the player that gets the points and gives them the points
        if (turn % numPlayers + 1 == 1) {
            p1score += currentPlayerPoints;
            score1.setText(names.get(0).getName() + ": " + p1score + "");
        } else if (turn % numPlayers + 1 == 2) {
            p2score += currentPlayerPoints;
            score2.setText(names.get(1).getName() + ": " + p2score + "");
        } else if (turn % numPlayers + 1 == 3) {
            p3score += currentPlayerPoints;
            score3.setText(names.get(2).getName() + ": " + p3score + "");
        } else if (turn % numPlayers + 1 == 4) {
            p4score += currentPlayerPoints;
            score4.setText(names.get(3).getName() + ": " + p4score + "");
        }
        //sets the board and changes the turn for the next user
        setBoard();
        getNextTurnReady(false);
    }
    //applies extra points gained form powerups
    private void applyPowerupPoints(){
        //multiplies for the multiplication powerup
        System.out.println("MULTI" + pMult);
        currentPlayerPoints = currentPlayerPoints * currentPlayerPoints * pMult;
        pMult = 1;
        //doesa the pattern points
        applyPatternPoints();
    }
    //this applies the points that someone earns through the pattern powerup
    private void applyPatternPoints() {
        //if the pattern is not empty
        if (!pPattern.equals("")) {
            //reads the pattern string and if it is valid, it gets applied
            if (pPattern.equals("blue")) {
                if (matchCards[2][0].getImage() != null) {
                    currentPlayerPoints *= 3;
                }
            }
            if (pPattern.equals("orange")) {
                if (matchCards[2][1].getImage() != null) {
                    currentPlayerPoints *= 3;
                }
            }
            if (pPattern.equals("yellow")) {
                if (matchCards[2][2].getImage() != null) {
                    currentPlayerPoints *= 3;
                }
            }
            pPattern = "";
        }
    }
    //this happens when someone has the opportunity to use a powerup but decides not to, instead pressing the not powreup button
    @FXML
    private void notUsePowerUp() {
        //changes the board for the next turn
        getNextTurnReady(temp_time);
    }
    //this switches the turn
    private void switchTurn(boolean time) {
        message.setText("");
        if (usingPowerups) {
            //diplays the powerups that apply after a turn
            displayAfterPowerups(names.get(turn % numPlayers));
            //checks if there are after powerups
            boolean afterPowerUpsPresent = false;
            //loops through the powerImages and if any of the images is not empty, then it states that powerups present is true
            for (ImageView i : powerImages) {
                if (i.getImage() != powerImages[0].getImage()) {
                    //finds that there are
                    System.out.println("wre");
                    System.out.println(i.imageProperty().get());
                    afterPowerUpsPresent = true;
                }
            }
            //if there are not any powerups
            if (!afterPowerUpsPresent) {
                //gets the next turn ready
                getNextTurnReady(time);
            } else {
                //if anything is there, then it sets an instance field to time so that the getNextTurnReady function can be run through a fxml function
                temp_time = time;
            }
        } else {
            //just runs the get next turn ready function if there is not powerups in this game
            getNextTurnReady(time);
        }

    }
    //this basically sets up the next turn for the game, including the timer to slow down the turn flip
    private void getNextTurnReady(boolean time) {
        //adds to turn
        turn++;
        if (usingPowerups) {
            //resets all of the powerup variables
            pMult = 1;
            pDouble = false;
            pPattern = "";
            pSecond = false;
            //makes the powerup imageview clear
            for (ImageView i : powerImages) {
                i.imageProperty().set(null);
            }
            //display the powerups for the next player that are applicable before a turn
            displayBeforePowerups(names.get(turn % numPlayers));
        }
        //changes turn
        lblTurn.setText("Turn: " + (turn % numPlayers + 1));
        //we have to clear the arraylist in which the comparing cards are passed in
        //uses a timer if needed to make sure users can still see the cards and are not flipped immediately
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                temp.clear();
                System.out.println("We are RUN");
                currentPlayerPoints = 0;
                flipBoard();
            }
        };
        if (time) {
            //if their is a timer, meaning that this is not run after the take points function, then it applies the time
            timer.schedule(task, 1000l);
        } else {
            //clears the temp, changes the point counter, flips the board
            temp.clear();
            currentPlayerPoints = 0;
            flipBoard();
        }
    }
    //this fills the game board for the next turn
    private void setBoard() {
        //this takes away the cards that were clicked and replaces them with cards that are in the deck
        for (int i = 0; i < boardSpotsIMG.length; i++) {
            for (int j = 0; j < boardSpotsIMG[i].length; j++) {
                //sets each ImageView in the array to a blank square png found in the resources directory
                if ((board[i][j] != null && temp.contains(board[i][j].getAttributes())) || (board[i][j] != null && board[i][j].getAttributes()[0].equals("0") && !boardSpotsIMG[i][j].getImage().equals(back))) {
                    if (!(unused_cards.size() == 0)) {
                        //removes a new card from the deck and puts it in any spots that need it
                        int index = (int) (Math.random() * unused_cards.size());
                        board[i][j] = deck.get(index);
                        unused_cards.remove(index);
                        deck.remove(index);
                        boardSpotsIMG[i][j].setImage(new Image(board[i][j].getPath()));
                    } else {
                        //if there are not any cards to take, then set the card spot to null
                        board[i][j] = null;
                        boardSpotsIMG[i][j].imageProperty().set(null);
                    }

                }

            }
        }
    }
    //this shows the back of all of the cards
    private void flipBoard() {
        //makes the message clear
        message.setText("");
        //makes the match cards flipped
        for(ImageView[] i: matchCards){
            for(ImageView j: i){
                j.imageProperty().set(null);
            }
        }
        //makes all of the image views enabled
        for(ImageView[] i: boardSpotsIMG){
            for(ImageView j: i){
                j.setDisable(false);
                //properly sets the rotation of the images, this has to be done because the powerup images don't need rotation while the others do so the rotation has to reset after every turn
                j.setRotate(0);
                j.setRotate(90);
            }
        }
        //this is where the timer has to be for the rest
        if (!checkGameOver()) {
            //this just flips everything back over
            for (int i = 0; i < boardSpotsIMG.length; i++) {
                for (int j = 0; j < boardSpotsIMG[i].length; j++) {
                    //sets each ImageView in the array to a blank square png found in the resources directory
                    if (board[i][j] != null) boardSpotsIMG[i][j].setImage(back);
                }
            }
        } else {
            //makes the game over screen visible
            gameOver.setVisible(true);
            //finds the biggest score and then identifies who the score belongs to and then puts that in the notification
            int max = 0;
            int index = 0;
            int[] scores = {p1score, p2score, p3score, p4score};
            for(int i = 0; i<scores.length; i++){
                if(scores[i]>max){
                    max = scores[i];
                    index = i;
                }
            }
            String winner = "";
            if(index == 0){
                winner = p1name.getText();
            } else if (index == 1){
                winner = p2name.getText();
            }else if (index == 2){
                winner = p3name.getText();
            }else {
                winner = p4name.getText();
            }
            gameOver.setText("Game Over the winner is: " + winner);
            //makes anything not the game over screen invisible
            powerGrid.setVisible(false);
            gdpPlayGrid.setVisible(false);
            gameRect.setVisible(false);
            message.setVisible(false);
            matchRect.setVisible(false);
            resultRect.setVisible(false);
            score1.setVisible(false);
            score2.setVisible(false);
            //sees if there are 3 or 4 players and the labels for those players need to be displayed
            if (numPlayers > 2) {
                score3.setVisible(false);
            }
            if (numPlayers > 3) {
                score4.setVisible(false);
            }
            //makes more thing visible
            matches.setVisible(false);
            lblTurn.setVisible(false);
            lblResult.setVisible(false);
            takePts.setVisible(false);
            notUsingPowerup.setVisible(false);
        }
    }
    //this displays the powerups for a person that are applicable before a turn
    private void displayBeforePowerups(Player p) {
        //if the user has multiplier or pattern powerup, then it displays these two cards in teh powerup array
        if (p.getPowerups()[0] != 0) {
            powerImages[0].setImage(new Image("000M.jpg"));
        }
        if (p.getPowerups()[2] != 0) {
            powerImages[2].setImage(new Image("000P.jpg"));
        }
    }
    //this displays the powerups for a person that are applicable after a turn
    private void displayAfterPowerups(Player p) {
        //if the person has a second chance or double or nothing powerup, it displays this in the powerup grid pane
        if (p.getPowerups()[1] != 0) {
            powerImages[1].setImage(new Image("000S.jpg"));
        }
        if (p.getPowerups()[3] != 0) {
            powerImages[3].setImage(new Image("000D.jpg"));
        }
    }
    //this checks if the game is over
    private boolean checkGameOver() {
        //looks if all the gameboard spots are empty
        int count = 0;
        for (int i = 0; i < boardSpotsIMG.length; i++) {
            for (int j = 0; j < boardSpotsIMG[i].length; j++) {
                //sets each ImageView in the array to a blank square png found in the resources directory
                if (board[i][j] == null) {
                    count++;
                }
            }
        }
        return count == 20;
        //this function runs the code that ends the game and shows the game over screen and stuff
    }
    @FXML
    private void resetGame(){
        //makes the setup items invisible
        p1name.setVisible(true);
        p2name.setVisible(true);
        p3name.setVisible(true);
        p4name.setVisible(true);
        btnStart.setVisible(true);
        nameRect.setVisible(true);
        powerupCheckBox.setVisible(true);
        cheat.setVisible(true);
        //makes everything that is needed for the game visible
        gdpPlayGrid.setVisible(false);
        gameRect.setVisible(false);
        message.setVisible(false);
        matchRect.setVisible(false);
        resultRect.setVisible(false);
        score1.setVisible(false);
        score2.setVisible(false);
        gameOver.setVisible(false);
        chooserLabel.setVisible(false);
        numMult.setVisible(false);
        numSecond.setVisible(false);
        numPat.setVisible(false);
        numDouble.setVisible(false);
        multField.setVisible(false);
        secondField.setVisible(false);
        patField.setVisible(false);
        doubleField.setVisible(false);
        accept.setVisible(false);
        powerGrid.setVisible(false);
        if (numPlayers > 2) {
            score3.setVisible(false);
        }
        if (numPlayers > 3) {
            score4.setVisible(false);
        }
        matches.setVisible(false);
        lblTurn.setVisible(false);
        lblResult.setVisible(false);
        takePts.setVisible(false);
        notUsingPowerup.setVisible(false);
        for(ImageView[] i : boardSpotsIMG){
            for(ImageView j: i){
                j.imageProperty().set(null);
            }
        }
        for(ImageView[] i : matchCards){
            for(ImageView j: i){
                j.imageProperty().set(null);
            }
        }
        for(ImageView j: powerImages){
            j.imageProperty().set(null);
        }
        deck.clear();
        temp.clear();
        unused_cards.clear();
        names.clear();
        powerupCheckBox.setSelected(false);
        cheat.setSelected(false);
        turn = 0;
        numPlayers = 0;
        rowIndex = 0;
        colIndex = 0;
        p1score = 0;
        p2score = 0;
        p3score = 0;
        p4score = 0;
        currentPlayerPoints = 0;
        usingPowerups = false;
        cheatLayout = false;
        pMult = 1;
        pSecond = false;
        pDouble = false;
        usedSecondorDoublePowerup = false;
        pPattern = "";
        temp_time = false;
        count = 0;
    }
}
