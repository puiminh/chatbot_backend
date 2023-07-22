package com.project.chatbot.service;

import com.project.chatbot.exception.MessageCollectionException;
import com.project.chatbot.logic.Answer;
import com.project.chatbot.model.MessageDTO;
import jakarta.validation.ConstraintViolationException;

import java.io.IOException;

public interface MessageService {
    public Answer createMessage(MessageDTO messageDTO) throws ConstraintViolationException, MessageCollectionException;

    public String analysisMessage(MessageDTO messageDTO) throws IOException;
}
