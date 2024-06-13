package com.openclassrooms.mddapi.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.openclassrooms.mddapi.exceptions.TopicNotFoundException;
import com.openclassrooms.mddapi.models.Topic;
import com.openclassrooms.mddapi.repositories.TopicRepository;
import com.openclassrooms.mddapi.services.TopicService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TopicServiceTests {

    @Mock
    private TopicRepository topicRepository;

    @InjectMocks
    private TopicService topicService;

    @Test
    public void testGetAllThemes() {
        var theme1 = Topic.builder()
                .title("Topic 1")
                .build();
        var theme2 = Topic.builder()
                .title("Topic 2")
                .build();
        List<Topic> topics = Arrays.asList(theme1, theme2);
        when(topicRepository.findAll()).thenReturn(topics);

        List<Topic> result = topicService.getAll();

        assertEquals(2, result.size());
        assertEquals("Topic 1", result.get(0).getTitle());
        assertEquals("Topic 2", result.get(1).getTitle());
    }

    @Test
    void testGetThemeById() {
        int themeId = 1;
        var theme = Topic.builder()
                .id(themeId)
                .title("Topic 1")
                .build();
        when(topicRepository.findById(themeId)).thenReturn(Optional.of(theme));

        Topic result = topicService.getById(themeId);

        assertEquals(themeId, result.getId());
        assertEquals("Topic 1", result.getTitle());
    }

    @Test
    void testGetThemeById_ThrowsExceptionWhenNotFound() {
        int nonExistentThemeId = 99;
        when(topicRepository.findById(nonExistentThemeId)).thenReturn(Optional.empty());

        assertThrows(TopicNotFoundException.class, () -> topicService.getById(nonExistentThemeId));
    }
}
