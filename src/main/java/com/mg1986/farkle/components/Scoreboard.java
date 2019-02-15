package com.mg1986.farkle.components;

import java.lang.*;
import java.util.List;
import static com.mg1986.farkle.controllers.MenuController.*;

/**
 * Scoreboard class - Scoreboard for the game. Keeps track of all players
 */

public class Scoreboard {

    // Roster that holds the player objects for the current game
    public Player[] playerRoster;

    // List that holds names to check when scoreboard is created
    public static List<String> playerNames;

    // Minumum point value from a single turn required to initially bank points to the scoreboard.
    public static final int MIN_SCOREBOARD_SCORE = 1_000;

    // Number of points required to win game
    public static final int MAX_SCORE = 10_000;

    // Scoreboard constructor - Takes the number of players the scoreboard will have and creates the player roster array
    public Scoreboard () {}

    // Scoreboard constructor - Takes the number of players the scoreboard will have and creates the player roster array
    public Scoreboard (int numberOfPlayers) { playerRoster = new Player[numberOfPlayers];}

    //------------------------------------------------------------------------------------------------------------------
    // printScoreboard() - Prints player name and score for every player in player roster to the console.
    public void printScoreboard() {
        clearScreen();
        for (Player player : playerRoster) {
            String stringPadding = Integer.toString(50 - player.getName().length());
            String playerScore = Integer.toString(player.getPlayerScore());
            System.out.printf("%s %" + stringPadding + "s\n", player.getName(), playerScore);
        }
        pauseScreen();
    }
}
