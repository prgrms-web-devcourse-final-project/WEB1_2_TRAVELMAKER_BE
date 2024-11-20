package edu.example.wayfarer.repository;

import edu.example.wayfarer.entity.Marker;
import edu.example.wayfarer.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarkerRepository extends JpaRepository<Marker, String> {
}
