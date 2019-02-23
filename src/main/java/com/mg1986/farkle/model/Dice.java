package com.mg1986.farkle.model;

import java.security.SecureRandom;

/**
 * Dice class - Simulates dice with 1-infinite number of sides
 */

public class Dice {

    // SecureRandom object used to generate pseudo random numbers
    private static final SecureRandom secureRandom = new SecureRandom();

    // Number of sides Dice object has
    private Integer numSides;

    // Dice class instance constructor - Takes the number of sides the dice will have as input
    public Dice(Integer numDiceSides) {
        this.numSides = numDiceSides;
    }

    //------------------------------------------------------------------------------------------------------------------
    // rollDice() - Returns random integer from 1 <--> numSides
    public Integer rollDice () {
        Integer randomInteger =  1 + secureRandom.nextInt(numSides);
        return randomInteger;
    }
}
