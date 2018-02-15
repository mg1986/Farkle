/**
 * Rulebook class - Rulebook for game of Farkle
 */

public class RuleBook {

    // Rules for game of Farkle
    private static final String gameRules = "Farkle Rules:\n" +
            "   The goal of Farkle is to reach 10,000 points before other players. In a single turn\n" +
            "a player may keep rolling as long as they score some scoring combination of dice every\n" +
            "roll.  At least one scoring point combination must be kept every roll to keep rolling. \n" +
            "The dice associated with each scoring combination are set aside before the next roll.\n" +
            "Once all 6 dice have been tied up in point combinations, the player must reroll all 6 and \n" +
            "keep going at least one more roll no matter what, while maintaining the previous roll's point.   " +
            "Until a player initially reaches 1,000 points in one turn, they may not stop their turn and bank " +
            "their points.  However, once the 1,000 point mark has been reached in one turn, the player may choose " +
            "to stop their turn at any time and bank that current turn's points to the scoreboard, as well as stop " +
            "an future turn at any point and bank any points on hand.  If a player rolls no scoring combination " +
            "of dice on a roll, that is called a Farkle and their turn is over. The first player to reach 10,000" +
            " points wins the game!\n" +
            "\n" +
            "Possible point combinations: \n" +
            "One - 100 points\n" +
            "Five - 50 points\n" +
            "3-6 of a Kind - Three 1's equals 1,000 points, every additional 1 after that doubles the points.\n" +
            "                Three of anything other than a 1 equals that number multiplied by 100, every additional\n" +
            "                dice of that number after that keeps doubles the point total.\n" +
            "Three pairs of Two - 1,500 points\n" +
            "Straight - 1,500 points\n ";

    //------------------------------------------------------------------------------------------------------------------
    // viewGameRules() - Return String of game rules
    public static void viewRulebook() {
        MainMenu.menuPrint(gameRules);
    }
}
