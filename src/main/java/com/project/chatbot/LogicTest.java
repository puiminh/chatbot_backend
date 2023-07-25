package com.project.chatbot;

import com.project.chatbot.logic.ChatBot;
import com.project.chatbot.logic.Logic;

import java.io.IOException;

public class LogicTest {
    public static void main(String[] args) throws IOException {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        ChatBot chatBot = new ChatBot();

        System.out.println(chatBot.getResponse("Một một một").getAnswer());

        chatBot.learn("Một một một", "hai hai hai");

        chatBot.loadData();

        System.out.println(chatBot.getResponse("Một một một").getAnswer());
    }
}
