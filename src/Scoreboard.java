import java.io.*;
import java.lang.*;
import java.util.*;

/**
 * Created by matt on 10/16/17.
 */

public class Scoreboard {

    public Player[] roster;
    public static final int MIN_SCOREBOARD_SCORE = 1_000;
    public static final int MAX_SCORE = 10_000;
    private static final String FILE_EXTENSION = ".farkle";
    private static final String SCOREBOARD_SEPARATOR = "                              ";
    private static String savedGamesDirectory = System.getProperty("user.dir") + File.separatorChar + "saved_games";

    Scoreboard () { }

    Scoreboard (int numberOfPlayers) {
        Player[] roster = new Player[numberOfPlayers];
    }

    public void createRoster() {

        int numPlayers = 0;

        try {
            Farkle.menuPrint("Enter the number of players (must 2 or more players): ");
            numPlayers = Integer.parseInt(System.console().readLine());
            if (numPlayers < 2) {
                Farkle.menuPrint("Farkle requires a minimum or 2 players");
                createRoster();
            }
            roster = new Player[numPlayers];

            Farkle.menuPrint("Enter the name for each player: \n");

            for (int i = 0; i < roster.length; i++) {
                String name = System.console().readLine();
                Player player = new Player(name);
                roster[i] = player;
            }
        } catch (NumberFormatException nfe) {
            Farkle.menuPrint("Please enter the number of players");
            createRoster();
        }
    }

    public void loadRoster() {
        File folder = new File(savedGamesDirectory);
        File[] listOfFiles = folder.listFiles();
        int numOfFiles = listOfFiles.length;
        String numFilesRange = "";

        if (numOfFiles > 0) {
            try {

                if (numOfFiles == 1) {
                    numFilesRange = "1";
                } else if (numOfFiles > 1) {
                    numFilesRange = "1 - " + Integer.toString(numOfFiles);
                }

                Farkle.menuPrint("Pick a saved game file to load game.");

                for (int idx = 0; idx < listOfFiles.length; idx++) {
                    File file = listOfFiles[idx];
                    System.out.println((idx + 1) + ". " + file.getName());
                }

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
                            Farkle.menuPrint("This game was last played: " + date);
                        } else if (line.contains("Roster_")) {
                            int numPlayers = Integer.parseInt(line.split("_")[1]);
                            roster = new Player[numPlayers];
                        } else if (line.contains("Player_")) {
                            String name = line.split("_")[1];
                            int score = Integer.parseInt(line.split("_")[2]);
                            int numDiceInUse = Integer.parseInt(line.split("_")[3]);
                            boolean onScoreboard = false;
                            if (score > MIN_SCOREBOARD_SCORE) {
                                onScoreboard = true;
                            }
                            Player player = new Player(name, score, numDiceInUse, onScoreboard);
                            roster[idx] = player;
                            idx++;
                        }
                        line = br.readLine();
                    }
                } else {
                    Farkle.menuPrint("Press " + numFilesRange + " to proceed.");
                    loadRoster();
                }
            } catch (NumberFormatException nfe) {
                Farkle.menuPrint("Press " + numFilesRange + " to proceed.");
                loadRoster();
            } catch (FileNotFoundException fnfe) {
                System.out.println("Enter a valid path and file name");
                loadRoster();
            } catch (IOException ioe) {
                loadRoster();
            }
        } else {
            Farkle.menuPrint("There are currently no saved games to load.");
            Farkle.mainMenu();
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    public void saveRoster() {
        try {
            Farkle.menuPrint("Enter a name for save game file: ");
            String saveFile = System.console().readLine() + FILE_EXTENSION;
            saveFile = savedGamesDirectory + File.separatorChar + saveFile;
            BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile));
            bw.write("Date_" + (new Date().toString()) + "\n");
            bw.write("Roster_" + roster.length + "\n");

            int currentTurnIdx = 0;
            boolean currentTurn = false;

            for (int idx = 0; idx < roster.length; idx++) {
                Player player = roster[idx];
                if (player.getIsCurrentTurn() == true) {
                    currentTurn = true;
                    currentTurnIdx = idx;
                }

                if (currentTurn) {
                    bw.write("Player_" + player.getName() + "_" + player.getScore() + "_" + player.getNumDiceInUse() + "\n");
                }
            }

            for (int idx = 0; idx < currentTurnIdx; idx++) {
                Player player = roster[idx];
                bw.write("Player_" + player.getName() + "_" + player.getScore() + "_" + player.getNumDiceInUse() + "\n");
                break;
            }

            bw.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("Please enter a valid path and file name");
            saveRoster();
        } catch (IOException ioe) {
            System.out.println("Please enter a valid path and file name");
            saveRoster();
        }
    }

    public static void clearSavedGames() {
        File folder = new File(savedGamesDirectory);
        File[] listOfFiles = folder.listFiles();

        for (File file: listOfFiles) {
            file.delete();
        }
    }

    public void viewScoreboard() {
        System.out.println(Farkle.MENU_SEPARATOR);
        for (Player player : roster) {
            System.out.println(player.getName() + SCOREBOARD_SEPARATOR + player.getScore());
        }
    }
}
