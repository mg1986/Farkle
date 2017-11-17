import java.util.*;

/**
 * Created by matt on 10/24/17.
 */

public class RollScoreMenu {

    public Integer rollScoreMenuKeyCounter = 1;
    public HashMap<String, String> pointType = new HashMap<String, String>();
    public HashMap<String, Integer> pointScore = new HashMap<String, Integer>();
    public HashMap<String, List<Integer>> pointIndexes = new HashMap<String, List<Integer>>();

    public RollScoreMenu() {}

    public void removeKey(String key) {
        pointType.remove(key);
        pointScore.remove(key);
        pointIndexes.remove(key);
    }

    public String createMenuKey() {
        String rollScoreMenuKey = Integer.toString(rollScoreMenuKeyCounter);
        rollScoreMenuKeyCounter++;
        return rollScoreMenuKey;
    }

    public Integer getMenuSize() {
        return pointType.size();
    }
}
