package com.project.chatbot.service;

import com.project.chatbot.exception.MessageCollectionException;
import com.project.chatbot.logic.Answer;
import com.project.chatbot.logic.ChatBot;
import com.project.chatbot.model.MessageDTO;
import com.project.chatbot.model.TermDTO;
import com.project.chatbot.repository.MessageRepository;
import com.project.chatbot.repository.TermRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.pipeline.Annotation;
import vn.pipeline.VnCoreNLP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService{

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private TermRepository termRepository;

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

        if (answer.getIntent().equals("learning_term")) {
            List<TermDTO> listTerm = termRepository.findAllTermWithTag(answer.getEntityId());

            if (answer.getNumber() == 0) answer.setNumber(5);
            answer.setListTerm(pickNRandom(listTerm, answer.getNumber()));
            if (answer.getListTerm().size() != 0) {
                answer.setAnswer("Sau đây là " + answer.getNumber() + " từ vựng: ");
            } else {
                answer.setAnswer("Xin lỗi trong cơ sở dữ liệu hiện tại bọn tôi chưa có");
            }
        } else {
            answer.setAnswer(answer.getAnswer());
        }
        return answer;


    }

    @Override
    public Answer learnFromUser(MessageDTO messageDTO) throws ConstraintViolationException {

        Answer answer;

        messageDTO.setTimestamp(new Date(System.currentTimeMillis()));
        messageRepository.save(messageDTO);

        if (messageDTO.getContent().toLowerCase().equals("skip")) {
            //Nguoi dung skip

        } else {
            chatBot.learn(messageDTO.getBefore(), messageDTO.getContent());

            chatBot.loadData();
        }

        answer = new Answer("Cảm ơn bạn đã đóng góp cho chatbot");

        return answer;
    }

    public void reloadData() {
        chatBot.loadData();
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

    public static List<TermDTO> pickNRandom(List<TermDTO> lst, int n) {
        List<TermDTO> copy = new ArrayList<TermDTO>(lst);
        Collections.shuffle(copy);
        return n > copy.size() ? copy.subList(0, copy.size()) : copy.subList(0, n);
    }
}
