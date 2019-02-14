package com.mg1986.farkle.managers;

import java.io.IOException;
import java.util.Scanner;
import java.util.stream.IntStream;

public class MenuManager {

    public static Scanner scanner = new Scanner(System.in);

    //------------------------------------------------------------------------------------------------------------------
    // getMenuOptionInt() - Requests user to enter an integer and will not return until user input has been properly
    //                      parsed.
    public static int getMenuOptionInt(Scanner scanner) {

        int menuOption = 0;

        try {
            menuOption = Integer.parseInt(scanner.next());
        } catch (NumberFormatException  ime) {
            System.out.println("Please enter a valid number and press ENTER to continue");
            menuOption = getMenuOptionInt(scanner);
        }

        return menuOption;
    }

    //------------------------------------------------------------------------------------------------------------------
    // clearScreen() - Simulates clearing the screen of console by printing 50 blank lines.
    public static void clearScreen() {
        IntStream.range(0, 50).forEach(n -> { System.out.println(); });
    }


    //------------------------------------------------------------------------------------------------------------------
    // pauseScreen() - Displays a message and then waits for user to press ENTER key to proceed with game.
    public static void pauseScreen() {
        System.out.println("-----------------------------------------------");
        System.out.println("Press ENTER to continue...");
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
        System.out.println(messageToPrint);
        pauseScreen();
    }
}
