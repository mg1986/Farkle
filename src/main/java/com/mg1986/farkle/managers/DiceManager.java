package com.mg1986.farkle.managers;

import com.mg1986.farkle.components.Dice;
import com.mg1986.farkle.components.Player;
import com.mg1986.farkle.components.ScoreVariant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.IntStream;

public class DiceManager {

    // Max number of dice a player can use in a turn
    public static final int MAX_NUM_DICE = 6;

    // Number of sides a single dice will have (Farkle uses six standard 6 sides dice)
    public  static final int MAX_NUM_DICE_SIDES = 6;

    // Dice object that will be rolled throughout the game
    public static final Dice dice = new Dice(MAX_NUM_DICE_SIDES);

    //------------------------------------------------------------------------------------------------------------------
    // checkforFarkle() - Checks the player of the current turn's turnScore value.  It if is zero, it indicates the
    //                    player Farkled during their turn, and now their turn is over.
    public static boolean checkforFarkle(Player player) {

        boolean playersTurnStillActive = true;

        if (player.getTurnScore() == 0) {
            System.out.println(player.getName() + " farkled and will receive a score of zero points for this turn.");
            MenuManager.pauseScreen();
            playersTurnStillActive = false;
        }

        return playersTurnStillActive;
    }

    //------------------------------------------------------------------------------------------------------------------
    // analyzeDiceRoll() - Analyze Player's dice roll and presents each possble point scoring option possible.  These
    //                     possible point scoring options are then fed into the resolveDiceRoll() method where the player
    //                     will choose which ones to keep and discard.
    public static void analyzeDiceRoll(Player player, ArrayList<Integer> diceRoll) {

        MenuManager.clearScreen();
        HashMap<Integer, ArrayList<Integer>> rollHashMap = buildRollHashMap(diceRoll);

        Integer rollIndexListSizeOfTwo = 0;
        for (ArrayList<Integer> rollIndexList : rollHashMap.values()) {
            if (rollIndexList.size() == 2) rollIndexListSizeOfTwo++;
        }

        boolean straight = rollHashMap.size() == 6; // Indicates 6 unique values in roll
        boolean threePairsOfTwo = (rollIndexListSizeOfTwo == 3); // Indicates 3 pairs of 2

        // Analyze roll for 3 pairs of 2 (a, a, b b, c, c) or straight (a, b, c, d, e, f)
        if (straight || threePairsOfTwo) {
            String pointType = (straight) ? "a straight" : "3 pairs of 2";
            System.out.println(player.getName() + " rolled " + pointType + ". This is worth 1,500 points");
            player.setTurnScore(1500);
            MenuManager.pauseScreen();
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
                resolveDiceRoll(player, diceRollMenu, diceRoll);
            } else { // Else if player's dice roll was a farkle, reset all turn variables end turn.
                MenuManager.clearScreen();
                System.out.println(player.getName() + "'s roll: " + Arrays.toString(diceRoll.toArray()) + "\n" +
                        "------------------------------------------------------------------------");
                player.resetPlayerTurn();
            }
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    // resolveDiceRoll() - Takes all the possible point scoring options for a roll from the processDiceRoll() method
    //                     and presents them to the Player.  They may choose what point scoring options to keep and
    //                     discard for every roll.  The point scoring options that are kept are then added to the
    //                     Player's turnScore variable.
    public static void resolveDiceRoll(Player player, ArrayList<ScoreVariant> diceRollMenu, ArrayList<Integer> diceRoll) {

        ArrayList<ScoreVariant> diceRollMenuReset = new ArrayList<ScoreVariant>(diceRollMenu);
        boolean rollResolved = false;

        while (!rollResolved) {

            MenuManager.clearScreen();
            System.out.println(player.getName() + "'s roll: " + Arrays.toString(diceRoll.toArray()) + "\n" +
                    "------------------------------------------------------------------------");

            int diceRollMenuSize = diceRollMenu.size();
            int resolveRollID = diceRollMenuSize + 1;
            int keepAllScoreVariants = diceRollMenuSize + 2;

            System.out.println("Select from the following scoring combinations to keep for this roll:");
            for (int idx = 0; idx < diceRollMenuSize; idx++) {
                String scoreType = diceRollMenu.get(idx).scoreType;
                Integer scoreAmount = diceRollMenu.get(idx).scoreAmount;
                System.out.println((idx + 1)+ ". " + scoreType + " - " + scoreAmount + " points");
            }

            System.out.println(resolveRollID + ". Keep current point total and resolve current roll");
            System.out.println(keepAllScoreVariants + ". Keep all scoring options and resolve current roll");

            int menuOption = MenuManager.getMenuOptionInt();
            int menuOptionListIndex = menuOption - 1;

            if (menuOption == resolveRollID) {
                System.out.println("Do you want to keep the current point total and resolve current roll? y/n");
                Scanner menuScanner = new Scanner(System.in);
                String resolveRollInput = menuScanner.next();

                switch(resolveRollInput) {
                    case "y":
                        rollResolved = true;
                        break;
                    case "n":
                        resolveDiceRoll(player, diceRollMenuReset, diceRoll);
                        break;
                    default:
                        System.out.println("Please enter y/n");
                        break;
                }
            } else if (menuOption == keepAllScoreVariants) {

                for (ScoreVariant scoreVariant : diceRollMenu ) {
                    player.setTurnScore(scoreVariant.scoreAmount);
                    player.setNumDiceInUse(scoreVariant.scoreIndices.size());
                }

                rollResolved = true;

            } else if (menuOptionListIndex < diceRollMenuSize) {

                ScoreVariant scoreVariant = diceRollMenu.get(menuOptionListIndex);
                player.setTurnScore(scoreVariant.scoreAmount);
                player.setNumDiceInUse(scoreVariant.scoreIndices.size());
                diceRollMenu.remove(scoreVariant);

                if (player.getNumDiceInUse() == MAX_NUM_DICE) rerollDice(player);

            } else {
                System.out.println("Please enter ID of scoring option from menu");
                resolveDiceRoll(player, diceRollMenu, diceRoll);
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // buildRollHashMap() - Takes a ArrayList of dice roll values and builds a hashmap using the roll values as keys,
    //                      and the index of that value in the ArrayList, in a corresping ArrayList as the value. As an
    //                      example, if a player rolls [1,2,4,5,6,6], the hashmap would look this this:
    //                      Key ----- Value
    //                       1  ----- [0]
    //                       2  ----- [1]
    //                       4  ----- [2]
    //                       5  ----- [3]
    //                       6  ----- [4, 5]
    //                      This hashmap is then used to calculate all of that rolls scoring variations and the
    //                      correpsonding index values of the dice they involve.
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
    // rollDice() - Returns an int array containing random rolls for the number of dice indicated with the numDice
    //              parameter
    public static ArrayList<Integer> rollDice(Player player, int numDice) {

        ArrayList<Integer> diceRoll = new ArrayList<Integer>();
        IntStream.range(0, numDice).forEachOrdered(n -> { diceRoll.add(dice.rollDice()); });

        return diceRoll;
    }

    //------------------------------------------------------------------------------------------------------------------
    // rollAndAnalyzeDice() - Created a dice roll using the number of dice indicated by the numDice paramter.  Dice roll
    //                        ArrayList is then fed into the analyzeDiceRoll method to check it for possible scoring
    //                        variations.
    public static void rollAndAnalyzeDice(Player player, int numDice) {
        ArrayList<Integer> diceRoll = rollDice(player, numDice);
        analyzeDiceRoll(player, diceRoll);
    }

    //------------------------------------------------------------------------------------------------------------------
    // rerollDice() - Automatically makes player roll all 6 dice again if they have 6 dice tied up in points.
    public static void rerollDice(Player player) {
        MenuManager.clearScreen();
        System.out.println(player.getName() + " must roll again since all 6 dice are in play.");
        player.resetNumDiceInUse();
        rollAndAnalyzeDice(player, MAX_NUM_DICE);
        String pauseMenu = System.console().readLine();
    }
}
