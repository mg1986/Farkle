import java.util.*;

/**
 * Created by matt on 10/24/17.
 */

public class DiceRollMenu {

    public Integer diceRollMenuKeyCounter = 1;
    public HashMap<String, String> pointType = new HashMap<String, String>();
    public HashMap<String, Integer> pointScore = new HashMap<String, Integer>();
    public HashMap<String, List<Integer>> pointIndexes = new HashMap<String, List<Integer>>();

    public DiceRollMenu() {}

    public void removeKey(String key) {
        pointType.remove(key);
        pointScore.remove(key);
        pointIndexes.remove(key);
    }

    public String createDiceRollMenuKey() {
        String diceRollMenuKey = Integer.toString(diceRollMenuKeyCounter);
        diceRollMenuKeyCounter++;
        return diceRollMenuKey;
    }

    public Integer getMenuSize() {
        return pointType.size();
    }
}
