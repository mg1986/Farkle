import java.util.Scanner;

/**
 * Farkle - Dice game where the goal is to reach 10,000 points
 * Created by matt on 8/3/17.
 */

public class Farkle {

    static int MAX_SCORE = 10_000;
    static String LINE = "----------------------------" +
            "----------------------------";

    public static void main (String[] args) {
        mainMenu();
    }

    public static void mainMenu() {
        menuPrint("Welcome to Farkle. Pick from the " +
                "following menu options: \n" +
                "1. New Game\n" +
                "2. Load Game\n" +
                "3. View Rules\n" +
                "4. Exit");

        Scanner sc = new Scanner(System.in);

        while (true) {
            try {
                int menuOption = sc.nextInt();

                if (menuOption == 1) {
                    Player[] roster = createRoster();
                    newGame(roster);
                } else if (menuOption == 2) {
                    // Player[] roster = loadRoster();
                    // newGame(roster);
                } else if (menuOption == 3) {
                    // displayRules();
                } else if (menuOption == 4) {
                    menuPrint("Thank you for playing Farkle!");
                    System.exit(0);
                }
                break;
            } catch (Exception e) {
                menuPrint("Please press 1-4 to proceed.\n");
                mainMenu();
            }
        }
    }

    public static void newGame(Player[] roster) {

        boolean gameOver = false;

        while (!gameOver) {
            for (Player player:roster) {
                int score = playerTurn();
                player.setScore(score);

                if (player.getScore() > MAX_SCORE) {
                    gameOver = true;
                    menuPrint(player.getName() + " won the game" +
                    " with a score of " + player.getScore());
                }
            }
        }
    }

    public static Player[] createRoster() {

        menuPrint("Enter the number of players\n");

        Scanner sc = new Scanner(System.in);
        int numPlayers = sc.nextInt();

        Player[] roster = new Player[numPlayers];

        menuPrint("Enter the name for each player\n");

        for (int i=0; i<=numPlayers; i++) {

            String name = sc.nextLine();
            Player p = new Player(name);
            roster[i] = p;
        }

        return roster;
    }

    public static int playerTurn() {

        int score = 0;
        boolean endOfTurn = false;

        while (!endOfTurn) {
            menuPrint("1. Roll dice\n" +
                    "2. End turn" +
                    "3. View Rules\n" +
                    "4. View Scoreboard\n" +
                    "5. Save and exit game");
        }

        return score;
    }

    public static void menuPrint(String s) {
        System.out.println(LINE);
        System.out.println(s);
    }
}