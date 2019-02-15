package com.mg1986.farkle.ui;

import com.mg1986.farkle.components.Player;
import com.mg1986.farkle.components.Scoreboard;
import static com.mg1986.farkle.controllers.DiceController.*;
import static com.mg1986.farkle.controllers.GameController.*;
import static com.mg1986.farkle.controllers.MenuController.*;
import static com.mg1986.farkle.controllers.RuleBookController.*;
import static com.mg1986.farkle.controllers.ScoreboardController.*;

/**
 * TurnMenu class - Turn menu for the game. Allows players to:
 *                      1. Roll dice
 *                      2. Bank points and end turn
 *                      3. View Scoreboard
 *                      4. View Rules
 *                      5. Save and exit game
 */

public class TurnMenu {

    //------------------------------------------------------------------------------------------------------------------
    // startPlayerTurn() - Called every time a players turn happens.  Continues until the player Farkles or chooses to
    //                     end their turn and keep their current turn's points.
    public static void startPlayerTurn(Scoreboard scoreboard, Player player) {

        boolean playersTurnStillActive = true;

        while (playersTurnStillActive) {

            clearScreen();

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
                    playersTurnStillActive = checkForFarkle(player);
                    break;
                case 2:
                    playersTurnStillActive = checkScoreboardAndBankPoints(player, scoreboard);
                    break;
                case 3:
                    printScoreboard(scoreboard);
                    break;
                case 4:
                    printRules();
                    break;
                case 5:
                    saveGame(scoreboard);
                    break;
                default:
                    println("Please press 1-5 to proceed with turn.");
                    break;
            }
        }
    }
}
