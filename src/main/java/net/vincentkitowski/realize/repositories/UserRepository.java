package net.vincentkitowski.realize.repositories;

import net.vincentkitowski.realize.models.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {

    public User findByEmail(String email);
}
