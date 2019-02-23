package com.mg1986.farkle.controllers;

import java.util.Map;
import java.util.ArrayList;
import com.mg1986.farkle.model.ScoreVariant;

/**
 * ScoreVariantController class - Controls creation of ScoreVariants
 */

public class ScoreVariantController {

    //------------------------------------------------------------------------------------------------------------------
    // singlesExists() - Detects if roll has 1's or 5's
    public static boolean singlesExists(Map<Integer, ArrayList<Integer>> rollMap) {
        for (int key : rollMap.keySet()) {
            if (key == 1 || key == 5) return true;
        }

        return false;
    }

    //------------------------------------------------------------------------------------------------------------------
    // straightExists() - Detects if roll is straight - 1, 2, 3, 4, 5, 6
    public static boolean straightExists(Map<Integer, ArrayList<Integer>> rollMap) {
        return rollMap.size() == 6;
    }

    //------------------------------------------------------------------------------------------------------------------
    // threePairsExists() - Detects if roll is three pairs of two - 1, 1, 2, 2, 3, 3
    public static boolean threePairsExists(Map<Integer, ArrayList<Integer>> rollMap) {

        Integer rollIndexListSizeOfTwo = 0;
        for (ArrayList<Integer> rollIndexList : rollMap.values()) {
            if (rollIndexList.size() == 2) rollIndexListSizeOfTwo++;
        }
        return rollIndexListSizeOfTwo == 3;
    }

    //------------------------------------------------------------------------------------------------------------------
    // duplicatesExists() - Detects if roll has three or more of a kind - 1, 2, 3, 4, 4, 4
    public static boolean duplicatesExists(Map<Integer, ArrayList<Integer>> rollMap) {
        for (ArrayList<Integer> rollIndexList : rollMap.values()) {
            if (rollIndexList.size() >= 3) return true;
        }

        return false;
    }

    //------------------------------------------------------------------------------------------------------------------
    // calculateSingle() - Analyzes roll and adds ScoreVariant objects to diceRollMenu for single 1's and 5's
    public static void calculateSingle(Map<Integer, ArrayList<Integer>> rollMap, ArrayList<ScoreVariant> diceRollMenu) {

        rollMap.forEach((rollNum, rollIndexList) -> {
            if (rollNum == 1 || rollNum == 5) {
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
        });
    }

    // -------------------------------------------------------------------------------------------------------
    // calculateDuplicates() - Calculates the point value for a scoring variation of 3 of a kind or more based on the
    //                         formula in the Rulebook. Creates and adds a ScoreVariant object to the DiceRollMenu.
    public static void calculateDuplicates(Map<Integer, ArrayList<Integer>> rollMap, ArrayList<ScoreVariant> diceRollMenu) {

        rollMap.forEach((rollNum, rollIndexList) -> {
            Integer rollIndexListSize = rollIndexList.size();
            String pointType = rollIndexListSize + " Of A Kind: " + rollNum;
            Integer pointsAmount = (rollNum == 1) ? 1000 : rollNum * 100;

            if (rollIndexListSize >= 3) {
                for (int idx = 0; idx < rollIndexListSize - 3; idx++) {
                    pointsAmount = pointsAmount * 2;
                }

                ScoreVariant scoreVariant = new ScoreVariant(pointType, pointsAmount, rollIndexList);
                diceRollMenu.add(scoreVariant);
            }
        });
    }
}
