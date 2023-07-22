package com.project.chatbot.service;

import com.project.chatbot.exception.MessageCollectionException;
import com.project.chatbot.logic.Answer;
import com.project.chatbot.logic.ChatBot;
import com.project.chatbot.model.MessageDTO;
import com.project.chatbot.repository.MessageRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.pipeline.Annotation;
import vn.pipeline.VnCoreNLP;

import java.io.IOException;
import java.util.Date;

@Service
public class MessageServiceImpl implements MessageService{

    @Autowired
    private MessageRepository messageRepository;

    ChatBot chatBot = new ChatBot();
    @Override
    public Answer createMessage(MessageDTO messageDTO) throws ConstraintViolationException, MessageCollectionException {
//        Optional<MessageDTO> messageDTOOptional = messageRepository.findByContent(messageDTO.getContent());
//        if (messageDTOOptional.isPresent()) {
//            //Chu thich: khi ma nguoi dung nhap y het mot message da gui truoc do :v - trich tu code todo nen no khong can tiet lam
//            throw new MessageCollectionException(MessageCollectionException.SomeCustomMessage());
//        } else {
//            messageDTO.setTimestamp(new Date(System.currentTimeMillis()));
//            messageRepository.save(messageDTO);
//        }

        Answer answer;

        messageDTO.setTimestamp(new Date(System.currentTimeMillis()));
        messageRepository.save(messageDTO);

        try {
             answer = chatBot.getResponse(messageDTO.getContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return answer;
    }

    public String analysisMessage(MessageDTO messageDTO) throws IOException {
        String[] annotators = {"wseg", "pos", "ner", "parse"};
        VnCoreNLP pipeline = new VnCoreNLP(annotators);

        String str = messageDTO.getContent();
        Annotation annotation = new Annotation(str);
        pipeline.annotate(annotation);

        System.out.println(annotation.toString());

        return annotation.toString();
    }
}
