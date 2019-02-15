package com.mg1986.farkle.controllers;

import java.util.Scanner;
import java.io.IOException;
import java.util.stream.IntStream;

/**
 * MenuController class - Controls menu input/reset
 */

public class MenuController {

    // Scanner used in all menus
    public static Scanner scanner = new Scanner(System.in);

    //------------------------------------------------------------------------------------------------------------------
    // getMenuOptionInt() - Requests user to enter an integer and will not return until user input has been properly parsed
    public static int getMenuOptionInt() {

        int menuOption = 0;

        try {
            menuOption = Integer.parseInt(scanner.next());
        } catch (NumberFormatException  ime) {
            println("Please enter a valid number and press ANY KEY to continue...");
            menuOption = getMenuOptionInt();
        }

        return menuOption;
    }

    //------------------------------------------------------------------------------------------------------------------
    // clearScreen() - Simulates clearing the screen of console by printing 50 blank lines.
    public static void clearScreen() {
        IntStream.range(0, 50).forEach(n -> { println(""); });
    }


    //------------------------------------------------------------------------------------------------------------------
    // pauseScreen() - Displays a message and then waits for user to press any key to proceed with game.
    public static void pauseScreen() {
        println("---------------------------------------------------------------------");
        println("Press ANY KEY to continue...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // clearPrintPauseScreen() - Clears screen, prints a message, and then waits for user to press ANY KEY to proceed
    //                           with game.
    public static void clearPrintPauseScreen(String messageToPrint) {
        clearScreen();
        println(messageToPrint);
        pauseScreen();
    }

    //------------------------------------------------------------------------------------------------------------------
    // println() - Shortens System.out.println() statements
    public static void println(String messageToPrint) {
        System.out.println(messageToPrint);
    }
}
