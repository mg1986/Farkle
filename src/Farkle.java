import java.util.Scanner;

/**
 * Farkle - Dice game where the goal is to reach 10,000 points
 * Created by matt on 8/3/17.
 */

public class Farkle {
    public static void main (String[] args) {
        run();
    }

    int MAX_SCORE = 10_000;

    public static void run() {
        System.out.println("Welcome to Farkle. Pick one of the " +
                "following menu options: \n1. New Game\n2. Load " +
                "Game\n3. View Rules\n4. Exit");
        System.out.println();

        Scanner sc = new Scanner(System.in);
        int menuOption = sc.nextInt();

        if (menuOption == 1) {
            newGame();
        } else if (menuOption == 2) {
            // loadGame();
        } else if (menuOption == 3) {
            // displayRules();
        } else if (menuOption == 4) {
            System.out.println("Thank you for playing Farkle!");
            System.exit(0);
        }
    }

    public static void newGame() {

        Scanner sc = new Scanner(System.in);
        int numPlayers = sc.nextInt();

        System.out.println("Enter name for each player\n");

        for (int i=0; i<=numPlayers; i++) {

            Player p = new Player("");


        }
    }

    public static void loadGame() {

    }

    public static void displayRules() {

    }

}

