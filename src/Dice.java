import java.util.concurrent.ThreadLocalRandom;

/**
 * Dice class - Simulates dice with 1-infinite number of sides
 */

public class Dice {

    // Dice roll will always have a minimum of 1
    public final int ROLL_MINIMUM = 1;

    // Number of sides Dice object has
    public int numSides;

    // Dice constructor - Takes the number of sides the dice will have as input
    Dice(int sides) {
        numSides = sides;
    }

    //------------------------------------------------------------------------------------------------------------------
    // rollDice() - Returns random integer from 1 - numSides
    public int rollDice () {
        int randomInt = ThreadLocalRandom.current().nextInt(ROLL_MINIMUM, numSides + 1);
        return randomInt;
    }
}
