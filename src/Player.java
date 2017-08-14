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

    public String getPlayerName() {
        return playerName;
    }

    public int getPlayerScore () {
        return playerScore;
    }

    public void setPlayerScore (int score) {
        playerScore = playerScore + score;
    }
}
