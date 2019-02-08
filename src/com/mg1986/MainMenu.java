import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.stream.IntStream;

/**
 * MainMenu class - Starting menu for the game. Allows players to:
 *                      1. Start new game
 *                      2. Continue existing game
 *                      3. Delete saved games (if they exist)
 *                      4. View game rules
 *                      5. Exit game
 */

public class MainMenu {

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
    // mainMenu() - Main menu of the game. Allows options to start new game, load saved game, delete all
    //              saved games, view game rules, and exit menu.
    public static void mainMenu() {

        clearScreen();
        System.out.println("Welcome to Farkle. Pick from the following options: \n" +
                "1. New Game\n" +
                "2. Load Game\n" +
                "3. Clear Saved Games\n" +
                "4. View Rules\n" +
                "5. Exit");

        int menuOption = getMenuOptionInt();

        switch (menuOption) {
            case 1:  // New game
                Scoreboard newScoreboard = createScoreboard();
                newGame(newScoreboard);
                break;
            case 2: // Load game
                Scoreboard loadedScoreboard = loadScoreboard();
                newGame(loadedScoreboard);
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
                System.out.println("Thank you for playing Farkle!");
                System.exit(0);
            default:
                System.out.println("Please press 1-5 to proceed.");
                mainMenu();
                break;
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // newGame() - Called from the mainMenu() method.  Takes a Scoreboard object as input. Starts a new game or
    //             resumes existing game depending on state of input Scoreboard object. Once a player has reached
    //             10,000 points, that player is delclared the game winner, and then the newGame() method
    //             terminates ending the game.
    private static void newGame(Scoreboard scoreboard) {

        boolean gameOver = false;

        while (!gameOver) {
            for (Player player : scoreboard.playerRoster) {
                player.setIsCurrentTurn(true);
                PlayerMenu.startPlayerTurn(scoreboard, player);
                int turnScore = player.getTurnScore();

                if (player.getTurnScore() >= scoreboard.MIN_SCOREBOARD_SCORE) {
                    player.setOnScoreboard(true);
                }

                player.setPlayerScore(turnScore);

                if (player.getPlayerScore() >= scoreboard.MAX_SCORE) {
                    clearScreen();
                    System.out.println(player.getName() + " won the game with a score of " +
                            String.format("%,d", player.getPlayerScore()) + " points!");
                    gameOver = true;
                }

                player.resetPlayerTurn();
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // createScoreboard() - Creates a Scoreboard object and populates it with Player objects based off input from
    //                      console.
    public static Scoreboard createScoreboard() {

        int numPlayers = askHowManyPlayers();

        Scoreboard scoreboard = new Scoreboard(numPlayers);

        clearScreen();
        System.out.println("Enter the name for each player: \n");
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

                clearScreen();
                System.out.println("Pick a saved game file to load game.");

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
                mainMenu();
            }
        } else {
            clearPrintPauseScreen("There are currently no saved games to load.");
            mainMenu();
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

            File savedGamesFolder = new File(savedGamesDirectory);
            boolean exists = savedGamesFolder.exists();

            if (!exists) { savedGamesFolder.mkdirs(); }

            System.out.println("Enter a name for save game file: ");
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
            clearPrintPauseScreen("Please enter a valid path and file name");
            saveScoreboard(scoreboard);
        } catch (IOException ioe) {
            clearPrintPauseScreen("Please enter a valid path and file name");
            saveScoreboard(scoreboard);
        }

        System.exit(0);
    }

    //------------------------------------------------------------------------------------------------------------------
    // deleteSavedGames() - Deletes all .farkle files saved in the savedGamesDirectory, if it exists.
    public static void deleteSavedGames() {
        clearScreen();
        System.out.println("Deleting saved games....");
        File folder = new File(savedGamesDirectory);
        File[] listOfFiles = folder.listFiles();

        try {
            for (File file: listOfFiles) { file.delete(); }
            System.out.println("Saved games deleted");
        } catch (NullPointerException npe) {
            clearPrintPauseScreen("There are no saved games to delete.");
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // getMenuOptionInt() - Requests user to enter an integer and will not return until user input has been properly
    //                      parsed.
    public static int getMenuOptionInt() {

        // Scanner for menu input
        Scanner menuScanner = new Scanner(System.in);

        int menuOption = 0;

        try {
            menuOption = Integer.parseInt(menuScanner.next());
        } catch (NumberFormatException  ime) {
            System.out.println("Please enter a valid number and press ENTER to continue");
            menuOption = getMenuOptionInt();
        }

        return menuOption;
    }

    //------------------------------------------------------------------------------------------------------------------
    // askHowManyPlayers() - Requests user to enter an integer >= 2. Will not exit method until valid integer has been
    //                       properly parsed and confirmed to be >= 2.
    public static Integer askHowManyPlayers() {

        clearScreen();
        System.out.println("Enter the number of players (must 2 or more players): ");
        Integer numPlayers = getMenuOptionInt();

        if (numPlayers < 2) {
            System.out.println("Farkle requires a minimum of 2 players");
            numPlayers = askHowManyPlayers();
        }

        return numPlayers;
    }

    //------------------------------------------------------------------------------------------------------------------
    // clearScreen() - Simulates clearing the screen of console by printing 50 blank lines.
    public static void clearScreen() {
        IntStream.range(0, 50).forEach(n -> { System.out.println(); });
    }

    //------------------------------------------------------------------------------------------------------------------
    // pauseScreen() - Displays a message and then waits for user to press ENTER key to proceed with game.
    public static void pauseScreen() {
        System.out.println("Press ENTER to continue...");
        String pauseScreen = System.console().readLine();
    }

    //------------------------------------------------------------------------------------------------------------------
    // clearPrintPauseScreen() - Clears screen, prints a message, and teh waits for user to press ENTER to proceed with
    //                           game.
    public static void clearPrintPauseScreen(String messageToPrint) {
        clearScreen();
        System.out.println(messageToPrint);
        pauseScreen();
    }
}
