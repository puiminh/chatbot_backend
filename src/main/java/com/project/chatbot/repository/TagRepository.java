package com.project.chatbot.repository;

import com.project.chatbot.model.TagDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TagRepository extends MongoRepository<TagDTO, String> {
}
