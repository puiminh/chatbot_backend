package com.project.chatbot;

import com.project.chatbot.logic.ChatBot;
import com.project.chatbot.logic.Logic;

import java.io.IOException;

public class LogicTest {
    public static void main(String[] args) throws IOException {
        ChatBot chatBot = new ChatBot("D:\\gr1\\src\\main\\java\\com\\project\\chatbot\\logic\\response_data.json");
        System.out.println(chatBot.getResponse("Hello there"));
    }
}
