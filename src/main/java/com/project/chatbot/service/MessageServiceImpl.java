package com.project.chatbot.service;

import com.project.chatbot.exception.MessageCollectionException;
import com.project.chatbot.model.MessageDTO;
import com.project.chatbot.repository.MessageRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService{

    @Autowired
    private MessageRepository messageRepository;
    @Override
    public void createMessage(MessageDTO messageDTO) throws ConstraintViolationException, MessageCollectionException {
        Optional<MessageDTO> messageDTOOptional = messageRepository.findByContent(messageDTO.getContent());
        if (messageDTOOptional.isPresent()) {
            //Chu thich: khi ma nguoi dung nhap y het mot message da gui truoc do :v - trich tu code todo nen no khong can tiet lam
            throw new MessageCollectionException(MessageCollectionException.SomeCustomMessage());
        } else {
            messageDTO.setTimestamp(new Date(System.currentTimeMillis()));
            messageRepository.save(messageDTO);
        }
    }
}
