package petshop.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import petshop.backend.entities.Animal;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
}
