package com.project.chatbot.service;

import com.project.chatbot.exception.MessageCollectionException;
import com.project.chatbot.model.MessageDTO;
import jakarta.validation.ConstraintViolationException;

public interface MessageService {
    public void createMessage(MessageDTO messageDTO) throws ConstraintViolationException, MessageCollectionException;
}
