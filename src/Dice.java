import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by matt on 8/3/17.
 */

public class Dice {

    // Number of sides Dice object will start with
    public int numSides;
    public int MIN = 0;

    // Dice constructor
    Dice(int sides) {
        numSides = sides;
    }

    // Roll Dice method
    public int rollDice () {
        int randomNum = ThreadLocalRandom.current().nextInt(MIN, numSides + 1);
        return randomNum;
    }
}
