package softuni.xmlprocessing.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.xmlprocessing.domain.entities.Supplier;


import java.util.Set;

@Repository
public interface SupplierRepo extends JpaRepository<Supplier, Long> {
    Set<Supplier> findAllByImporterIsFalseOrderById();
}
