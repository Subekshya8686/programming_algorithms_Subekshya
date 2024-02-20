package com.example.task7.repository;


import com.example.task7.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    // You don't need to define findAll() here, it's provided by JpaRepository
}
