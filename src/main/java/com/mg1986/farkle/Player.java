package com.mg1986.farkle;

/**
 * Player class - Represents a player in the game.
 */

public class Player {

    // Player's name
    private String playerName;

    // Player's score
    private int playerScore;

    //Player's score for their current turn
    private int turnScore;

    // Number of dice Player has avialable for current roll
    private int numDiceInUse;

    // If Player has made it on the scoreboard
    private boolean onScoreboard;

    // Is is the Player's current turn -  used to preserve order of turns when game is saved
    private boolean isCurrentTurn;

    // Player constructor -  Create new Player object for new game
    Player (String name) {
        playerName = name;
        playerScore = 0;
        turnScore = 0;
        numDiceInUse = 0;
        onScoreboard = false;
        isCurrentTurn = false;
    }

    // Player constructor -  Create Player object for game loaded from save file
    Player (String name, int score, int numDice, boolean onBoard) {
        playerName = name;
        playerScore = score;
        turnScore = 0;
        numDiceInUse = numDice;
        onScoreboard = onBoard;
    }

    //------------------------------------------------------------------------------------------------------------------
    // setName() - Sets Player's name
    public void setName (String name) { playerName = name; }

    //------------------------------------------------------------------------------------------------------------------
    // getName() - Returns Player's name
    public String getName() { return playerName; }

    //------------------------------------------------------------------------------------------------------------------
    // setPlayerScore() - Sets Player's score
    public void setPlayerScore (int score) { playerScore = playerScore + score; }

    //------------------------------------------------------------------------------------------------------------------
    // getPlayerScore() - Gets Player's score
    public int getPlayerScore () { return playerScore; }

    //------------------------------------------------------------------------------------------------------------------
    // setTurnScore() - Set Player's score for the current turn
    public void setTurnScore(int score) { turnScore = turnScore + score; }

    //------------------------------------------------------------------------------------------------------------------
    // getTurnScore() - Returns Player's score for the current turn
    public Integer getTurnScore() { return turnScore; }

    //------------------------------------------------------------------------------------------------------------------
    // resetTurnScore() - Reset Player's score for the current turn
    public void resetTurnScore() { turnScore = 0; }

    //------------------------------------------------------------------------------------------------------------------
    // setNumDiceInUse() - Set number of dice Player currently has tied up in scoring combinations
    public void setNumDiceInUse(Integer numDice) { numDiceInUse = numDiceInUse + numDice; }

    //------------------------------------------------------------------------------------------------------------------
    // getNumDiceInUse() -  Return number of dice Player currently has tied up in scoring combinations
    public Integer getNumDiceInUse() { return numDiceInUse; }

    //------------------------------------------------------------------------------------------------------------------
    // resetNumDiceInUse() - Reset number of dice Player currently has tied up in scoring combinations to zero
    public void resetNumDiceInUse() { numDiceInUse = 0; }

    //------------------------------------------------------------------------------------------------------------------
    // setOnScoreboard() - Set boolean if Player is currently on scoreboard or not
    public void setOnScoreboard(boolean onBoard) { onScoreboard = onBoard; }

    //------------------------------------------------------------------------------------------------------------------
    // getOnScoreboard() - Return boolean if Player is currently on scoreboard or not
    public boolean getOnScoreboard() { return onScoreboard; }

    //------------------------------------------------------------------------------------------------------------------
    // setIsCurrentTurn() - Set boolean if currently Player's turn
    public void setIsCurrentTurn(boolean isTurn) { isCurrentTurn = isTurn; }

    //------------------------------------------------------------------------------------------------------------------
    // setIsCurrentTurn() - Return boolean if currently Player's turn
    public boolean getIsCurrentTurn() { return isCurrentTurn; }

    //------------------------------------------------------------------------------------------------------------------
    // resetPlayerTurn() - Resets all instance variables that deal with the player's turn.
    public void resetPlayerTurn() {
        resetTurnScore();
        resetNumDiceInUse();
        setIsCurrentTurn(false);
    }
}
