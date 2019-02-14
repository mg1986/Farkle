package com.mg1986.farkle.managers;

import com.mg1986.farkle.components.Player;
import com.mg1986.farkle.components.Scoreboard;
import com.mg1986.farkle.ui.MainMenu;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class ScoreboardManager {

    // File extension for saved games
    private static final String FILE_EXTENSION = ".farkle";

    //------------------------------------------------------------------------------------------------------------------
    // printScoreboard() - Prints player name and score for every player in player roster to the console.
    public void printScoreboard() {
        MenuManager.clearScreen();
        for (Player player : playerRoster) {
            String stringPadding = Integer.toString(50 - player.getName().length());
            String playerScore = Integer.toString(player.getPlayerScore());
            System.out.printf("%s %" + stringPadding + "s\n", player.getName(), playerScore);
        }
        MenuManager.pauseScreen();
    }

    //------------------------------------------------------------------------------------------------------------------
    // checkScoreboardAndBankPoints() - Checks to see if player of current turn is on scoreboard already, or they have
    //                                  scored >= 1,000 points on their current turn.  If they meet any of these criteria,
    //                                  they can end their turn at any time and bank their current point total.  If they
    //                                  haven't reached any of the criteria, they must keep rolling until they reach
    //                                  >= 1,000 points or Farkle to end their turn.
    public static boolean checkScoreboard(Player player, Scoreboard scoreboard) {

        MenuManager.clearScreen();
        boolean playersTurnStillActive = true;

        if (player.getOnScoreboard() || player.getTurnScore() >= scoreboard.MIN_SCOREBOARD_SCORE) {
            System.out.println(player.getName() + " ended their turn with " + player.getTurnScore() + " points " +
                    "added to the scoreboard!");
            playersTurnStillActive = false;
        } else {
            System.out.println("You can only end your turn after reaching 1,000 points to get on the \n" +
                    "scoreboard or already having points on the scoreboard.");
        }

        MenuManager.pauseScreen();
        return playersTurnStillActive;
    }

    public void updateScorebaord() {

    }

    //------------------------------------------------------------------------------------------------------------------
    // createScoreboard() - Creates a Scoreboard object and populates it with Player objects based off input from
    //                      console.
    public static Scoreboard createScoreboard() {

        int numPlayers = askHowManyPlayers();
        playerNames = new ArrayList<>();

        Scoreboard scoreboard = new Scoreboard(numPlayers);

        MenuManager.clearScreen();
        System.out.println("Enter the name for each player: \n");
        for (int i = 0; i < numPlayers; i++) {
            String name = checkName(scanner.next());
            Player player = new Player(name);
            scoreboard.playerRoster[i] = player;
        }

        return scoreboard;
    }

    //------------------------------------------------------------------------------------------------------------------
    // checkScoreboardAndBankPoints() - Checks to see if player of current turn is on scoreboard already, or they have
    //                                  scored >= 1,000 points on their current turn.  If they meet any of these criteria,
    //                                  they can end their turn at any time and bank their current point total.  If they
    //                                  haven't reached any of the criteria, they must keep rolling until they reach
    //                                  >= 1,000 points or Farkle to end their turn.
    public static boolean checkScoreboardAndBankPoints(Player player, Scoreboard scoreboard) {

        MenuManager.clearScreen();
        boolean playersTurnStillActive = true;

        if (player.getOnScoreboard() || player.getTurnScore() >= scoreboard.MIN_SCOREBOARD_SCORE) {
            System.out.println(player.getName() + " ended their turn with " + player.getTurnScore() + " points " +
                    "added to the scoreboard!");
            playersTurnStillActive = false;
        } else {
            System.out.println("You can only end your turn after reaching 1,000 points to get on the \n" +
                    "scoreboard or already having points on the scoreboard.");
        }

        MenuManager.pauseScreen();
        return playersTurnStillActive;
    }

    //------------------------------------------------------------------------------------------------------------------
    // checkName()
    //
    public static String checkName(String name) {

        playerNames.add(name);
        int nameCounter = Collections.frequency(playerNames, name);
        if (nameCounter > 1) {
            nameCounter = Collections.frequency(playerNames, name);
            name = name + "-" + Integer.toString(nameCounter);
        }

        return name;
    }


    //------------------------------------------------------------------------------------------------------------------
    // loadScoreboard - Takes a user provided .farkle file of a previous game, and turns it into a Scoreboard object
    //                  that is then used to resume the game saved in the file.
    public static Scoreboard loadScoreboard() {

        Scoreboard scoreboard = new Scoreboard();

        File savedGamesFolder = new File(GameManager.savedGamesDirectory);
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
                System.out.println("Pick a saved game file to load game.");

                for (int idx = 0; idx < listOfFiles.length; idx++) {
                    File file = listOfFiles[idx];
                    System.out.println((idx + 1) + ". " + file.getName());
                }

                try {

                    String menuOption = scanner.next();
                    int fileArrayIndex = Integer.parseInt(menuOption) - 1;
                    boolean inFileArray = (fileArrayIndex >= 0) && (fileArrayIndex < listOfFiles.length);

                    if (inFileArray) {
                        String loadFile = listOfFiles[fileArrayIndex].getName();
                        loadFile = GameManager.savedGamesDirectory + File.separatorChar + loadFile;
                        BufferedReader br = new BufferedReader(new FileReader(loadFile));
                        String line = br.readLine();
                        int idx = 0;
                        while (line != null) {
                            if (line.contains("Date_")) {
                                String date = line.split("_")[1];
                                clearScreen();
                                System.out.println("This game was last played: " + date);
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
                        loadScoreboard();
                    }
                } catch (NumberFormatException nfe) {
                    clearPrintPauseScreen("Press " + numFilesRange + " to proceed.");
                    loadScoreboard();
                } catch (FileNotFoundException fnfe) {
                    clearPrintPauseScreen("Saved game files does not exist. Press " + numFilesRange + " to proceed.");
                    loadScoreboard();
                } catch (IOException ioe) {
                    clearPrintPauseScreen("Problem loading game.");
                    loadScoreboard();
                }
            } else {
                clearPrintPauseScreen("There are currently no saved games to load.");
                MainMenu.mainMenu();
            }
        } else {
            clearPrintPauseScreen("There are currently no saved games to load.");
            MainMenu.mainMenu();
        }

        return scoreboard;
    }

    //------------------------------------------------------------------------------------------------------------------
    // saveScoreboard() - Takes the current games Scoreboard object and turns it into a .farkle file when a player
    //                    chooses to save the game
    public static void saveScoreboard(Scoreboard scoreboard) {

        clearScreen();
        System.out.println("Thank you for playing Farkle!");

        try {

            File savedGamesFolder = new File(GameManager.savedGamesDirectory);
            boolean exists = savedGamesFolder.exists();

            if (!exists) { savedGamesFolder.mkdirs(); }

            System.out.println("Enter a name for save game file: ");
            String saveFile = System.console().readLine() + FILE_EXTENSION;
            saveFile = GameManager.savedGamesDirectory + File.separatorChar + saveFile;
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
            saveScoreboard(scoreboard);
        } catch (IOException ioe) {
            clearPrintPauseScreen("Please enter a valid path and file name");
            saveScoreboard(scoreboard);
        }

        System.exit(0);
    }
}
