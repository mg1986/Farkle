import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.stream.IntStream;

/**
 * PlayerMenu class - Class that handles the logic of a Player's turn.  This involves rolling dice and then resolving
 *                    the point scoring combinations for each roll until they Farkle or end their turn voluntarily.
 */

public class PlayerMenu {

    // Max number of dice a player can use in a turn
    private static final int MAX_NUM_DICE = 6;

    // Number of sides a single dice will have (Farkle uses six standard 6 sides dice)
    private static final int MAX_NUM_DICE_SIDES = 6;

    // Dice object that will be rolled throughout the game
    private static final Dice dice = new Dice(MAX_NUM_DICE_SIDES);
    
    //------------------------------------------------------------------------------------------------------------------
    // startPlayerTurn() - Called every time a players turn happens.  Continues until the player Farkles or chooses
    //                     to end their turn and keep their current turn's points.
    public static void startPlayerTurn(Scoreboard scoreboard, Player player) {

        boolean playersTurnStillActive = true;

        while (playersTurnStillActive) {
            if (player.getNumDiceInUse() == MAX_NUM_DICE) { rerollDice(player); }

            int numDiceInHand = MAX_NUM_DICE - player.getNumDiceInUse();

            MainMenu.menuPrint("It is " + player.getName() + "'s turn\n" +
                    player.getName() + "'s current score for this turn: " + player.getTurnScore() + "\n" +
                    player.getName() + "'s current number of dice available to roll: " + numDiceInHand);

            MainMenu.menuPrint("1. Roll dice\n" +
                    "2. Bank points and end turn\n" +
                    "3. View Scoreboard\n" +
                    "4. View Rules\n" +
                    "5. Save and exit game");

            String menuOption = System.console().readLine();

            switch (menuOption) {
                case "1":
                    rollAndAnalyzeDice(player, numDiceInHand);
                    playersTurnStillActive = checkforFarkle(player);
                    break;
                case "2":
                    playersTurnStillActive = checkScoreboardAndBankPoints(player, scoreboard);
                    break;
                case "3":
                    scoreboard.viewScoreboard();
                    break;
                case "4":
                    RuleBook.viewRulebook();
                    break;
                case "5":
                    MainMenu.saveScoreboard(scoreboard);
                    break;
                default:
                    MainMenu.menuPrint("Please press 1-5 to proceed with turn.");
                    break;
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // rollDice() - Returns an int array containing random rolls for the number of dice indicated with the numDice
    //              parameter
    public static ArrayList<Integer> rollDice(Player player, int numDice) {

        ArrayList<Integer> diceRoll = new ArrayList<Integer>();
        IntStream.range(0, numDice).forEachOrdered(n -> { diceRoll.add(dice.rollDice()); });
        MainMenu.menuPrint(player.getName() + "'s roll: " + Arrays.toString(diceRoll.toArray()));

        return diceRoll;
    }

    //------------------------------------------------------------------------------------------------------------------
    // rollAndAnalyzeDice() -
    //
    public static void rollAndAnalyzeDice(Player player, int numDice) {
        ArrayList<Integer> diceRoll = rollDice(player, numDice);
        analyzeDiceRoll(player, diceRoll);
    }

    //------------------------------------------------------------------------------------------------------------------
    // rerollDice() -
    public static void rerollDice(Player player) {
        MainMenu.menuPrint(player.getName() + " must roll again since all 6 dice are in play.");
        player.resetNumDiceInUse();
        rollAndAnalyzeDice(player, MAX_NUM_DICE);
    }

    //------------------------------------------------------------------------------------------------------------------
    // processDiceRoll() - Analyze Player's dice roll and presents each possble point scoring option possible.  These
    //                       possible point scoring options are then fed into the resolveDiceRoll() method.
    public static void analyzeDiceRoll(Player player, ArrayList<Integer> roll) {

        HashMap<Integer, ArrayList<Integer>> rollHashMap = buildRollHashMap(roll);

        Integer rollIndexListSizeOfTwo = 0;
        for (ArrayList<Integer> rollIndexList : rollHashMap.values()) {
            if (rollIndexList.size() == 2) rollIndexListSizeOfTwo++;
        }

        boolean straight = rollHashMap.size() == 6; // Indicates 6 unique values in roll
        boolean threePairsOfTwo = (rollIndexListSizeOfTwo == 3); // Indicates 3 pairs of 2

        // Analyze roll for 3 pairs of 2 (a, a, b b, c, c) or straight (a, b, c, d, e, f)
        if (straight || threePairsOfTwo) {
            String pointType = (straight) ? "a straight" : "3 pairs of 2";
            MainMenu.menuPrint(player.getName() + " rolled " + pointType + ". This is worth 1,500 points");
            player.setTurnScore(1500);
            rerollDice(player);
        }  else { // Check for >= 3 of a Kind, Ones, and Fives

            ArrayList<ScoreVariant> diceRollMenu = new ArrayList<ScoreVariant>();

            rollHashMap.forEach((rollNum, rollIndexList) -> {
                Integer rollIndexListSize = rollIndexList.size();
                if (rollIndexListSize >= 3) {
                    calculateThreeOfKindOrGreater(diceRollMenu, rollNum, rollIndexList);
                } else if (rollNum == 1 || rollNum == 5) {
                    calculateOneOrFive(diceRollMenu, rollNum, rollIndexList);
                }
            });

            if (diceRollMenu.size() > 0) { // If players roll contains any scoring combinations, resolve dice roll.
                resolveDiceRoll(player, diceRollMenu);
            } else { // Else if player's dice roll was a farkle, reset all turn variables end turn.
                player.resetPlayerTurn();
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // resolveDiceRoll() - Takes all the possible point scoring options for a roll from the processDiceRoll() method
    //                     and presents them to the Player.  They may choose what point scoring options to keep and
    //                     discard for every roll.  The point scoring options that are kept are then added to the
    //                     Player's turnScore variable.
    public static void resolveDiceRoll(Player player, ArrayList<ScoreVariant> diceRollMenu) {

        ArrayList<ScoreVariant> diceRollMenuReset = new ArrayList<ScoreVariant>(diceRollMenu);
        boolean rollResolved = false;

        while (!rollResolved) {

            Integer diceRollMenuSize = diceRollMenu.size();
            String resolveRollID = Integer.toString(diceRollMenuSize + 1);
            String keepAllScoreVariants = Integer.toString(diceRollMenuSize + 2);

            MainMenu.menuPrint("Select from the following scoring combinations to keep for this roll:");
            for (int idx = 0; idx < diceRollMenuSize; idx++) {
                String scoreType = diceRollMenu.get(idx).scoreType;
                Integer scoreAmount = diceRollMenu.get(idx).scoreAmount;
                System.out.println((idx + 1)+ ". " + scoreType + " - " + scoreAmount + " points");
            }

            System.out.println(resolveRollID + ". Keep current point total and resolve current roll");
            System.out.println(keepAllScoreVariants + ". Keep all scoring options and resolve current roll");

            String menuOption = System.console().readLine();
            Integer menuOptionIndex = - 1;

            try {
                menuOptionIndex = Integer.parseInt(menuOption);
                menuOptionIndex = menuOptionIndex - 1;
            } catch (NumberFormatException nfe) {
                MainMenu.menuPrint("Please enter a number from the above menu options");
                resolveDiceRoll(player, diceRollMenu);
            }

            if (menuOption.equals(resolveRollID)) {
                System.out.println("Do you want to keep the current point total and resolve current roll? y/n");
                menuOption = System.console().readLine();

                switch(menuOption) {
                    case "y":
                        rollResolved = true;
                        break;
                    case "n":
                        resolveDiceRoll(player, diceRollMenuReset);
                        break;
                    default:
                        System.out.println("Please enter y/n");
                        break;
                }
            } else if (menuOption.equals(keepAllScoreVariants)) {

                for (ScoreVariant scoreVariant : diceRollMenu ) {
                    player.setTurnScore(scoreVariant.scoreAmount);
                    player.setNumDiceInUse(scoreVariant.scoreIndices.size());
                }

                rollResolved = true;

            } else if (menuOptionIndex < diceRollMenuSize) {

                ScoreVariant scoreVariant = diceRollMenu.get(menuOptionIndex);
                player.setTurnScore(scoreVariant.scoreAmount);
                player.setNumDiceInUse(scoreVariant.scoreIndices.size());
                diceRollMenu.remove(scoreVariant);

                if (player.getNumDiceInUse() == MAX_NUM_DICE) rerollDice(player);

            } else {
                MainMenu.menuPrint("Please enter ID of scoring option");
                resolveDiceRoll(player, diceRollMenu);
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // checkforFarkle() -
    public static boolean checkforFarkle(Player player) {

        boolean playersTurnStillActive = true;

        if (player.getTurnScore() == 0) {
            MainMenu.menuPrint(player.getName() + " farkled and will receive a score of zero points for this turn.");
            playersTurnStillActive = false;
        }

        return playersTurnStillActive;
    }

    //------------------------------------------------------------------------------------------------------------------
    // checkScoreboardAndBankPoints() -
    public static boolean checkScoreboardAndBankPoints(Player player, Scoreboard scoreboard) {

        boolean playersTurnStillActive = true;

        if (player.getOnScoreboard() || player.getTurnScore() >= scoreboard.MIN_SCOREBOARD_SCORE) {
            MainMenu.menuPrint(player.getName() + " ended their turn with " + player.getTurnScore() + " points " +
                    "added to the scoreboard!");
            playersTurnStillActive = false;
        } else {
            MainMenu.menuPrint("You can only end your turn after reaching 1,000 points to get on the \n" +
                    "scoreboard or already having points on the scoreboard.");
        }

        return playersTurnStillActive;
    }

    //------------------------------------------------------------------------------------------------------------------
    // buildRollHashMap() -
    public static HashMap<Integer, ArrayList<Integer>> buildRollHashMap (ArrayList<Integer> roll) {

        HashMap<Integer, ArrayList<Integer>> rollHashMap = new HashMap<Integer, ArrayList<Integer>>();

        Integer rollIndex = 0;
        for (Integer rollNum : roll) {
            if (rollHashMap.containsKey(rollNum)) {
                rollHashMap.get(rollNum).add(rollIndex);
            } else {
                ArrayList<Integer> rollIndexList = new ArrayList<Integer>();
                rollIndexList.add(rollIndex);
                rollHashMap.put(rollNum, rollIndexList);
            }
            rollIndex++;
        }

        return rollHashMap;
    }

    //------------------------------------------------------------------------------------------------------------------
    // calculateThreeOfKindOrGreater() -
    public static void calculateThreeOfKindOrGreater (ArrayList<ScoreVariant> diceRollMenu, Integer rollNum, ArrayList<Integer> rollIndexList) {

        Integer rollIndexListSize = rollIndexList.size();
        String pointType = rollIndexListSize + " Of A Kind: " + rollNum;
        Integer pointsAmount = (rollNum == 1) ? 1000 : rollNum * 100;

        if (rollIndexListSize > 3) {
            for (int idx = 0; idx < rollIndexListSize - 3; idx++) {
                pointsAmount = pointsAmount * 2;
            }
        }

        ScoreVariant scoreVariant = new ScoreVariant(pointType, pointsAmount, rollIndexList);
        diceRollMenu.add(scoreVariant);
    }

    //------------------------------------------------------------------------------------------------------------------
    // calculateOneOrFive -
    public static void calculateOneOrFive (ArrayList<ScoreVariant> diceRollMenu, Integer rollNum, ArrayList<Integer> rollIndexList) {

        Integer rollIndexListSize = rollIndexList.size();
        String pointType = (rollNum == 1) ? "One" : "Five";
        Integer pointAmount = (rollNum == 1) ? 100 : 50;

        for (int idx = 0; idx < rollIndexListSize; idx++) {
            ArrayList<Integer> scoreVariantRollIndex = new ArrayList<Integer>();
            scoreVariantRollIndex.add(rollIndexList.get(idx));
            ScoreVariant scoreVariant = new ScoreVariant(pointType, pointAmount, scoreVariantRollIndex);
            diceRollMenu.add(scoreVariant);
        }
    }
}