/**
 * Created by matt on 9/28/17.
 */

public class RuleBook {

    public static final String gameRules = "Farkle Rules:\n" +
            "The goal of Farkle is to reach 10,000 points before other players. In a single turn\n" +
            "a player may keep rolling as long as they score some scoring combination of dice every\n" +
            "roll.  At least one scoring point combination must be kept every roll to keep rolling. \n" +
            "The dice associated with each scoring combination are set aside before the next roll.\n" +
            "Once all 6 dice have been tied up in point combinations, the player must reroll all 6 and \n" +
            "keep going one more roll no matter what.  The point totals from the previous rolls are kept \n" +
            "as the player rerolls with all 6 dice.  Until a player initially reaches 1,000 points, they \n" +
            "may not stop their turn and bank their points.  However, once the 1,000 point mark has been\n" +
            "reached in one turn, the player may choose to stop their turn at any time and bank that\n" +
            "current turn's points to the scoreboard, as well as stop an future turn at any point and bank\n" +
            "any points on hand.  If a player rolls no scoring combination of dice on a roll, that is called\n"+ 
	    "a Farkle and their turn is over. The first player to reach 10,000 points wins the game!\n" +
            "\n" +
            "Possible point combinations: \n" +
            "One - 100 points\n" +
            "Five - 50 points\n" +
            "3-6 of a Kind - Three 1's equals 1,000 points, every additional 1 after that doubles the points.\n" +
            "                Three of anything other than a 1 equals that number multiplied by 100, every additional\n" +
            "                dice of that number after that doubles the points.\n" +
            "Three pairs of Two - 1,500 points\n" +
            "Two pairs of Three - 1,500 points\n" +
            "Straight - 2,500 points\n ";
}
