/**
 * Created by matt on 10/16/17.
 */

public class TurnScore {

    private Integer turnScore;
    private Integer numDiceInUse;

    TurnScore() {
        turnScore = 0;
        numDiceInUse = 0;
    }

    public Integer getTurnScore() {
        return turnScore;
    }

    public void setTurnScore(Integer score) {
        turnScore = turnScore + score;
    }

    public void resetTurnScore() {
        turnScore = 0;
    }

    public Integer getNumDiceInUse() {
        return numDiceInUse;
    }

    public void setNumDiceInUse(Integer numDice) {
        numDiceInUse = numDiceInUse + numDice;
    }

    public void resetNumDiceInUse() {
        numDiceInUse = 0;
    }
}
