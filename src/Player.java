/**
 * Created by matt on 8/6/17.
 */

public class Player {

    public String playerName;
    public int playerScore;

    Player (String name) {
        playerName = name;
        playerScore = 0;
    }

    public String getName() {
        return playerName;
    }

    public int getScore () {

        return playerScore;
    }

    public void setScore (int score) {

        playerScore = playerScore + score;
    }
}
