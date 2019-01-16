package com.home.tictactoemvvmdemo.utilities;


public class StringUtility {

    /** 將多個int轉String */
    public static String stringFromNumbers(int... numbers) {
        StringBuilder sNumbers = new StringBuilder();
        for (int number : numbers)
            sNumbers.append(number);
        return sNumbers.toString();
    }

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.length() == 0;
    }
}
