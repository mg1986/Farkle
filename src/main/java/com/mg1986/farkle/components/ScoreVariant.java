package com.mg1986.farkle.components;

import java.util.ArrayList;

/**
 * ScoreVariant - Object holding the point amount, scoring combination, type, and the dice indexes involved
 *                with the scoring combination from the roll int[]
 */

public class ScoreVariant {

        public String type;
        public Integer points;
        public ArrayList<Integer> indices;

        public ScoreVariant(String type, Integer amount, ArrayList<Integer> indices) {
            this.type = type;
            this.points = amount;
            this.indices = indices;
        }
}
