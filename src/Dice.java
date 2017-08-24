import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by matt on 8/3/17.
 */

public class Dice {

    // Dice roll will always have a minimum of 1
    public final int ROLL_MINIMUM = 1;

    // Number of sides Dice object will start with
    public int numSides;

    // Dice constructor
    Dice(int sides) {

        numSides = sides;
    }

    // Roll Dice method
    public int rollDice () {
        int randomInt = ThreadLocalRandom.current().nextInt(ROLL_MINIMUM,
                numSides + 1);

        return randomInt;
    }
}
