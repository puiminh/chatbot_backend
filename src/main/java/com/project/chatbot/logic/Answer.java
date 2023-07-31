package com.project.chatbot.logic;

import com.project.chatbot.model.TermDTO;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Answer {
    private String answer;
    private String intent;

    private String entity = "";

    private int entityId = -1;

    private int number = 0;

    private List<TermDTO> listTerm;

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

    public List<TermDTO> getListTerm() {
        return listTerm;
    }

    public void setListTerm(List<TermDTO> listTerm) {
        this.listTerm = listTerm;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "answer='" + answer + '\'' +
                ", intent='" + intent + '\'' +
                ", entity='" + entity + '\'' +
                ", entityId=" + entityId +
                ", number=" + number +
                ", listTerm=" + listTerm +
                '}';
    }
}
