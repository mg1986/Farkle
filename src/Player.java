/**
 * Created by matt on 8/6/17.
 */

public class Player {

    private String playerName;
    private int playerScore;
    private boolean onScoreboard;

    Player (String name) {
        playerName = name;
        playerScore = 0;
        onScoreboard = false;
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
