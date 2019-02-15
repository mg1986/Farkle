package com.mg1986.farkle.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.stream.IntStream;
import com.mg1986.farkle.components.*;
import static com.mg1986.farkle.controllers.MenuController.*;
import static com.mg1986.farkle.controllers.ScoreVariantController.*;

/**
 * DiceController class - Controls dice roll/analysis logic
 */

public class DiceController {

    // Max number of dice a player can use in a turn
    public static final int MAX_NUM_DICE = 6;

    // Number of sides a single dice will have (Farkle uses six standard 6 sides dice)
    public  static final int MAX_NUM_DICE_SIDES = 6;

    // Dice object that will be rolled throughout the game
    public static final Dice dice = new Dice(MAX_NUM_DICE_SIDES);

    //------------------------------------------------------------------------------------------------------------------
    // checkForFarkle() - Checks the player of the current turn's turnScore value.  It if is zero, it indicates the
    //                    player farkled during their turn, and now their turn is over.
    public static boolean checkForFarkle(Player player) {

        boolean playersTurnStillActive = true;

        if (player.getTurnScore() == 0) {
            println(player.getName() + " farkled and will receive a score of 0 points for this turn.");
            pauseScreen();
            playersTurnStillActive = false;
        }

        return playersTurnStillActive;
    }

