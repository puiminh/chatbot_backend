package com.project.chatbot.logic;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.chatbot.Data;
import com.project.chatbot.repository.TagRepository;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import vn.pipeline.Annotation;
import vn.pipeline.Sentence;
import vn.pipeline.VnCoreNLP;
import vn.pipeline.Word;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ChatBot {

    private List<ResponseData> responseData;
    private List<TagData> tagData;

    // Constructor to load ResponseData from JSON file
    public ChatBot() {
        // Load the JSON data from the file and populate the responseData list

        String response_data_filepath = System.getProperty("user.dir") + "/src/response_data.json";
        String tag_filepath = System.getProperty("user.dir") + "/src/tag.json";

        loadResponseDataFromJson(response_data_filepath);
        loadTagDataFromJson(tag_filepath);
    }

    // Define a method to split the input string and get the response
    // (This method is the same as provided in the previous code)

    // Define a class to represent the ResponseData
    public static class ResponseData {
        // Define attributes for the ResponseData class, you can add more as per your JSON structure
        private List<String> required_words;
        private List<String> user_input;
        private List<String> bot_response;

        private String intent;

        // Define getters and setters for the attributes
        public List<String> getRequiredWords() {
            return required_words;
        }

        public List<String> getUserInput() {
            return user_input;
        }

        public List<String> getBotResponse() {
            return bot_response;
        }

        public String getIntent() { return intent; }
    }

    // Define a class to represent the Tag
    public static class TagData {
        private int id;
        private String name;
        private List<String> keyword;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public List<String> getKeyword() {
            return keyword;
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

    private void loadTagDataFromJson(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();

            // Create a type to represent List<ResponseData>
            Type tagDataListType = new TypeToken<List<TagData>>() {}.getType();

            // Deserialize the JSON data into a List<ResponseData>
            tagData = gson.fromJson(reader, tagDataListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Define a method to split the input string and get the response
    public Answer getResponse(String inputString) throws IOException {

        Answer answer = new Answer("Xin lỗi, tôi chưa được lập trình cho trường hợp này");

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
            return new Answer("Please type something so we can chat :(");
        }

        TagData tagDefine = null;

        // If there is no good response, return a random one.
        if (bestResponse != 0) {

            //Get this intent
            answer.setIntent(responseData.get(responseIndex).getIntent());

            String pob = "";

            switch (answer.getIntent()) {
                case "learning_term":

                    String[] annotators = {"wseg", "pos", "ner", "parse"};
                    VnCoreNLP pipeline = new VnCoreNLP(annotators);
                    Annotation annotation = new Annotation(inputString);
                    pipeline.annotate(annotation);
                    List<Word> words = annotation.getWords();
                    System.out.println(words.toString());

                    //Get the Entity, Det


                    for (Word word : words) {
                        if (word.getDepLabel().equals("det")) {
                            // Get the det

                        }
                        if (word.getDepLabel().equals("pob") && !word.getPosTag().equals("P")) {
                            //Get the pob
                            pob = word.getForm();
                            System.out.println("Get the pob of Input: " + pob);
                        }
                    }

                    for (TagData tag: tagData) {
                        System.out.println("Now compare: " + tag.getKeyword().toString());

                        for (String keyword: tag.getKeyword()) {
                            if (pob.equals(keyword)) {
                                answer.setEntity(tag.getName());
                            }
                        }
                    }

                    System.out.println("Get the entity of input: " + answer.getEntity());

                    break;

                default:

                    System.out.println();
                    break;
            }

            answer.setAnswer(getRandomAnswer(responseData.get(responseIndex).getBotResponse()));
            return answer;
        }

        return answer;
    }

    public static String getRandomAnswer(List<String> array) {
        int rnd = new Random().nextInt(array.size());
        return array.get(rnd);
    }
}