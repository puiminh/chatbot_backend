package com.project.chatbot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "terms")
public class TermDTO {
    @Id
    private String id;
    private String term;

    private String meaning;
    private List<String> example;

    private List<String> tags;


    // Constructors, getters, and setters


    public TermDTO(String term, String meaning, List<String> example, List<String> tags) {
        this.term = term;
        this.meaning = meaning;
        this.example = example;
        this.tags = tags;
    }

    public TermDTO(String term, String meaning, List<String> example) {
        this.term = term;
        this.meaning = meaning;
        this.example = example;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public List<String> getExample() {
        return example;
    }

    public void setExample(List<String> example) {
        this.example = example;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
