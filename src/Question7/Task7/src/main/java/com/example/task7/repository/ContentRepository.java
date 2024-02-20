package com.example.task7.repository;

import com.example.task7.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    @Query("SELECT c FROM Content c WHERE (:tags IS NULL OR c.tags IN :tags) ORDER BY RAND()")
    List<Content> findByTagsInOrRandomOrder(List<String> tags);
}
