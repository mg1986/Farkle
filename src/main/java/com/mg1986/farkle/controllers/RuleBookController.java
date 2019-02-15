package com.mg1986.farkle.controllers;

import com.mg1986.farkle.components.RuleBook;
import static com.mg1986.farkle.controllers.MenuController.*;

/**
 * RuleBookController class - Controls RuleBook logic
 */

public class RuleBookController {

    //------------------------------------------------------------------------------------------------------------------
    // printRules() - Print String of game rules
    public static void printRules() {
        clearScreen();
        println(RuleBook.gameRules);
        pauseScreen();
    }
}
