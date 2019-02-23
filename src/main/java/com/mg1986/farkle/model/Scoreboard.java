package com.mg1986.farkle.model;

import java.util.List;

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

}
