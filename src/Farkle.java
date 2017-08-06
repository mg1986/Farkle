
/**
 * Farkle - Dice game where the goal is to reach 10,000 points
 * Created by matt on 8/3/17.
 */

public class Farkle {
    public static void main (String[] args) {

        Player player1 = new Player("Matt");
        System.out.println(player1.playerName);

        Dice dice = new Dice(6);

        int roll = dice.rollDice();

        player1.setPlayerScore(roll);

        System.out.println(player1.getPlayerScore());

        roll = dice.rollDice();

        player1.setPlayerScore(roll);

        System.out.println(player1.getPlayerScore());


    }
}
