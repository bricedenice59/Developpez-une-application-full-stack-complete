package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.exceptions.TopicNotFoundException;
import com.openclassrooms.mddapi.models.Topic;
import com.openclassrooms.mddapi.repositories.TopicRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class TopicService {

    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public List<Topic> getAll() {
        return topicRepository.findAll();
    }

    public Topic getById(final Integer id) {
        var theme = topicRepository.findById(id);
        return theme.orElseThrow(() -> new TopicNotFoundException("Topic not found"));
    }
}
