package com.group.grouprolesecurity.repository;

import com.group.grouprolesecurity.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Integer> {
}
