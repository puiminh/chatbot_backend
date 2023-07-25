package com.project.chatbot.logic;

public class Answer {
    private String answer;
    private String intent;

    private String entity = "";

    private int entityId = -1;

    private int number = 0;

    public Answer(String answer, String intent) {
        this.answer = answer;
        this.intent = intent;
    }

    public Answer(String answer) {
        this.answer = answer;
        this.intent = "identify";

    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
