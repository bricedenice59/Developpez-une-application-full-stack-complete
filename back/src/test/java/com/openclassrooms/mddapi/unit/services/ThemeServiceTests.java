package com.openclassrooms.mddapi.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.openclassrooms.mddapi.exceptions.ThemeNotFoundException;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.repositories.ThemeRepository;
import com.openclassrooms.mddapi.services.ThemeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ThemeServiceTests {

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private ThemeService themeService;

    @Test
    public void testGetAllThemes() {
        var theme1 = Theme.builder()
                .title("Theme 1")
                .build();
        var theme2 = Theme.builder()
                .title("Theme 2")
                .build();
        List<Theme> themes = Arrays.asList(theme1, theme2);
        when(themeRepository.findAll()).thenReturn(themes);

        List<Theme> result = themeService.getAll();

        assertEquals(2, result.size());
        assertEquals("Theme 1", result.get(0).getTitle());
        assertEquals("Theme 2", result.get(1).getTitle());
    }

    @Test
    void testGetThemeById() {
        int themeId = 1;
        var theme = Theme.builder()
                .id(themeId)
                .title("Theme 1")
                .build();
        when(themeRepository.findById(themeId)).thenReturn(Optional.of(theme));

        Theme result = themeService.getById(themeId);

        assertEquals(themeId, result.getId());
        assertEquals("Theme 1", result.getTitle());
    }

    @Test
    void testGetThemeById_ThrowsExceptionWhenNotFound() {
        int nonExistentThemeId = 99;
        when(themeRepository.findById(nonExistentThemeId)).thenReturn(Optional.empty());

        assertThrows(ThemeNotFoundException.class, () -> themeService.getById(nonExistentThemeId));
    }
}