    //------------------------------------------------------------------------------------------------------------------
    // analyzeDiceRoll() - Analyze Player's dice roll and presents each possible point scoring option. These possible
    //                     point scoring options are then fed into the resolveDiceRoll() method where the player will
    //                     choose which ones to keep and discard.
    public static void analyzeDiceRoll(Player player, ArrayList<Integer> diceRoll) {

        clearScreen();
        HashMap<Integer, ArrayList<Integer>> rollMap = buildRollHashMap(diceRoll);

        // Analyze roll for 3 pairs of 2 (a, a, b b, c, c) or straight (a, b, c, d, e, f)
        if (straightExists(rollMap)) {
            println(player.getName() + " rolled a straight. This is worth 3,000 points.");
            player.setTurnScore(3000);
            pauseScreen();
            rerollDice(player);
        } else if (threePairsExists(rollMap)) {
            println(player.getName() + " rolled Three Pairs Of Two. This is worth 1,500 points.");
            player.setTurnScore(1500);
            pauseScreen();
            rerollDice(player);
        } else { // Check for >= 3 of a Kind, Ones, and Fives

            ArrayList<ScoreVariant> diceRollMenu = new ArrayList<>();

            if (duplicatesExists(rollMap)) {
                calculateDuplicates(rollMap, diceRollMenu);
            } else if (singlesExists(rollMap)) {
                calculateSingle(rollMap, diceRollMenu);
            }

            if (player.getNumDiceInUse() == MAX_NUM_DICE) {
                println(player.getName() + " has all 6 dice in use and must reroll.");
                pauseScreen();
                rerollDice(player);
            }

            if (diceRollMenu.size() > 0) { // If players roll contains any scoring combinations, resolve dice roll.
                resolveDiceRoll(player, diceRollMenu, diceRoll);
            } else { // Else if player's dice roll was a farkle, reset all turn variables end turn.
                clearScreen();
                println(player.getName() + "'s roll: " + Arrays.toString(diceRoll.toArray()) + "\n" +
                        "---------------------------------------------------------------------");
                player.resetPlayerTurn();
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // resolveDiceRoll() - Takes all the possible point scoring options for a roll from the processDiceRoll() method
    //                     and presents them to the Player. They may choose what point scoring options to keep and
    //                     discard for every roll.  The point scoring options that are kept are then added to the
    //                     Player's turnScore variable.
    public static void resolveDiceRoll(Player player, ArrayList<ScoreVariant> diceRollMenu, ArrayList<Integer> diceRoll) {

        ArrayList<ScoreVariant> diceRollMenuReset = new ArrayList<ScoreVariant>(diceRollMenu);
        boolean rollResolved = false;

        while (!rollResolved) {

            clearScreen();
            println(player.getName() + "'s roll: " + Arrays.toString(diceRoll.toArray()) + "\n" +
                    "------------------------------------------------------------------------");

            int diceRollMenuSize = diceRollMenu.size();
            int resolveRollID = diceRollMenuSize + 1;
            int keepAllScoreVariants = diceRollMenuSize + 2;

            println("Select from the following scoring combinations to keep for this roll:");
            for (int idx = 0; idx < diceRollMenuSize; idx++) {
                String scoreType = diceRollMenu.get(idx).type;
                Integer scoreAmount = diceRollMenu.get(idx).points;
                println((idx + 1)+ ". " + scoreType + " - " + scoreAmount + " points");
            }

            println(resolveRollID + ". Keep current point total and resolve current roll");
            println(keepAllScoreVariants + ". Keep all scoring options and resolve current roll.");

            int menuOption = getMenuOptionInt();
            int menuOptionListIndex = menuOption - 1;

            if (menuOption == resolveRollID) {
                println("Do you want to keep the current point total and resolve current roll? y/n");

                String resolveRollInput = scanner.next();

                switch(resolveRollInput) {
                    case "y":
                        rollResolved = true;
                        break;
                    case "n":
                        resolveDiceRoll(player, diceRollMenuReset, diceRoll);
                        break;
                    default:
                        println("Please enter y/n");
                        break;
                }
            } else if (menuOption == keepAllScoreVariants) {

                for (ScoreVariant scoreVariant : diceRollMenu ) {
                    player.setTurnScore(scoreVariant.points);
                    player.setNumDiceInUse(scoreVariant.indices.size());
                }

                rollResolved = true;

            } else if (menuOptionListIndex < diceRollMenuSize) {

                ScoreVariant scoreVariant = diceRollMenu.get(menuOptionListIndex);
                player.setTurnScore(scoreVariant.points);
                player.setNumDiceInUse(scoreVariant.indices.size());
                diceRollMenu.remove(scoreVariant);

                if (player.getNumDiceInUse() == MAX_NUM_DICE) rerollDice(player);

            } else {
                println("Please enter ID of scoring option from menu");
                resolveDiceRoll(player, diceRollMenu, diceRoll);
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // buildRollHashMap() - Takes a ArrayList of dice roll values and builds a hashmap using the roll values as keys, and
    //                      the index of that value in the ArrayList, in a corresponding ArrayList as the value. As
    //                      an example, if a player rolls [1,2,4,5,6,6], the HashMap would look this this:
    //                      Key ----- Value
    //                       1  ----- [0]
    //                       2  ----- [1]
    //                       4  ----- [2]
    //                       5  ----- [3]
    //                       6  ----- [4, 5]
    //                      This HashMap is then used to calculate all of that rolls scoring variations and the corresponding
    //                      index values of the dice they involve.
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
    // rollDice() - Returns an Integer array containing random rolls for the number of dice indicated with the numDice
    //              parameter
    public static ArrayList<Integer> rollDice(Player player, int numDice) {

        ArrayList<Integer> diceRoll = new ArrayList<Integer>();
        IntStream.range(0, numDice).forEachOrdered(n -> { diceRoll.add(dice.rollDice()); });

        return diceRoll;
    }

    //------------------------------------------------------------------------------------------------------------------
    // rollAndAnalyzeDice() - Creates a dice roll using the number of dice indicated by the numDice parameter.  Dice roll
    //                        ArrayList is then fed into the analyzeDiceRoll method to check it for possible scoring variations.
    public static void rollAndAnalyzeDice(Player player, int numDice) {
        ArrayList<Integer> diceRoll = rollDice(player, numDice);
        analyzeDiceRoll(player, diceRoll);
    }

    //------------------------------------------------------------------------------------------------------------------
    // rerollDice() - Automatically makes player roll all 6 dice again if they have 6 dice tied up in points.
    public static void rerollDice(Player player) {
        clearScreen();
        println(player.getName() + " must roll again since all 6 dice are in play.");
        player.resetNumDiceInUse();
        rollAndAnalyzeDice(player, MAX_NUM_DICE);
    }
}
