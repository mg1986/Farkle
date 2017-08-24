import java.util.*;

/**
 * Farkle - Dice game where the goal is to reach 10,000 points
 * Created by matt on 8/3/17.
 */

public class Farkle {

    private static final int MAX_NUM_DICE = 6;
    private static final int MAX_SCORE = 10_000;
    private static final String SCOREBOARD_SEPARATOR = "             ";
    private static final String MENU_SEPARATOR = "------------------------------------" +
                                                 "------------------------------------";

    public static void main(String[] args) {

        mainMenu();
    }

    private static void mainMenu() {

        menuPrint("Welcome to Farkle. Pick from the following options: \n" +
                "1. New Game\n" +
                "2. Load Game\n" +
                "3. View Rules\n" +
                "4. Exit");

        String menuOption = System.console().readLine();

        if (menuOption.equals("1")) {
            Player[] roster = createRoster();
            newGame(roster);
        } else if (menuOption.equals("2")) {
            // loadGame()
        } else if (menuOption.equals("3")) {
            // displayRules();
        } else if (menuOption.equals("4")) {
            menuPrint("Thank you for playing Farkle!");
            System.exit(0);
        } else {
            menuPrint("Please press 1-4 to proceed.");
            mainMenu();
        }
    }

    private static Player[] createRoster() {

        int numPlayers = 0;

        try {
            menuPrint("Enter the number of players (must be >= 2): ");
            numPlayers = Integer.parseInt(System.console().readLine());
            if (numPlayers < 2) {
                menuPrint("Farkle requires a minimum or 2 players");
                createRoster();
            }
        } catch (Exception e) {
            menuPrint("Please enter the number of players");
            createRoster();
        }

        Player[] roster = new Player[numPlayers];

        menuPrint("Enter the name for each player: \n");

        for (int i = 0; i < roster.length; i++) {
            String name = System.console().readLine();
            Player p = new Player(name);
            roster[i] = p;
        }

        return roster;
    }

    private static void newGame(Player[] roster) {

        boolean gameOver = false;
        Dice dice = new Dice(6);

        while (!gameOver) {
            for (Player player : roster) {
                int score = playerTurn(roster, player, dice);
                if (score >= 1000) {
                    player.onScoreboard = true;
                }

                player.setScore(score);

                if (player.getScore() >= MAX_SCORE) {
                    gameOver = true;
                    menuPrint(player.getName() + " won the game" +
                            " with a score of " + String.format("%,d", player.getScore()) + " points");
                }
            }
        }
    }

    private static int playerTurn(Player[] roster, Player player, Dice dice) {

        int score = 0;
        int numDiceUsed = 0;
        int[] diceOnHand = new int[6];
        boolean endOfTurn = false;

        while (!endOfTurn) {

            menuPrint("It is " + player.getName() + "'s turn\n" +
                    player.getName() + "'s current score for this turn is " + score);

            menuPrint("1. Roll dice\n" +
                    "2. Bank points and end turn\n" +
                    "3. View Scoreboard\n" +
                    "4. Save and exit game");

            String menuOption = System.console().readLine();

            if (menuOption.equals("1")) {
                int[] roll = rollDice(dice, MAX_NUM_DICE - numDiceUsed);
                System.out.println(Arrays.toString(roll));
                score = score + analyzeDiceRoll(player, score, roll, dice, numDiceUsed);
            } else if (menuOption.equals("2")) {
                if (player.onScoreboard || player.getScore() >= 1000) {
                    return score;
                } else {
                    menuPrint("You can only end your turn after reaching " +
                            "1,000 points to get on the board.");
                }
            } else if (menuOption.equals("3")) {
                displayScoreboard(roster);
            } else if (menuOption.equals("4")) {
                // saveGame();
                menuPrint("Thank you for playing Farkle!");
                System.exit(0);
            } else {
                menuPrint("Please press 1-4 to proceed with turn.");
            }
        }

        return score; // Return player score for the turn
    }

    private static int[] rollDice(Dice dice, int numDice) {

        int[] roll = new int[numDice];

        for (int i = 0; i < roll.length; i++) {
            int diceRoll = dice.rollDice();
            roll[i] = diceRoll;
        }

        return roll;
    }

    private static int analyzeDiceRoll(Player player, int score, int[] roll, Dice dice, int numDiceUsed) {
        int rollScore = 0;

        // Analyze roll for straight or 3 pairs of 2
        Set rollSet = new HashSet<>(Arrays.asList(roll));
        //int[] targetArray = rollSet.toArray(new Integer[rollSet.size()]);

        if (rollSet.size() == 6) { // Analyze roll for straight (a, b, c, d, e, f)
            menuPrint(player.getName() + "rolled a straight. " + player.getName()+ " must keep rolling since all" +
                    "6 dice are in play.");
            score = score + 1500;
            roll = rollDice(dice, MAX_NUM_DICE - numDiceUsed);
            score = score + analyzeDiceRoll(player, score, roll, dice, numDiceUsed);

        } else if (rollSet.size() == 3) { // Analyze roll for 3 pairs of 2 (a, a, b, b, c, c)
            menuPrint(player.getName() + "'rolled 3 pairs of 2." + player.getName()+ "  They must keep rolling " +
                    "since all 6 dice are in play.");
            score = score + 1500;
            roll = rollDice(dice, MAX_NUM_DICE - numDiceUsed);
            score = score + analyzeDiceRoll(player, score, roll, dice, numDiceUsed);

        } else if (rollSet.size() == 1) { // Analyze roll for 6 of a kind (a, a, a, a, a, a)
            menuPrint(player.getName() + "'rolled 6 of a kind." + player.getName()+ " must keep rolling since " +
                     "6 dice are in play.");
            if (rollSet.contains(1)) {
                score = score + 8000;
            }

            //score = score +  ;
            roll = rollDice(dice, MAX_NUM_DICE - numDiceUsed);
            score = score + analyzeDiceRoll(player, score, roll, dice, numDiceUsed);

        } else {

        }

        return rollScore; // Player score for the current roll
    }

    private static void displayRules() {

    }

    private static void displayScoreboard(Player[] roster) {
        System.out.println(MENU_SEPARATOR);
        for (Player player : roster) {
            System.out.println(player.getName() + SCOREBOARD_SEPARATOR +
                    player.getScore());
        }
    }
    /*
    private static Player[] loadGame() {

        }

     private static void saveGame() {

        }
    */
    private static void menuPrint(String s) {
        System.out.println(MENU_SEPARATOR);
        System.out.println(s);
    }
}