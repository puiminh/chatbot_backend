package com.project.chatbot.controller;

import com.project.chatbot.exception.MessageCollectionException;
import com.project.chatbot.logic.Answer;
import com.project.chatbot.model.MessageDTO;
import com.project.chatbot.repository.MessageRepository;
import com.project.chatbot.service.MessageService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://127.0.0.1:5500", maxAge = 3600)
@RestController
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private MessageService messageService;

    MessageController(MessageRepository repository) {
        this.messageRepository = repository;
    }

    @GetMapping("/messages")
    public ResponseEntity<?> getAllMessages() {
        List<MessageDTO> messages = messageRepository.findAll();
        if (messages.size() > 0) {
            return new ResponseEntity<List<MessageDTO>>(messages, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No messages available", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/reload_data")
    public ResponseEntity<?> reloadData() {
        messageService.reloadData();
        return new ResponseEntity<>("Reload success", HttpStatus.OK);
    }

    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody MessageDTO message) {
        try {
            Answer res = messageService.createMessage(message);
            return new ResponseEntity<Answer>(res, HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (MessageCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/learn")
    public ResponseEntity<?> learnFromUser(@RequestBody MessageDTO message) {
        try {
            Answer res = messageService.createMessage(message);
            return new ResponseEntity<Answer>(res, HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (MessageCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/analysis_messages")
    public ResponseEntity<?> analysisMessage(@RequestBody MessageDTO message) {
        try {
            String analysis = messageService.analysisMessage(message);
            return new ResponseEntity<>(analysis, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


    @GetMapping("/messages/{id}")
    public ResponseEntity<?> getSingleMessage(@PathVariable("id") String id) {
        Optional<MessageDTO> messageDTOOptional = messageRepository.findById(id);
        if (messageDTOOptional.isPresent()) {
            return new ResponseEntity<>(messageDTOOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Message not found with id: "+id, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/messages/{id}")
    public ResponseEntity<?> updateById(@PathVariable("id") String id, @RequestBody MessageDTO messageDTO) {
        Optional<MessageDTO> messageDTOOptional = messageRepository.findById(id);
        if (messageDTOOptional.isPresent()) {
            MessageDTO messageDTOToSave = messageDTOOptional.get();
            messageDTOToSave.setTimestamp(new Date(System.currentTimeMillis()));
            messageDTOToSave.setContent(messageDTO.getContent() != null ? messageDTO.getContent() : messageDTOToSave.getContent());

            messageRepository.save(messageDTOToSave);
            return new ResponseEntity<>(messageDTOToSave, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Message not found with id: "+id, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/messages/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") String id) {
        try {
            messageRepository.deleteById(id);
            return new ResponseEntity<>("Successfully deleted with id: "+ id, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
