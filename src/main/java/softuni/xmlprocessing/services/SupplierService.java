package softuni.xmlprocessing.services;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public interface SupplierService {
    void seedSuppliers() throws IOException, JAXBException;

    String getAllLocalSuppliers();
}
