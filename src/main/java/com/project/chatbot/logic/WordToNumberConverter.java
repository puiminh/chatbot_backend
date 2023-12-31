package com.project.chatbot.logic;

import java.util.HashMap;

public class WordToNumberConverter {
    private static final HashMap<String, Integer> wordToNumberMap = new HashMap<>();

    static {
        wordToNumberMap.put("một", 1);
        wordToNumberMap.put("hai", 2);
        wordToNumberMap.put("ba", 3);
        wordToNumberMap.put("bốn", 4);
        wordToNumberMap.put("năm", 5);
        wordToNumberMap.put("sáu", 6);
        wordToNumberMap.put("bảy", 7);
        wordToNumberMap.put("tám", 8);
        wordToNumberMap.put("chín", 9);
        wordToNumberMap.put("mười", 10);
    }

    public static int convertWordToNumber(String word) {
        Integer number = wordToNumberMap.get(word);
        if (number != null) {
            return number;
        } else {
            return Integer.parseInt(word);
        }
    }

    public static int convertWordToNumber(int word) {
        return word;
    }

    public static void main(String[] args) {
       System.out.println(convertWordToNumber("1"));
    }
}