package com.project.chatbot.repository;

import com.project.chatbot.model.TermDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TermRepository extends MongoRepository<TermDTO, String> {
}
