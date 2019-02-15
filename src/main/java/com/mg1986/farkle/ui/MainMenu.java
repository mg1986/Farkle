package com.mg1986.farkle.ui;

import static com.mg1986.farkle.controllers.GameController.*;
import com.mg1986.farkle.components.RuleBook;
import com.mg1986.farkle.components.Scoreboard;
import static com.mg1986.farkle.controllers.MenuController.*;
import com.mg1986.farkle.controllers.ScoreboardController;

/**
 * MainMenu class - Starting menu for the game. Allows players to:
 *                      1. Start new game
 *                      2. Continue existing game
 *                      3. Delete saved games (if they exist)
 *                      4. View game rules
 *                      5. Exit game
 */

public class MainMenu {

    //------------------------------------------------------------------------------------------------------------------
    // mainMenu() - Main menu of the game. Allows options to start new game, load saved game, delete all
    //              saved games, view game rules, and exit menu.
    public static void mainMenu() {

        clearScreen();
        println("Welcome to Farkle. Pick from the following options: \n" +
                "1. New Game\n" +
                "2. Load Game\n" +
                "3. Clear Saved Games\n" +
                "4. View Rules\n" +
                "5. Exit");

        int menuOption = getMenuOptionInt();

        switch (menuOption) {
            case 1:  // New game
                startGame(true);
                break;
            case 2: // Load game
                startGame(false);
                break;
            case 3:  // Delete saved games
                deleteSavedGames();
                mainMenu();
                break;
            case 4: // View game rules
                RuleBook.viewRulebook();
                mainMenu();
                break;
            case 5:  // Exit game
                println("Thank you for playing Farkle!");
                System.exit(0);
            default:
                println("Please press 1-5 to proceed.");
                mainMenu();
                break;
        }
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
}
