package net.vincentkitowski.realize.repositories;

import net.vincentkitowski.realize.models.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VolunteerRepository extends JpaRepository<Volunteer, Integer> {
}
