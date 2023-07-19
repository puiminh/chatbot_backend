package com.project.chatbot.logic;

public abstract class Response {
    private String answer;

    public Response(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
