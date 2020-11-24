package softuni.xmlprocessing.services;

public interface CarService {
    void seedCars() throws Exception;

    String findByToyota();

    String getCarsWithParts();
}
