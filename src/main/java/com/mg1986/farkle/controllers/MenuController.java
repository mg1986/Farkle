package com.mg1986.farkle.controllers;

import java.io.IOException;
import java.util.Scanner;
import java.util.stream.IntStream;

public class MenuController {

    public static Scanner scanner = new Scanner(System.in);

    //------------------------------------------------------------------------------------------------------------------
    // getMenuOptionInt() - Requests user to enter an integer and will not return until user input has been properly
    //                      parsed.
    public static int getMenuOptionInt() {

        int menuOption = 0;

        try {
            menuOption = Integer.parseInt(scanner.next());
        } catch (NumberFormatException  ime) {
            println("Please enter a valid number and press ENTER to continue");
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
    // pauseScreen() - Displays a message and then waits for user to press ENTER key to proceed with game.
    public static void pauseScreen() {
        println("-----------------------------------------------");
        println("Press ENTER to continue...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // clearPrintPauseScreen() - Clears screen, prints a message, and then waits for user to press ENTER to proceed with
    //                           game.
    public static void clearPrintPauseScreen(String messageToPrint) {
        clearScreen();
        println(messageToPrint);
        pauseScreen();
    }

    public static void println(String messageToPrint) {
        System.out.println(messageToPrint);
    }
}
