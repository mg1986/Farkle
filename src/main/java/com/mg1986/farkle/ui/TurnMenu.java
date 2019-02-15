package com.mg1986.farkle.ui;

import com.mg1986.farkle.components.Player;
import com.mg1986.farkle.components.RuleBook;
import com.mg1986.farkle.components.Scoreboard;
import static com.mg1986.farkle.controllers.DiceController.*;
import static com.mg1986.farkle.controllers.ScoreboardController.*;
import static com.mg1986.farkle.controllers.MenuController.*;

/**
 * TurnMenu class - Class that handles the logic of a Player's turn.  This involves rolling dice and then resolving
 *                    the point scoring combinations for each roll until they Farkle or end their turn voluntarily.
 */

public class TurnMenu {

    //------------------------------------------------------------------------------------------------------------------
    // startPlayerTurn() - Called every time a players turn happens.  Continues until the player Farkles or chooses
    //                     to end their turn and keep their current turn's points.
    public static void startPlayerTurn(Scoreboard scoreboard, Player player) {

        boolean playersTurnStillActive = true;

        while (playersTurnStillActive) {

            clearScreen();

            if (player.getNumDiceInUse() == MAX_NUM_DICE) { rerollDice(player); }

            int numDiceInHand = MAX_NUM_DICE - player.getNumDiceInUse();

            println("It is " + player.getName() + "'s turn\n" +
                    player.getName() + "'s current score for this turn: " + player.getTurnScore() + "\n" +
                    player.getName() + "'s current number of dice available to roll: " + numDiceInHand + "\n" +
                    "------------------------------------------------------------------------");

            println("1. Roll dice\n" +
                    "2. Bank points and end turn\n" +
                    "3. View Scoreboard\n" +
                    "4. View Rules\n" +
                    "5. Save and exit game");

            int menuOption = getMenuOptionInt();

            switch (menuOption) {
                case 1:
                    rollAndAnalyzeDice(player, numDiceInHand);
                    playersTurnStillActive = checkforFarkle(player);
                    break;
                case 2:
                    playersTurnStillActive = checkScoreboardAndBankPoints(player, scoreboard);
                    break;
                case 3:
                    scoreboard.printScoreboard();
                    break;
                case 4:
                    RuleBook.viewRulebook();
                    break;
                case 5:
                    saveScoreboard(scoreboard);
                    break;
                default:
                    println("Please press 1-5 to proceed with turn.");
                    break;
            }
        }
    }
}
