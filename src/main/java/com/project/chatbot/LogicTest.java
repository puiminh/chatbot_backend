package com.project.chatbot;

import com.project.chatbot.logic.ChatBot;

import java.io.IOException;

public class LogicTest {
    public static void main(String[] args) throws IOException {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        ChatBot chatBot = new ChatBot();

        System.out.println(chatBot.getResponse("Cho tôi 3 từ vựng về kinh doanh").toString());
    }
}
