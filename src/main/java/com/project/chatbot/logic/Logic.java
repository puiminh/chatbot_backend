package com.project.chatbot.logic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
public class Logic {

    private static void method1() {
        System.out.println("Running method 1");
    }

    private static int messageProbability(String[] userMessage, String[] recognisedWords, boolean singleResponse, String[] requiredWords) {
        int messageCertainty = 0;
        boolean hasRequiredWords = true;

        // Check if recognisedWords and requiredWords are null
        recognisedWords = recognisedWords != null ? recognisedWords : new String[0];
        requiredWords = requiredWords != null ? requiredWords : new String[0];

        // Counts how many words are present in each predefined message
        for (String word : userMessage) {
            for (String recognisedWord : recognisedWords) {
                if (recognisedWord.equals(word)) {
                    messageCertainty += 1;
                    break;
                }
            }
        }

        // Print recognised words and required words for debugging
        System.out.println("Recognised words: " + Arrays.toString(recognisedWords));
        System.out.println("Required words: " + Arrays.toString(requiredWords));


        // Calculates the percent of recognised words in a user message
        double percentage = (double) messageCertainty / (double) recognisedWords.length;


        // Print message certainty and percentage
        System.out.println("Message Certainty: " + messageCertainty);
        System.out.println("Percentage: " + ((double) messageCertainty / (double) recognisedWords.length));


        // Checks that the required words are in the string
        for (String requiredWord : requiredWords) {
            boolean found = false;
            for (String word : userMessage) {
                if (requiredWord.equals(word)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                hasRequiredWords = false;
                break;
            }
        }

        // Must either have the required words, or be a single response
        if (hasRequiredWords || singleResponse) {
            return (int) (percentage * 100);
        } else {
            return 0;
        }


    }

    private static void response(String[] userMessage, Map<String, Integer> highestProbList, String botResponse, String[] listOfWords, boolean singleResponse, String[] requiredWords, Runnable method) {
        int probability = messageProbability(userMessage, listOfWords, singleResponse, requiredWords);
        method.run();
        highestProbList.put(botResponse, probability);

        // Print bot response, probability, and list of words for debugging
//        System.out.println("Bot Response: " + botResponse);
//        System.out.println("Probability: " + probability);
//        System.out.println("List of Words: " + Arrays.toString(listOfWords));
    }

    private static void response(String[] userMessage, Map<String, Integer> highestProbList, String botResponse, String[] listOfWords, boolean singleResponse, String[] requiredWords) {
        int probability = messageProbability(userMessage, listOfWords, singleResponse, requiredWords);
        highestProbList.put(botResponse, probability);

        // Print bot response, probability, and list of words for debugging
        System.out.println("Bot Response: " + botResponse);
        System.out.println("Probability: " + probability);
        System.out.println("List of Words: " + Arrays.toString(listOfWords));
    }

    public static String getResponse(String userInput) {
        String[] splitMessage = userInput.toLowerCase().split("[\\s,;?!.-]+");
        return checkAllMessages(splitMessage);
    }


    public static String checkAllMessages(String[] message) {
        Map<String, Integer> highestProbList = new HashMap<>();

        // Responses -------------------------------------------------------------------------------------------------------
        response(message, highestProbList, "Chào bạn!", new String[]{"chào", "hello", "alo"}, true, null);

        response(message, highestProbList, "Tạm biệt!", new String[]{"tạm biệt", "hẹn gặp lại"}, true, null);
        response(message, highestProbList, "Tôi dạo này vẫn khỏe, còn bạn thì sao?", new String[]{"bạn", "dạo", "này", "thế", "nào"}, false, new String[]{"how"});

        response(message, highestProbList, "Không cần khách sáo, đó là công việc của tôi mà!", new String[]{"thank", "thanks"}, true, null);

        response(message, highestProbList, "Vâng, đây sau đây là một số từ vựng liên quan đến mèo: \n", new String[]{"mèo", "từ vựng"}, true, null, Logic::method1);

        // Print the highest probability list for debugging
        System.out.println("Highest Probability List: " + highestProbList);

        String bestMatch = highestProbList.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Xin lỗi, tôi chưa được lập trình cho việc này");

        // Print the best match for debugging
        System.out.println("Best Match: " + bestMatch);

        return highestProbList.get(bestMatch) < 1 ? "Xin lỗi, tôi chưa được lập trình cho việc này" : bestMatch;
    }
}
