import java.io.*;
import java.lang.*;
import java.util.*;

/**
 * MainMenu class - Starting menu for the game. Allows players to:
 *                      1. Start new game
 *                      2. Continue existing game
 *                      3. Delete saved games (if they exist)
 *                      4. View game rules
 *                      5. Exit game
 */

public class MainMenu {

    // Used to visually separate text in menuPrint() method
    public static final String MENU_SEPARATOR = "---------------------------------------------------------------------";

    // File extension for saved games
    private static final String FILE_EXTENSION = ".farkle";

    // Directory where games are saved too
    private static String savedGamesDirectory = System.getProperty("user.dir") + File.separatorChar + "saved_games";

    //------------------------------------------------------------------------------------------------------------------
    // main() - Calls the mainMenu() method to begin the game.
    public static void main(String[] args) {
        mainMenu();
    }

    //------------------------------------------------------------------------------------------------------------------
    // mainMenu() - Main menu of the game. Allows options to start new game, load previous game, delete all
    //                     saved games, view game rules, and exit menu.
    public static void mainMenu() {

        menuPrint("Welcome to Farkle. Pick from the following options: \n" +
                "1. New Game\n" +
                "2. Load Game\n" +
                "3. Clear Saved Games\n" +
                "4. View Rules\n" +
                "5. Exit");

        String menuOption = System.console().readLine();

        if (menuOption.equals("1")) { // New game
            Scoreboard scoreboard = createScoreboard();
            newGame(scoreboard);
        } else if (menuOption.equals("2")) { // Load game
            Scoreboard scoreboard = loadScoreboard();
            newGame(scoreboard);
        } else if (menuOption.equals("3")) { // Delete saved games
            deleteSavedGames();
            mainMenu();
        }else if (menuOption.equals("4")) { // View game rules
            menuPrint(RuleBook.getGameRules());
            mainMenu();
        } else if (menuOption.equals("5")) { // Exit game
            menuPrint("Thank you for playing Farkle!");
            System.exit(0);
        } else {
            menuPrint("Please press 1-5 to proceed.");
            mainMenu();
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // newGame() - Called from the mainMenu() method.  Takes a Scoreboard object as input. Starts a new game or
    //             resumes existing game depending on state of input Scoreboard object. Once a player has reached
    //             10,000 points, that player is delclared the game winner, and then the newGame() method
    //             terminates ending the game.
    private static void newGame(Scoreboard scoreboard) {

        boolean gameOver = false;
        Dice dice = new Dice(6);

        while (!gameOver) {
            for (Player player : scoreboard.playerRoster) {
                player.setIsCurrentTurn(true);
                PlayerTurn.startPlayerTurn(scoreboard, player, dice);
                if (player.getTurnScore() >= scoreboard.MIN_SCOREBOARD_SCORE) {
                    player.setOnScoreboard(true);
                }

                int turnScore = player.getTurnScore();
                player.setPlayerScore(turnScore);

                if (player.getPlayerScore() >= scoreboard.MAX_SCORE) {
                    gameOver = true;
                    menuPrint(player.getName() + " won the game with a score of " +
                            String.format("%,d", player.getPlayerScore()) + " points!");
                }
                player.resetTurnScore();
                player.resetNumDiceInUse();
                player.setIsCurrentTurn(false);
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // createScoreboard() - Creates a Scoreboard object and populates it with Player objects based off input from
    //                      console.
    public static Scoreboard createScoreboard() {

        int numPlayers = 0;

        menuPrint("Enter the number of players (must 2 or more players): ");

        try {
            numPlayers = Integer.parseInt(System.console().readLine());
        } catch (NumberFormatException nfe) {
            menuPrint("Please enter the number of players");
            createScoreboard();
        }

        if (numPlayers < 2) {
            menuPrint("Farkle requires a minimum of 2 players");
            createScoreboard();
        }

        Scoreboard scoreboard = new Scoreboard(numPlayers);

        menuPrint("Enter the name for each player: \n");

        for (int i = 0; i < numPlayers; i++) {
            String name = System.console().readLine();
            Player player = new Player(name);
            scoreboard.playerRoster[i] = player;
        }

        return scoreboard;
    }

    //------------------------------------------------------------------------------------------------------------------
    // loadScoreboard - Takes a user provided .farkle file of a previous game, and turns it into a Scoreboard object
    //                  that is then used to resume the game saved in the file.
    public static Scoreboard loadScoreboard() {

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

                menuPrint("Pick a saved game file to load game.");

                for (int idx = 0; idx < listOfFiles.length; idx++) {
                    File file = listOfFiles[idx];
                    System.out.println((idx + 1) + ". " + file.getName());
                }

                try {

                    String menuOption = System.console().readLine();
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
                                menuPrint("This game was last played: " + date);
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
                        menuPrint("Press " + numFilesRange + " to proceed.");
                        loadScoreboard();
                    }
                } catch (NumberFormatException nfe) {
                    menuPrint("Press " + numFilesRange + " to proceed.");
                    loadScoreboard();
                } catch (FileNotFoundException fnfe) {
                    menuPrint("Saved game files does not exist. Press " + numFilesRange + " to proceed.");
                    loadScoreboard();
                } catch (IOException ioe) {
                    menuPrint("Problem loading game.");
                    loadScoreboard();
                }
            } else {
                menuPrint("There are currently no saved games to load.");
                mainMenu();
            }
        } else {
            menuPrint("There are currently no saved games to load.");
            mainMenu();
        }

        return scoreboard;
    }

    //------------------------------------------------------------------------------------------------------------------
    // saveScoreboard() - Takes the current games Scoreboard object and turns it into a .farkle file when a player
    //                    chooses to save the game
    public static void saveScoreboard(Scoreboard scoreboard) {
        try {

            File savedGamesFolder = new File(savedGamesDirectory);
            boolean exists = savedGamesFolder.exists();

            if (!exists) { savedGamesFolder.mkdirs(); }

            menuPrint("Enter a name for save game file: ");
            String saveFile = System.console().readLine() + FILE_EXTENSION;
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
            menuPrint("Please enter a valid path and file name");
            saveScoreboard(scoreboard);
        } catch (IOException ioe) {
            menuPrint("Please enter a valid path and file name");
            saveScoreboard(scoreboard);
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // deleteSavedGames() - Deletes all .farkle files saved in the savedGamesDirectory, if one exists.
    public static void deleteSavedGames() {
        menuPrint("Deleting saved games....");
        File folder = new File(savedGamesDirectory);
        File[] listOfFiles = folder.listFiles();

        for (File file: listOfFiles) {
            file.delete();
        }

        menuPrint("Saved games deleted");
    }

    //------------------------------------------------------------------------------------------------------------------
    // menuPrint() - Prints input String to console with a visual line separator
    public static void menuPrint(String s) {
        System.out.println(MENU_SEPARATOR);
        System.out.println(s);
    }
}
