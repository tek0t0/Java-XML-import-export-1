package softuni.xmlprocessing.domain.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.xmlprocessing.domain.entities.Part;

@Repository
public interface PartRepo extends JpaRepository<Part, Long> {
}
