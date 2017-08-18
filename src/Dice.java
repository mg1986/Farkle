import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by matt on 8/3/17.
 */

public class Dice {

    // Number of sides Dice object will start with
    public int MIN = 0;
    public int numSides;

    // Dice constructor
    Dice(int sides) {
        numSides = sides;
    }

    // Roll Dice method
    public int rollDice () {
        int randomInt = ThreadLocalRandom.current().nextInt(MIN, numSides + 1);
        return randomInt;
    }
}
