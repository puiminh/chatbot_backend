package com.project.chatbot.controller;

import com.project.chatbot.exception.MessageCollectionException;
import com.project.chatbot.model.MessageDTO;
import com.project.chatbot.model.TermDTO;
import com.project.chatbot.repository.MessageRepository;
import com.project.chatbot.repository.TermRepository;
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

@RestController
public class TermController {
    @Autowired
    private TermRepository termRepository;

    TermController(TermRepository repository) {
        this.termRepository = repository;
    }

    @GetMapping("/terms")
    public ResponseEntity<?> getAllMessages() {
        List<TermDTO> termDTOS = termRepository.findAll();
        if (termDTOS.size() > 0) {
            return new ResponseEntity<List<TermDTO>>(termDTOS, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No term available", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/terms")
    public ResponseEntity<?> createTerm(@RequestBody TermDTO termDTO) {
        try {
            termRepository.save(termDTO);
            return new ResponseEntity<TermDTO>(termDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/terms/{id}")
    public ResponseEntity<?> getSingleTerm(@PathVariable("id") String id) {
        Optional<TermDTO> termDTOOptional = termRepository.findById(id);
        if (termDTOOptional.isPresent()) {
            return new ResponseEntity<>(termDTOOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Term not found with id: "+id, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/terms/{id}")
    public ResponseEntity<?> updateById(@PathVariable("id") String id, @RequestBody TermDTO termDTO) {
        Optional<TermDTO> termDTOOptional = termRepository.findById(id);
        if (termDTOOptional.isPresent()) {
            TermDTO termDTOToSave = termDTOOptional.get();
            termDTOToSave.setTags(termDTO.getTags().size() > 0 ? termDTO.getTags() : termDTOToSave.getTags());
            termDTOToSave.setMeaning(termDTO.getMeaning() != null ? termDTO.getTerm() : termDTOToSave.getTerm());
            termDTOToSave.setTerm(termDTO.getTerm() != null ? termDTO.getTerm() : termDTOToSave.getTerm());
            termDTOToSave.setExample(termDTO.getExample().size() > 0 ? termDTO.getExample() : termDTOToSave.getExample());

            termRepository.save(termDTOToSave);
            return new ResponseEntity<>(termDTOToSave, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Term not found with id: "+id, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/terms/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") String id) {
        try {
            termRepository.deleteById(id);
            return new ResponseEntity<>("Successfully deleted with id: "+ id, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
