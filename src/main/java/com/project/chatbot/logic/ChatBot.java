package com.project.chatbot.logic;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import vn.pipeline.Annotation;
import vn.pipeline.VnCoreNLP;
import vn.pipeline.Word;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.project.chatbot.logic.WordToNumberConverter.convertWordToNumber;

public class ChatBot {

    private List<ResponseData> responseData;
    private List<TagData> tagData;

    // Constructor to load ResponseData from JSON file
    public ChatBot() {
        // Load the JSON data from the file and populate the responseData list

        loadData();
    }

    String response_data_filepath = System.getProperty("user.dir") + "/src/response_data.json";
    String tag_filepath = System.getProperty("user.dir") + "/src/tag.json";

    public void learn(String before, String content) {

        try (FileReader reader = new FileReader(response_data_filepath)) {
            Gson gson = new Gson();

            // Create a type to represent List<ResponseData>
            Type responseDataListType = new TypeToken<List<ResponseData>>() {}.getType();

            // Deserialize the JSON data into a List<ResponseData>
            List<ResponseData> existingData = gson.fromJson(reader, responseDataListType);

            // Add new data to the existing list
            if (existingData == null) {
                existingData = new ArrayList<>();
            }

            List<String> splitBefore = Arrays.asList(before.toLowerCase().split("\\s+|[,;?!.-]\\s*"));
            List<String> splitContent = Arrays.asList(content.toLowerCase().split("\\s+|[,;?!.-]\\s*"));


            ResponseData newResponseData = new ResponseData();
            newResponseData.setIntent(before);
            newResponseData.setUser_input(splitBefore);
            newResponseData.setBot_response(Arrays.asList(content));
            newResponseData.setRequired_words(Arrays.asList());

            existingData.add(newResponseData);


            // Write the updated list back to the JSON file
            try (FileWriter writer = new FileWriter(response_data_filepath)) {
                gson.toJson(existingData, responseDataListType, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
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

        public void setRequired_words(List<String> required_words) {
            this.required_words = required_words;
        }

        public void setUser_input(List<String> user_input) {
            this.user_input = user_input;
        }

        public void setBot_response(List<String> bot_response) {
            this.bot_response = bot_response;
        }

        public void setIntent(String intent) {
            this.intent = intent;
        }
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

    public void loadData() {


        loadResponseDataFromJson(response_data_filepath);
        loadTagDataFromJson(tag_filepath);
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

            // Amount of required words should match the required score, if not, the score = 0
            if (requiredScore == requiredWords.size()) {
                // Check each word the user has typed
                for (String word : splitMessage) {
                    // If the word is in the response (bot required this), add to the score
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
            return new Answer("Xin lỗi, nhưng bạn đang chưa nhập tin nhắn của bạn");
        }

        TagData tagDefine = null;

        // If there is no good response, return a random one.
        if (bestResponse != 0) {

            //Get this intent
            answer.setIntent(responseData.get(responseIndex).getIntent());

            String pob = "";
            int det = 10;

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
                            det = convertWordToNumber(word.getForm());

                            if (det != 0) {
                                answer.setNumber(det);
                            } else {
                                answer.setNumber(4);
                            }
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
                                answer.setEntityId(tag.getId());
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