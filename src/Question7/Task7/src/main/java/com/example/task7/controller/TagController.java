package com.example.task7.controller;

import com.example.task7.entity.Tag;
import com.example.task7.repository.TagRepository;
import com.example.task7.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagRepository tagRepository;
    private final TagService tagService;

    @Autowired
    public TagController(TagRepository tagRepository, TagService tagService) {
        this.tagRepository = tagRepository;
        this.tagService = tagService;
    }

    @GetMapping
    public List<Tag> getAllTags() {
        return tagService.getAllTags();
    }

    @PostMapping("/post")
    public Tag createTag(@RequestBody Tag tag) {
        return tagRepository.save(tag);
    }
}

