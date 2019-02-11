package com.mg1986.farkle;

import java.util.*;

/**
 * ScoreOption - Object holding the point amount, scoring combination, type, and the dice indexes involved
 *                with te scoring combination from the roll int[]
 */

public class ScoreOption {

    public String scoreType;
    public Integer scoreAmount;
    public ArrayList<Integer> scoreIndices;

    public ScoreOption(String type, Integer amount, ArrayList<Integer> indices) {
        this.scoreType = type;
        this.scoreAmount = amount;
        this.scoreIndices = indices;
    }
}
