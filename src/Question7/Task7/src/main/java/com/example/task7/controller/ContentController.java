package com.example.task7.controller;

import com.example.task7.entity.Content;
import com.example.task7.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/content")
public class ContentController {

    @Autowired
    private ContentRepository contentRepository;

    @GetMapping
    public List<Content> getAllContent() {
        return contentRepository.findAll();
    }

    @PostMapping
    public Content addContent(@RequestBody Content content) {
        return contentRepository.save(content);
    }
}
