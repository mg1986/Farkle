import java.util.*;

/**
 * DiceRollMenu class - Simulates menu used to resolve scoring after each players roll.
 */

public class DiceRollMenu {

    // Used to build menu item indexes
    public Integer diceRollMenuKeyCounter = 1;

    // Stores the string description of the point type/amount, using diceRollMenuKeyCounter as the key
    public HashMap<String, String> pointType = new HashMap<String, String>();

    // Stores the actual integer value of the point type (See RuleBook class for all possible point amounts)
    public HashMap<String, Integer> pointScore = new HashMap<String, Integer>();

    // Stores a list of all dice roll indexes of the dice from the current roll that the point type uses.
    public HashMap<String, List<Integer>> pointIndexes = new HashMap<String, List<Integer>>();

    // DiceRollMenu constructor
    public DiceRollMenu() {}

    //------------------------------------------------------------------------------------------------------------------
    // removeKey() - removes items from all DiceRollMenu sub-menus when a player chooses to keep that point scoring
    //               combination
    public void removeKey(String key) {
        pointType.remove(key);
        pointScore.remove(key);
        pointIndexes.remove(key);
    }

    //------------------------------------------------------------------------------------------------------------------
    // createDiceRollMenuKey() - Generates an incrementing index for items in sub-menus each time the number of items
    //                           changes
    public String createDiceRollMenuKey() {
        String diceRollMenuKey = Integer.toString(diceRollMenuKeyCounter);
        diceRollMenuKeyCounter++;
        return diceRollMenuKey;
    }

    //------------------------------------------------------------------------------------------------------------------
    // getMenuSize() - Returns the number of items currently found in DiceRollMenu sub-menus
    public Integer getMenuSize() {
        return pointType.size();
    }
}
