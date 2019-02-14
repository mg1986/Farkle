package com.mg1986.farkle.ui;

import com.mg1986.farkle.managers.GameManager;
import com.mg1986.farkle.components.RuleBook;
import com.mg1986.farkle.components.Scoreboard;
import com.mg1986.farkle.managers.MenuManager;
import com.mg1986.farkle.managers.ScoreboardManager;

/**
 * MainMenu class - Starting menu for the game. Allows players to:
 *                      1. Start new game
 *                      2. Continue existing game
 *                      3. Delete saved games (if they exist)
 *                      4. View game rules
 *                      5. Exit game
 */

public class MainMenu extends BaseMenu {

    //------------------------------------------------------------------------------------------------------------------
    // mainMenu() - Main menu of the game. Allows options to start new game, load saved game, delete all
    //              saved games, view game rules, and exit menu.
    public static void mainMenu() {

        MenuManager.clearScreen();
        System.out.println("Welcome to Farkle. Pick from the following options: \n" +
                "1. New Game\n" +
                "2. Load Game\n" +
                "3. Clear Saved Games\n" +
                "4. View Rules\n" +
                "5. Exit");

        int menuOption = MenuManager.getMenuOptionInt(scanner);

        switch (menuOption) {
            case 1:  // New game
                Scoreboard newScoreboard = ScoreboardManager.createScoreboard();
                GameManager.startNewGame(newScoreboard);
                break;
            case 2: // Load game
                Scoreboard loadedScoreboard = ScoreboardManager.loadScoreboard();
                GameManager.startNewGame(loadedScoreboard);
                break;
            case 3:  // Delete saved games
                GameManager.deleteSavedGames();
                mainMenu();
                break;
            case 4: // View game rules
                RuleBook.viewRulebook();
                mainMenu();
                break;
            case 5:  // Exit game
                System.out.println("Thank you for playing Farkle!");
                System.exit(0);
            default:
                System.out.println("Please press 1-5 to proceed.");
                mainMenu();
                break;
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // askHowManyPlayers() - Requests user to enter an integer >= 2. Will not exit method until valid integer has been
    //                       properly parsed and confirmed to be >= 2.
    public static Integer askHowManyPlayers() {

        MenuManager.clearScreen();
        System.out.println("Enter the number of players (must 2 or more players): ");
        Integer numPlayers = MenuManager.getMenuOptionInt(scanner);

        if (numPlayers < 2) {
            System.out.println("Farkle requires a minimum of 2 players");
            numPlayers = askHowManyPlayers();
        }

        return numPlayers;
    }
}
