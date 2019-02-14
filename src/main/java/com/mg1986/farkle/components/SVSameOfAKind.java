package com.mg1986.farkle.components;

import com.mg1986.farkle.components.ScoreVariant;

import java.util.ArrayList;
import java.util.List;

public class SVSameOfAKind implements ScoreVariant {

    @Override
    public int getScoreAmount(
             return null;
    )


    @Override
    public List<Integer> getScoreIndices() {
        return null;
    }

    //-----------
    // -------------------------------------------------------------------------------------------------------
    // calculateThreeOfKindOrGreater() - Calculates the point value for a scoring variation of 3 of a kind or more
    //                                   based on the formula in the Rulebook. Creates and adds a ScoreVariant object
    //                                   to the DiceRollMenu ArrayList.
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
}

