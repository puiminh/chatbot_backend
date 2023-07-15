package com.project.chatbot.repository;

import com.project.chatbot.model.MessageDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends MongoRepository<MessageDTO, String> {

    @Query("{'content': ?0}")
    Optional<MessageDTO> findByContent(String message);
}
