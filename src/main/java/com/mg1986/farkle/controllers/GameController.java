package com.mg1986.farkle.controllers;

import java.io.*;
import java.util.Date;
import com.mg1986.farkle.ui.TurnMenu;
import com.mg1986.farkle.model.Player;
import com.mg1986.farkle.model.Scoreboard;
import static com.mg1986.farkle.ui.MainMenu.mainMenu;
import static com.mg1986.farkle.controllers.MenuController.*;
import static com.mg1986.farkle.controllers.ScoreboardController.*;

/**
 * GameController class - Controls start/save/load game logic
 */

public class GameController {

    // Directory where games are saved too
   public static String savedGamesDirectory = System.getProperty("user.dir") + File.separatorChar + "saved_games";


    //------------------------------------------------------------------------------------------------------------------
    // startGame() - Called from the mainMenu() method.  Takes a Scoreboard object as input. Starts a new game or
    //               resumes existing game depending on state of input Scoreboard object. Once a player has reached
    //               10,000 points, that player is delclared the game winner, and then the startNewGame() method
    //               terminates ending the game.
    public static void startGame(Boolean newGame) {

        Scoreboard scoreboard;
        if (newGame) {
            scoreboard = createScoreboard();
        } else {
            scoreboard = loadGame();
        }

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
                    MenuController.clearScreen();
                    println(player.getName() + " won the game with a score of " +
                            String.format("%,d", player.getPlayerScore()) + " points!");
                    gameOver = true;
                }

                player.resetPlayerTurn();
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // loadGame - Takes a user provided .farkle file of a previous game, and turns it into a Scoreboard object that is
    //            then used to resume the game saved in the file.
    public static Scoreboard loadGame() {

        Scoreboard scoreboard = new Scoreboard();

        File savedGamesFolder = new File(savedGamesDirectory);
        boolean exists = savedGamesFolder.exists();

        if (exists) {
            File[] listOfFiles = savedGamesFolder.listFiles();
            int numOfFiles = listOfFiles.length;
            String numFilesRange = "";

            if (numOfFiles > 0) {
                if (numOfFiles > 1) {
                    numFilesRange = "1 - " + Integer.toString(numOfFiles);
                } else {
                    numFilesRange = "1";
                }

                clearScreen();
                println("Pick a saved game file to load game.");

                for (int idx = 0; idx < listOfFiles.length; idx++) {
                    File file = listOfFiles[idx];
                    println((idx + 1) + ". " + file.getName());
                }

                try {

                    String menuOption = scanner.next();
                    int fileArrayIndex = Integer.parseInt(menuOption) - 1;
                    boolean inFileArray = (fileArrayIndex >= 0) && (fileArrayIndex < listOfFiles.length);

                    if (inFileArray) {
                        String loadFile = listOfFiles[fileArrayIndex].getName();
                        loadFile = savedGamesDirectory + File.separatorChar + loadFile;
                        BufferedReader br = new BufferedReader(new FileReader(loadFile));
                        String line = br.readLine();
                        int idx = 0;
                        while (line != null) {
                            if (line.contains("Date_")) {
                                String date = line.split("_")[1];
                                clearScreen();
                                println("This game was last played: " + date);
                                pauseScreen();
                            } else if (line.contains("Roster_")) {
                                int numPlayers = Integer.parseInt(line.split("_")[1]);
                                scoreboard.playerRoster = new Player[numPlayers];
                            } else if (line.contains("Player_")) {
                                String name = line.split("_")[1];
                                int score = Integer.parseInt(line.split("_")[2]);
                                int numDiceInUse = Integer.parseInt(line.split("_")[3]);
                                boolean onScoreboard = false;
                                if (score > scoreboard.MIN_SCOREBOARD_SCORE) {
                                    onScoreboard = true;
                                }
                                Player player = new Player(name, score, numDiceInUse, onScoreboard);
                                scoreboard.playerRoster[idx] = player;
                                idx++;
                            }

                            line = br.readLine();
                        }

                    } else {
                        clearPrintPauseScreen("Press " + numFilesRange + " to proceed.");
                        loadGame();
                    }
                } catch (NumberFormatException nfe) {
                    clearPrintPauseScreen("Press " + numFilesRange + " to proceed.");
                    loadGame();
                } catch (FileNotFoundException fnfe) {
                    clearPrintPauseScreen("Saved game files does not exist. Press " + numFilesRange + " to proceed.");
                    loadGame();
                } catch (IOException ioe) {
                    clearPrintPauseScreen("Problem loading game.");
                    loadGame();
                }
            } else {
                clearPrintPauseScreen("There are currently no saved games to load.");
                mainMenu();
            }
        } else {
            clearPrintPauseScreen("There are currently no saved games to load.");
            mainMenu();
        }

        return scoreboard;
    }

    //------------------------------------------------------------------------------------------------------------------
    // saveGame() - Takes the current games Scoreboard object and turns it into a .farkle file when a player
    //                chooses to save the game
    public static void saveGame(Scoreboard scoreboard) {

        clearScreen();
        println("Thank you for playing Farkle!");

        try {

            File savedGamesFolder = new File(savedGamesDirectory);
            boolean exists = savedGamesFolder.exists();

            if (!exists) { savedGamesFolder.mkdirs(); }

            println("Enter a name for save game file: ");
            String saveFile = scanner.next() + FILE_EXTENSION;
            saveFile = savedGamesDirectory + File.separatorChar + saveFile;
            BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile));
            bw.write("Date_" + (new Date().toString()) + "\n");
            bw.write("Roster_" + scoreboard.playerRoster.length + "\n");

            int currentTurnIdx = 0;
            boolean currentTurn = false;

            for (int idx = 0; idx < scoreboard.playerRoster.length; idx++) {
                Player player = scoreboard.playerRoster[idx];
                if (player.getIsCurrentTurn() == true) {
                    currentTurn = true;
                    currentTurnIdx = idx;
                }

                if (currentTurn) {
                    bw.write("Player_" + player.getName() + "_" + player.getPlayerScore() + "_" +
                            player.getNumDiceInUse() + "\n");
                }
            }

            for (int idx = 0; idx < currentTurnIdx; idx++) {
                Player player = scoreboard.playerRoster[idx];
                bw.write("Player_" + player.getName() + "_" + player.getPlayerScore() + "_" +
                        player.getNumDiceInUse() + "\n");
                break;
            }

            bw.close();
        } catch (FileNotFoundException fnfe) {
            clearPrintPauseScreen("Please enter a valid path and file name");
            saveGame(scoreboard);
        } catch (IOException ioe) {
            clearPrintPauseScreen("Please enter a valid path and file name");
            saveGame(scoreboard);
        }

        mainMenu();
    }

    //------------------------------------------------------------------------------------------------------------------
    // deleteSavedGames() - Deletes all .farkle files saved in the savedGamesDirectory, if it exists.
    public static void deleteSavedGames() {
        clearScreen();
        clearPrintPauseScreen("Deleting saved games....");
        File folder = new File(savedGamesDirectory);
        File[] listOfFiles = folder.listFiles();

        try {
            for (File file: listOfFiles) {
                file.delete();
            }
            println("Saved games deleted");
        } catch (NullPointerException npe) {
           clearPrintPauseScreen("There are no saved games to delete.");
        }
    }
}
