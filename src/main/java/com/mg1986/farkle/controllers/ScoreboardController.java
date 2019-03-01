package com.mg1986.farkle.controllers;

import java.util.ArrayList;
import java.util.Collections;
import com.mg1986.farkle.model.Player;
import com.mg1986.farkle.model.Scoreboard;
import static com.mg1986.farkle.controllers.MenuController.*;

/**
 * ScoreboardController class - Controls Scoreboard logic
 */

public class ScoreboardController {

    // File extension for saved games
    public static final String FILE_EXTENSION = ".farkle";

    //------------------------------------------------------------------------------------------------------------------
    // printScoreboard() - Prints player name and score for every player in player roster to the console.
    public static void printScoreboard(Scoreboard scoreboard) {
        clearScreen();
        for (Player player : scoreboard.playerRoster) {
            String stringPadding = Integer.toString(50 - player.getName().length());
            String playerScore = Integer.toString(player.getPlayerScore());
            System.out.printf("%s %" + stringPadding + "s\n", player.getName(), playerScore);
        }
        pauseScreen();
    }

    //------------------------------------------------------------------------------------------------------------------
    // checkScoreboardAndBankPoints() - Checks to see if player of current turn is on scoreboard already, or they have
    //                                  scored >= 1,000 points on their current turn.  If they meet any of these criteria,
    //                                  they can end their turn at any time and bank their current point total.  If they
    //                                  haven't reached any of the criteria, they must keep rolling until they reach
    //                                  >= 1,000 points or farkle to end their turn.
    public static boolean checkScoreboardAndBankPoints(Player player, Scoreboard scoreboard) {
        println(scoreboard.toString());
        clearScreen();
        boolean playersTurnStillActive = true;

        if (player.getOnScoreboard() || player.getTurnScore() >= scoreboard.MIN_SCOREBOARD_SCORE) {
            println(player.getName() + " ended their turn with " + player.getTurnScore() + " points " +
                    "added to the scoreboard!");
            playersTurnStillActive = false;
        } else {
            println("You can only end your turn after reaching 1,000 points to get on the \n" +
                    "scoreboard or already having points on the scoreboard.");
        }

        pauseScreen();
        return playersTurnStillActive;
    }

    //------------------------------------------------------------------------------------------------------------------
    // askHowManyPlayers() - Requests user to enter an integer >= 2. Will not exit method until valid integer has been
    //                       properly parsed and confirmed to be >= 2.
    public static Integer askHowManyPlayers() {

        clearScreen();
        println("Enter the number of players (must 2 or more players): ");
        Integer numPlayers = getMenuOptionInt();

        if (numPlayers < 2) {
            println("Farkle requires a minimum of 2 players");
            numPlayers = askHowManyPlayers();
        }

        return numPlayers;
    }

    //------------------------------------------------------------------------------------------------------------------
    // createScoreboard() - Creates a Scoreboard object and populates it with Player objects based off input from
    //                      console.
    public static Scoreboard createScoreboard() {

        int numPlayers = askHowManyPlayers();
        ArrayList<String> playerNames = new ArrayList<>();

        Scoreboard scoreboard = new Scoreboard(numPlayers);

        clearScreen();
        println("Enter the name for each player: \n");
        for (int i = 0; i < numPlayers; i++) {
            String name = checkNameInRoster(playerNames, scanner.next());
            Player player = new Player(name);
            scoreboard.playerRoster[i] = player;
        }


        return scoreboard;
    }

    //------------------------------------------------------------------------------------------------------------------
    // checkNameInRoster() - Used in name validation if sames names entered for same game
    //
    public static String checkNameInRoster(ArrayList<String> playerNames, String name) {

        playerNames.add(name);
        int nameCounter = Collections.frequency(playerNames, name);
        if (nameCounter > 1) {
            nameCounter = Collections.frequency(playerNames, name);
            name = name + "-" + nameCounter;
        }

        return name;
    }
}
