package com.project.chatbot.controller;

import com.project.chatbot.model.TagDTO;
import com.project.chatbot.repository.TagRepository;
import com.project.chatbot.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class TagController {
    @Autowired
    private TagRepository tagRepository;

    TagController(TagRepository repository) {
        this.tagRepository = repository;
    }

    @GetMapping("/tags")
    public ResponseEntity<?> getAllMessages() {
        List<TagDTO> tagDTO = tagRepository.findAll();
        if (tagDTO.size() > 0) {
            return new ResponseEntity<List<TagDTO>>(tagDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No tag available", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/tags")
    public ResponseEntity<?> createTag(@RequestBody TagDTO tagDTO) {
        try {
            tagRepository.save(tagDTO);
            return new ResponseEntity<TagDTO>(tagDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }



    @GetMapping("/tags/{id}")
    public ResponseEntity<?> getSingleTag(@PathVariable("id") String id) {
        Optional<TagDTO> tagDTOOptional = tagRepository.findById(id);
        if (tagDTOOptional.isPresent()) {
            return new ResponseEntity<>(tagDTOOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Tag not found with id: "+id, HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/tags/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") String id) {
        try {
            tagRepository.deleteById(id);
            return new ResponseEntity<>("Successfully deleted with id: "+ id, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
