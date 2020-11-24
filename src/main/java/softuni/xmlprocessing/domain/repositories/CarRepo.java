package softuni.xmlprocessing.domain.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import softuni.xmlprocessing.domain.entities.Car;

import java.util.Set;

@Repository
public interface CarRepo extends JpaRepository<Car, Long> {


    Set<Car> findAllByMakeOrderByModelAscTravelledDistanceDesc(String make);

}
