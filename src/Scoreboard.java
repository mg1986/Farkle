import java.io.*;
import java.lang.*;
import java.util.*;

/**
 * Created by matt on 10/16/17.
 */

public class Scoreboard extends MainMenu{

    public Player[] playerRoster;
    public static final int MIN_SCOREBOARD_SCORE = 1_000;
    public static final int MAX_SCORE = 10_000;
    private static final String SCOREBOARD_SEPARATOR = "                              ";

    Scoreboard () {}

    Scoreboard (int numberOfPlayers) { playerRoster = new Player[numberOfPlayers];}

    public void viewScoreboard() {
        MainMenu.clearScreen();
        for (Player player : playerRoster) {
            System.out.println(player.getName() + SCOREBOARD_SEPARATOR + player.getPlayerScore());
        }
        MainMenu.pauseScreen();
    }
}
