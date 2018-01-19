import java.io.*;
import java.lang.*;
import java.util.*;

/**
 * PlayerTurn class - Class that handles the logic of a Player rolling dice and then resolving the point scoring
 *                    combinations for each roll.
 */

public class PlayerTurn {

    // Max number of dice a player can use in a turn
    private static final int MAX_NUM_DICE = 6;
    
    //------------------------------------------------------------------------------------------------------------------
    // playerTurn() - Called every time a players turn happens.  Continues until the player Farkles or chooses to end
    //                their turn and keep their current turn's points.
    public static void startPlayerTurn(Scoreboard scoreboard, Player player, Dice dice) {

        while (true) {
            if (player.getNumDiceInUse() == MAX_NUM_DICE) {
                MainMenu.menuPrint("All six dice are in use, so " + player.getName() + " must roll again.");
                player.resetNumDiceInUse();
                int[] roll = rollDice(player, dice, MAX_NUM_DICE);
                processDiceRoll(player, roll, dice);
            }

            int numDiceInHand = MAX_NUM_DICE - player.getNumDiceInUse();

            MainMenu.menuPrint("It is " + player.getName() + "'s turn\n" +
                    player.getName() + "'s current score for this turn is " + player.getTurnScore() + "\n" +
                    player.getName() + "'s current number of dice available to roll " + numDiceInHand);

            MainMenu.menuPrint("1. Roll dice\n" +
                    "2. Bank points and end turn\n" +
                    "3. View Scoreboard\n" +
                    "4. View Rules\n" +
                    "5. Save and exit game");

            String menuOption = System.console().readLine();

            if (menuOption.equals("1")) {
                int[] roll = rollDice(player, dice, numDiceInHand);
                processDiceRoll(player, roll, dice);
                if (player.getTurnScore() == 0) {
                    MainMenu.menuPrint(player.getName() + " farkled and will receive a score of zero points for this turn.");
                    break;
                }
            } else if (menuOption.equals("2")) {
                if (player.getOnScoreboard() || player.getTurnScore() >= scoreboard.MIN_SCOREBOARD_SCORE) {
                    MainMenu.menuPrint(player.getName() + " ended their turn with " + player.getTurnScore() + " points " +
                            "added to the scoreboard!");
                    break;
                } else {
                    MainMenu.menuPrint("You can only end your turn after reaching 1,000 points to get on the \n" +
                            "scoreboard or already having points on the scoreboard.");
                }
            } else if (menuOption.equals("3")) {
                scoreboard.viewScoreboard();
            } else if (menuOption.equals("4")) {
                MainMenu.menuPrint(RuleBook.getGameRules());
            } else if (menuOption.equals("5")) {
                MainMenu.saveScoreboard(scoreboard);
                MainMenu.menuPrint("Thank you for playing Farkle!");
                System.exit(0);
            } else {
                MainMenu.menuPrint("Please press 1-5 to proceed with turn.");
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // rollDice() - Returns an int array containing random rolls for the number of dice indicated with the numDice
    //              parameter
    public static int[] rollDice(Player player, Dice dice, int numDice) {

        int[] roll = new int[numDice];

        for (int i = 0; i < roll.length; i++) {
            int diceRoll = dice.rollDice();
            roll[i] = diceRoll;
        }
        MainMenu.menuPrint(player.getName() + "'s roll: " + Arrays.toString(roll));
        return roll;
    }

    //------------------------------------------------------------------------------------------------------------------
    // processDiceRoll() - Analyze Player's dice roll and presents each possble point scoring option possible.  These
    //                       possible point scoring options are then fed into the resolveDiceRoll() method.
    public static void processDiceRoll(Player player, int[] roll, Dice dice) {

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
            if (straight == 6) { // Analyze roll for straight (a, b, c, d, e, f)
                menuMessage = player.getName() + " rolled a straight. This is worth 2,500 points";
                player.setTurnScore(2500);
            } else if (twoPairsofThree == 2) { // Analyze roll for 2 pairs of 3 (a, a, b, b, c, c)
                menuMessage = player.getName() + " rolled 2 pairs of 3. This is worth 1,500 points";
                player.setTurnScore(1500);
            } else if (threePairsOfTwo == 3) { // Analyze roll for 3 pairs of 2 (a, a, a, b, b, b)
                menuMessage = player.getName() + " rolled 3 pairs of 2. This is worth 1,500 points";
                player.setTurnScore(1500);
            }

            MainMenu.menuPrint( menuMessage + ", however " + player.getName() + " must roll again since all \n" +
                    "6 dice are in play.");
            player.resetNumDiceInUse();
            roll = rollDice(player, dice, MAX_NUM_DICE);
            processDiceRoll(player, roll, dice);
        } else {
            DiceRollMenu diceRollMenu = new DiceRollMenu();

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

                    String diceRollMenuKey = diceRollMenu.createDiceRollMenuKey();
                    diceRollMenu.pointType.put(diceRollMenuKey, name);
                    diceRollMenu.pointScore.put(diceRollMenuKey, points);
                    diceRollMenu.pointIndexes.put(diceRollMenuKey, rollIndexList);
                } else if (key == 1 || key == 5) {
                    for (Integer rollIdx: rollIndexList) {
                        if (key == 1) {
                            name = "One";
                            points = 100;
                        } else {
                            name = "Five";
                            points = 50; }
                        String diceRollMenuKey = diceRollMenu.createDiceRollMenuKey();
                        diceRollMenu.pointType.put(diceRollMenuKey, name);
                        diceRollMenu.pointScore.put(diceRollMenuKey, points);
                        rollIndexList = new ArrayList<Integer>();
                        rollIndexList.add(rollIdx);
                        diceRollMenu.pointIndexes.put(diceRollMenuKey, rollIndexList);
                    }
                }
            }

            if (diceRollMenu.getMenuSize() > 0) {
                resolveDiceRoll(player, dice, diceRollMenu);
            } else {
                player.resetTurnScore();
                player.resetNumDiceInUse();
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // resolveDiceRoll() - Takes all the possible point scoring options for a roll from the processDiceRoll() method
    //                     and presents them to the Player.  They may choose what point scoring options to keep and
    //                     discard for every roll.  The point scoring options that are kept are then added to the
    //                     Player's turnScore variable.
    public static void resolveDiceRoll(Player player, Dice dice, DiceRollMenu diceRollMenu) {

        String resolveID = Integer.toString(diceRollMenu.getMenuSize() + 1);
        String menuOption = "";

        while (true) {
            if (diceRollMenu.getMenuSize() < 1) {
                MainMenu.menuPrint("Do you want to keep the current point total and resolve current roll? y/n");

                menuOption = System.console().readLine();

                if (menuOption.equals("y")) { break; }
                else if (menuOption.equals("n")) { ; }
                else { System.out.println("Please enter y/n"); }
            }

            MainMenu.menuPrint("Select from the following scoring combinations to keep for this roll");
            for (String key : diceRollMenu.pointType.keySet()) {
                String pointType = diceRollMenu.pointType.get(key);
                Integer pointScore = diceRollMenu.pointScore.get(key);
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
            } else if (diceRollMenu.pointType.containsKey(menuOption)) {
                Integer pointScore = diceRollMenu.pointScore.get(menuOption);
                player.setTurnScore(pointScore);
                player.setNumDiceInUse(diceRollMenu.pointIndexes.get(menuOption).size());
                diceRollMenu.removeKey(menuOption);
                if (player.getNumDiceInUse() == MAX_NUM_DICE) {
                    MainMenu.menuPrint("All six dice are in use, so " + player.getName() + " must roll again.");
                    player.resetNumDiceInUse();
                    int[] roll = rollDice(player, dice, MAX_NUM_DICE);
                    processDiceRoll(player, roll, dice);
                }
            } else {
                MainMenu.menuPrint("Please enter ID of scoring option");
            }
        }
    }
}
