package softuni.xmlprocessing.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.xmlprocessing.domain.entities.Sale;


import java.util.Set;

@Repository
public interface SalesRepo extends JpaRepository<Sale, Long> {
    Set<Sale> getAllBy();
}
