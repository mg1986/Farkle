package com.mg1986.farkle.models;

import java.util.ArrayList;

/**
 * ScoreVariant - Object holding the point amount, scoring combination, type, and the dice indexes involved with the
 *                scoring combination from the roll int array
 */

public class ScoreVariant {

    // Score type
    public String type;

    // Score point amount
    public Integer points;

    // Indices ScoreVariant uses from roll
    public ArrayList<Integer> indices;

    // ScoreVariant constructor -
    public ScoreVariant(String type, Integer amount, ArrayList<Integer> indices) {
        this.type = type;
        this.points = amount;
        this.indices = indices;
    }
}
