package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.exceptions.ThemeNotFoundException;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.repositories.ThemeRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<Theme> getAll() {
        return themeRepository.findAll();
    }

    public Theme getById(final Integer id) {
        var theme = themeRepository.findById(id);
        return theme.orElseThrow(() -> new ThemeNotFoundException("Theme not found"));
    }
}
