/**
 * Created by matt on 8/6/17.
 */

public class Player {

    private String playerName;
    private int playerScore;
    private int turnScore;
    private int numDiceInUse;
    private boolean onScoreboard;
    private boolean isCurrentTurn;

    Player (String name) {
        playerName = name;
        playerScore = 0;
        turnScore = 0;
        numDiceInUse = 0;
        onScoreboard = false;
        isCurrentTurn = false;
    }

    Player (String name, int score, int numDice, boolean onBoard) {
        playerName = name;
        playerScore = score;
        turnScore = 0;
        numDiceInUse = numDice;
        onScoreboard = onBoard;
    }

    public void setName (String name) {

        playerName = name;
    }

    public String getName() {

        return playerName;
    }

    public void setPlayerScore (int score) {

        playerScore = playerScore + score;
    }

    public int getPlayerScore () {

        return playerScore;
    }

    public void setTurnScore(int score) {

        turnScore = turnScore + score;
    }

    public Integer getTurnScore() {

        return turnScore;
    }

    public void resetTurnScore() {

        turnScore = 0;
    }

    public void setNumDiceInUse(Integer numDice) {

        numDiceInUse = numDiceInUse + numDice;
    }

    public Integer getNumDiceInUse() {

        return numDiceInUse;
    }

    public void resetNumDiceInUse() {

        numDiceInUse = 0;
    }

    public void setOnScoreboard(boolean onBoard) {

        onScoreboard = onBoard;
    }

    public boolean getOnScoreboard() {

        return onScoreboard;
    }

    public void setIsCurrentTurn(boolean isTurn) {

        isCurrentTurn = isTurn;
    }

    public boolean getIsCurrentTurn() {

        return isCurrentTurn; }
}
