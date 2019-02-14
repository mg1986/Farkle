package com.mg1986.farkle.components;

import com.mg1986.farkle.components.ScoreVariant;

import java.util.ArrayList;
import java.util.List;

public class SVSingleOne implements ScoreVariant {

    @Override
    public int getScoreAmount(
             return null;
    )


    @Override
    public List<Integer> getScoreIndices() {
        return null;
    }
    //------------------------------------------------------------------------------------------------------------------
    // calculateOneOrFive - Calculates the point value for a scoring variation a single 1 or 5 value being rolled.
    //                      Creates and adds a ScoreVariant object to the DiceRollMenu ArrayList.
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

