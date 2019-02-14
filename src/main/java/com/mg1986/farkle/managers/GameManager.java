package com.mg1986.farkle.managers;

import com.mg1986.farkle.components.Player;
import com.mg1986.farkle.components.Scoreboard;
import com.mg1986.farkle.ui.TurnMenu;

import java.io.*;

public class GameManager {

    // Directory where games are saved too
   public  static String savedGamesDirectory = System.getProperty("user.dir") + File.separatorChar + "saved_games";

    //------------------------------------------------------------------------------------------------------------------
    // startNewGame() - Called from the mainMenu() method.  Takes a Scoreboard object as input. Starts a new game or
    //             resumes existing game depending on state of input Scoreboard object. Once a player has reached
    //             10,000 points, that player is delclared the game winner, and then the startNewGame() method
    //             terminates ending the game.
    public static void startNewGame(Scoreboard scoreboard) {

        boolean gameOver = false;

        while (!gameOver) {
            for (Player player : scoreboard.playerRoster) {
                player.setIsCurrentTurn(true);
                TurnMenu.startPlayerTurn(scoreboard, player);
                int turnScore = player.getTurnScore();

                if (player.getTurnScore() >= scoreboard.MIN_SCOREBOARD_SCORE) {
                    player.setOnScoreboard(true);
                }

                player.setPlayerScore(turnScore);

                if (player.getPlayerScore() >= scoreboard.MAX_SCORE) {
                    MenuManager.clearScreen();
                    System.out.println(player.getName() + " won the game with a score of " +
                            String.format("%,d", player.getPlayerScore()) + " points!");
                    gameOver = true;
                }

                player.resetPlayerTurn();
            }
        }
    }


    //------------------------------------------------------------------------------------------------------------------
    // deleteSavedGames() - Deletes all .farkle files saved in the savedGamesDirectory, if it exists.
    public static void deleteSavedGames() {
        MenuManager.clearScreen();
        System.out.println("Deleting saved games....");
        File folder = new File(savedGamesDirectory);
        File[] listOfFiles = folder.listFiles();

        try {
            for (File file: listOfFiles) {
                file.delete();
            }
            System.out.println("Saved games deleted");
        } catch (NullPointerException npe) {
            MenuManager.clearPrintPauseScreen("There are no saved games to delete.");
        }
    }
}
