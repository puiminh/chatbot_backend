package com.project.chatbot.logic;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatBot {

    private List<ResponseData> responseData;

    // Constructor to load ResponseData from JSON file
    public ChatBot(String filePath) {
        // Load the JSON data from the file and populate the responseData list
        loadResponseDataFromJson(filePath);
    }

    // Define a method to split the input string and get the response
    // (This method is the same as provided in the previous code)

    // Define a class to represent the ResponseData
    public static class ResponseData {
        // Define attributes for the ResponseData class, you can add more as per your JSON structure
        private List<String> required_words;
        private List<String> user_input;
        private String bot_response;

        // Define getters and setters for the attributes
        public List<String> getRequiredWords() {
            return required_words;
        }

        public List<String> getUserInput() {
            return user_input;
        }

        public String getBotResponse() {
            return bot_response;
        }
    }

    // Method to load ResponseData from a JSON file
    private void loadResponseDataFromJson(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();

            // Create a type to represent List<ResponseData>
            Type responseDataListType = new TypeToken<List<ResponseData>>() {}.getType();

            // Deserialize the JSON data into a List<ResponseData>
            responseData = gson.fromJson(reader, responseDataListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Define a method to split the input string and get the response
    public String getResponse(String inputString) {
        List<String> splitMessage = Arrays.asList(inputString.toLowerCase().split("\\s+|[,;?!.-]\\s*"));
        List<Integer> scoreList = new ArrayList<>();

        // Check all the responses
        for (ResponseData response : responseData) {
            int responseScore = 0;
            int requiredScore = 0;
            List<String> requiredWords = response.getRequiredWords();

            // Check if there are any required words
            if (!requiredWords.isEmpty()) {
                for (String word : splitMessage) {
                    if (requiredWords.contains(word)) {
                        requiredScore++;
                    }
                }
            }

            // Amount of required words should match the required score
            if (requiredScore == requiredWords.size()) {
                // Check each word the user has typed
                for (String word : splitMessage) {
                    // If the word is in the response, add to the score
                    if (response.getUserInput().contains(word)) {
                        responseScore++;
                    }
                }
            }

            // Add score to list
            scoreList.add(responseScore);
        }

        // Find the best response and return it if they're not all 0
        int bestResponse = 0;
        int responseIndex = 0;
        for (int i = 0; i < scoreList.size(); i++) {
            int score = scoreList.get(i);
            if (score > bestResponse) {
                bestResponse = score;
                responseIndex = i;
            }
        }

        // Check if input is empty
        if (inputString.isEmpty()) {
            return "Please type something so we can chat :(";
        }

        // If there is no good response, return a random one.
        if (bestResponse != 0) {
            return responseData.get(responseIndex).getBotResponse();
        }

        return "...";
    }
}