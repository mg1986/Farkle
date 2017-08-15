import java.util.Scanner;

/**
 * Farkle - Dice game where the goal is to reach 10,000 points
 * Created by matt on 8/3/17.
 */

public class Farkle {
    public static void main (String[] args) {
        mainMenu();
    }

    int MAX_SCORE = 10_000;
    static String LINE = "--------------------------";

    public static void mainMenu() {
        System.out.println(LINE);
        System.out.println("Welcome to Farkle. Pick one of the " +
                "following menu options: \n1. New Game\n2. Load " +
                "Game\n3. View Rules\n4. Exit");
        System.out.println(LINE);

        Scanner sc = new Scanner(System.in);
        int menuOption = sc.nextInt();

        if (menuOption == 1) {
            //Player[] roster = createRoster();
            newGame(createRoster());
        } else if (menuOption == 2) {
            // Player[] roster = loadRoster();
            // newGame(roster);
        } else if (menuOption == 3) {
            // displayRules();
        } else if (menuOption == 4) {
            System.out.println("Thank you for playing Farkle!");
            System.exit(0);
        }
    }

    public static void newGame(Player[] roster) {

        boolean gameOver = false;

        int counter = 0;

        while (gameOver == false) {
            for (Player player:roster) {
                // Logic for player turn
            }
        }
    }

    public static void displayRules() {

    }

    public static Player[] createRoster() {

        System.out.println("Enter the number of players\n");

        Scanner sc = new Scanner(System.in);
        int numPlayers = sc.nextInt();

        Player[] roster = new Player[numPlayers];

        System.out.println("Enter name for each player\n");

        for (int i=0; i<numPlayers; i++) {

            String name = sc.nextLine();
            Player p = new Player(name);
            roster[i] = p;
        }

        return roster;
    }

    /*public static Player[] loadRoster() {


    }*/
}