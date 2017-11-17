import java.io.*;
import java.lang.*;
import java.util.*;

/**
 * Farkle - Dice game where the goal is to reach 10,000 points first
 * Created by matt on 8/3/17.
 */

public class Farkle {

    private static final int MAX_NUM_DICE = 6;
    public static final String MENU_SEPARATOR = "------------------------------------" +
                                                "------------------------------------";

    //------------------------------------------------------------------------------------------------------------------
    public static void main(String[] args) {
        mainMenu();
    }

    //------------------------------------------------------------------------------------------------------------------
    public static void mainMenu() {

        menuPrint("Welcome to Farkle. Pick from the following options: \n" +
                "1. New Game\n" +
                "2. Load Game\n" +
                "3. Clear Saved Games\n" +
                "4. View Rules\n" +
                "5. Exit");

        String menuOption = System.console().readLine();

        if (menuOption.equals("1")) { // New Game
            Scoreboard scoreboard = new Scoreboard();
            scoreboard.createRoster();
            newGame(scoreboard);
        } else if (menuOption.equals("2")) { // Load Game
            Scoreboard scoreboard = new Scoreboard();
            scoreboard.loadRoster();
            newGame(scoreboard);
        } else if (menuOption.equals("3")) {
            menuPrint("Clearing saved games....");
            Scoreboard.clearSavedGames();
            menuPrint("Saved games cleared");
            mainMenu();
        }else if (menuOption.equals("4")) {
            RuleBook.viewRules();
            mainMenu();
        } else if (menuOption.equals("5")) {
            menuPrint("Thank you for playing Farkle!");
            System.exit(0);
        } else {
            menuPrint("Please press 1-5 to proceed.");
            mainMenu();
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    private static void newGame(Scoreboard scoreboard) {

        boolean gameOver = false;
        Dice dice = new Dice(6);

        while (!gameOver) {
            for (Player player : scoreboard.roster) {
                player.setIsCurrentTurn(true);
                int turnScore = playerTurn(scoreboard, player, dice);
                if (turnScore >= scoreboard.MIN_SCOREBOARD_SCORE) {
                    player.setOnScoreboard(true);
                }

                player.setScore(turnScore);

                if (player.getScore() >= scoreboard.MAX_SCORE) {
                    gameOver = true;
                    menuPrint(player.getName() + " won the game with a score of " +
                            String.format("%,d", player.getScore()) + " points!");
                }
                player.setIsCurrentTurn(false);
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    private static int playerTurn(Scoreboard scoreboard, Player player, Dice dice) {

        TurnScore turnScore = new TurnScore();

        while (true) {
            if (turnScore.getNumDiceInUse() == MAX_NUM_DICE) {
                menuPrint("All six dice are in use, so " + player.getName() + " must roll again.");
                turnScore.resetNumDiceInUse();
                int[] roll = rollDice(player, dice, MAX_NUM_DICE);
                turnScore = processDiceRoll(player, roll, dice, turnScore);
            }

            int numDiceInHand = MAX_NUM_DICE - turnScore.getNumDiceInUse();

            menuPrint("It is " + player.getName() + "'s turn\n" +
                    player.getName() + "'s current score for this turn is " + turnScore.getTurnScore() + "\n" +
                    player.getName() + "'s current number of dice available to roll " + numDiceInHand);

            menuPrint("1. Roll dice\n" +
                    "2. Bank points and end turn\n" +
                    "3. View Scoreboard\n" +
                    "4. View Rules\n" +
                    "5. Save and exit game");

            String menuOption = System.console().readLine();

            if (menuOption.equals("1")) {
                int[] roll = rollDice(player, dice, numDiceInHand);
                turnScore = processDiceRoll(player, roll, dice, turnScore);
                if (turnScore.getTurnScore() == 0) {
                    menuPrint(player.getName() + " farkled and will receive a score of zero points for this turn.");
                    break;
                }
            } else if (menuOption.equals("2")) {
                if (player.getOnScoreboard() || turnScore.getTurnScore() >= scoreboard.MIN_SCOREBOARD_SCORE) {
                    menuPrint(player.getName() + " ended their turn with " + turnScore.getTurnScore() + " points " +
                            "added to the scoreboard!");
                    break;
                } else {
                    menuPrint("You can only end your turn after reaching 1,000 points to get on the \n" +
                            "scoreboard or already having points on the scoreboard.");
                }
            } else if (menuOption.equals("3")) {
                scoreboard.viewScoreboard();
            } else if (menuOption.equals("4")) {
                RuleBook.viewRules();
            } else if (menuOption.equals("5")) {
                player.setNumDiceInUse(turnScore.getNumDiceInUse());
                scoreboard.saveRoster();
                menuPrint("Thank you for playing Farkle!");
                System.exit(0);
            } else {
                menuPrint("Please press 1-5 to proceed with turn.");
            }
        }

        return turnScore.getTurnScore();
    }

    //------------------------------------------------------------------------------------------------------------------
    private static int[] rollDice(Player player, Dice dice, int numDice) {

        int[] roll = new int[numDice];

        for (int i = 0; i < roll.length; i++) {
            int diceRoll = dice.rollDice();
            roll[i] = diceRoll;
        }
        menuPrint(player.getName() + "'s roll: " + Arrays.toString(roll));
        return roll;
    }

    //------------------------------------------------------------------------------------------------------------------
    private static TurnScore processDiceRoll(Player player, int[] roll, Dice dice, TurnScore turnScore) {

        HashMap<Integer, List<Integer>> rollHashMap = new HashMap<Integer, List<Integer>>();

        Integer rollIndex = 0;
        for (Integer rollNum : roll) {
            if (rollHashMap.containsKey(rollNum)) {
                rollHashMap.get(rollNum).add(rollIndex);
            } else {
                List<Integer> rollIndexList = new ArrayList<Integer>();
                rollIndexList.add(rollIndex);
                rollHashMap.put(rollNum, rollIndexList);
            }
            rollIndex++;
        }

        int straight = 0;
        int threePairsOfTwo = 0;
        int twoPairsofThree = 0;

        for (Integer key : rollHashMap.keySet()) {
            if ((rollHashMap.get(key)).size() == 1) { straight++; }
            else if ((rollHashMap.get(key)).size() == 2) { threePairsOfTwo++; }
            else if ((rollHashMap.get(key)).size() == 3) { twoPairsofThree++; }
        }

        if (straight == 6 || twoPairsofThree == 2 || threePairsOfTwo == 3) {
            String menuMessage = "";
            if (straight == 6) {
                menuMessage = player.getName() + " rolled a straight. This is worth 2,500 points";
                turnScore.setTurnScore(2500);
            } else if (twoPairsofThree == 2) { // Analyze roll for 2 pairs of 3 (a, a, b, b, c, c)
                menuMessage = player.getName() + " rolled 2 pairs of 3. This is worth 1,500 points";
                turnScore.setTurnScore(1500);
            } else if (threePairsOfTwo == 3) { // Analyze roll for 3 pairs of 2 (a, a, a, b, b, b)
                menuMessage = player.getName() + " rolled 3 pairs of 2. This is worth 1,500 points";
                turnScore.setTurnScore(1500);
            }

            menuPrint( menuMessage + ", however " + player.getName() + " must roll again since all \n" +
                    "6 dice are in play.");
            turnScore.resetNumDiceInUse();
            roll = rollDice(player, dice, MAX_NUM_DICE);
            turnScore = processDiceRoll(player, roll, dice, turnScore);
        } else {
            RollScoreMenu rollScoreMenu = new RollScoreMenu();

            for (Integer key : rollHashMap.keySet()) {
                String name = "";
                Integer points = 0;
                List<Integer> rollIndexList = rollHashMap.get(key);
                int rollIndexListSize = rollIndexList.size();

                if (rollIndexListSize >= 3) {
                    name = rollIndexListSize + " Of A Kind: " + key;

                    if (key == 1) {
                        points = 1000;
                    } else {
                        points = key * 100;
                    }

                    if (rollIndexListSize > 3) {
                        int multiplier = rollIndexListSize - 3;
                        for (int x = 1; x <= multiplier; x++) {
                            points = points * 2;
                        }
                    }

                    String rollScoreMenuKey = rollScoreMenu.createMenuKey();
                    rollScoreMenu.pointType.put(rollScoreMenuKey, name);
                    rollScoreMenu.pointScore.put(rollScoreMenuKey, points);
                    rollScoreMenu.pointIndexes.put(rollScoreMenuKey, rollIndexList);
                } else if (key == 1 || key == 5) {
                    for (Integer rollIdx: rollIndexList) {
                        if (key == 1) {
                            name = "One";
                            points = 100;
                        } else {
                            name = "Five";
                            points = 50; }
                        String rollScoreMenuKey = rollScoreMenu.createMenuKey();
                        rollScoreMenu.pointType.put(rollScoreMenuKey, name);
                        rollScoreMenu.pointScore.put(rollScoreMenuKey, points);
                        rollIndexList = new ArrayList<Integer>();
                        rollIndexList.add(rollIdx);
                        rollScoreMenu.pointIndexes.put(rollScoreMenuKey, rollIndexList);
                    }
                }
            }

            if (rollScoreMenu.getMenuSize() > 0) {
                turnScore = resolveDiceRoll(player, dice, turnScore, rollScoreMenu);
            } else {
                turnScore.resetTurnScore();
                turnScore.resetNumDiceInUse();
            }
        }

        return turnScore;
    }

    //------------------------------------------------------------------------------------------------------------------
    private static TurnScore resolveDiceRoll(Player player, Dice dice, TurnScore turnScore, RollScoreMenu rollScoreMenu) {

        String resolveID = Integer.toString(rollScoreMenu.getMenuSize() + 1);
        String menuOption = "";

        while (true) {
            if (rollScoreMenu.getMenuSize() < 1) {
                menuPrint("Do you want to keep the current point total and resolve current roll? y/n");

                menuOption = System.console().readLine();

                if (menuOption.equals("y")) { break; }
                else if (menuOption.equals("n")) { ; }
                else { System.out.println("Please enter y/n"); }
            }

            menuPrint("Select from the following scoring combinations to keep for this roll");
            for (String key : rollScoreMenu.pointType.keySet()) {
                String pointType = rollScoreMenu.pointType.get(key);
                Integer pointScore = rollScoreMenu.pointScore.get(key);
                System.out.println(key + ". " + pointType + " - " + pointScore + " points");
            }

            System.out.println(resolveID + ". Keep current point total and resolve current roll");

            menuOption = System.console().readLine();

            if (menuOption.equals(resolveID)) {
                System.out.println("Do you want to keep the current point total and resolve current roll? y/n");
                menuOption = System.console().readLine();

                if (menuOption.equals("y")) { break; }
                else if (menuOption.equals("n")) { ; }
                else { System.out.println("Please enter y/n"); }
            } else if (rollScoreMenu.pointType.containsKey(menuOption)) {
                Integer pointScore = rollScoreMenu.pointScore.get(menuOption);
                turnScore.setTurnScore(pointScore);
                turnScore.setNumDiceInUse(rollScoreMenu.pointIndexes.get(menuOption).size());
                rollScoreMenu.removeKey(menuOption);
                if (turnScore.getNumDiceInUse() == MAX_NUM_DICE) {
                    menuPrint("All six dice are in use, so " + player.getName() + " must roll again.");
                    turnScore.resetNumDiceInUse();
                    int[] roll = rollDice(player, dice, MAX_NUM_DICE);
                    turnScore = processDiceRoll(player, roll, dice, turnScore);
                }
            } else {
                menuPrint("Please enter ID of scoring option");
            }
        }

        return turnScore;
    }

    //------------------------------------------------------------------------------------------------------------------
    public static void menuPrint(String s) {
        System.out.println(MENU_SEPARATOR);
        System.out.println(s);
    }
}