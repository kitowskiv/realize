package net.vincentkitowski.realize.repositories;

import net.vincentkitowski.realize.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<Post, Integer> {
}
