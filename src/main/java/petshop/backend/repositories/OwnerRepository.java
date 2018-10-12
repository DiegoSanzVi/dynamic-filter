package petshop.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import petshop.backend.entities.Owner;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
}
