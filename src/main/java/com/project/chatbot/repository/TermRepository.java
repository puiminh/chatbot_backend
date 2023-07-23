package com.project.chatbot.repository;

import com.project.chatbot.model.MessageDTO;
import com.project.chatbot.model.TermDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TermRepository extends MongoRepository<TermDTO, String> {
    @Query("{'tag': ?0}")
    List<TermDTO> findAllTermWithTag(int tag);
}
