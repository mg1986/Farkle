package com.mg1986.Farkle;

import java.util.*;

/**
 * ScoreVariant - Object holding the point amount, scoring combination, type, and the dice indexes involved
 *                with te scoring combination from the roll int[]
 */

public class ScoreVariant {

    public String scoreType;
    public Integer scoreAmount;
    public ArrayList<Integer> scoreIndices;

    public ScoreVariant (String type, Integer amount, ArrayList<Integer> indices) {
        this.scoreType = type;
        this.scoreAmount = amount;
        this.scoreIndices = indices;
    }
}
