package com.sisdist.common.util;

public class ConsoleUtils {
    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
