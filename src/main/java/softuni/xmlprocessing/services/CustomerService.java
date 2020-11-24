package softuni.xmlprocessing.services;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public interface CustomerService {
    void seedCustomers() throws IOException, JAXBException;

    String exportOrderedCustomers() throws JAXBException;

    String getAllCustomersByTotalSales();
}
