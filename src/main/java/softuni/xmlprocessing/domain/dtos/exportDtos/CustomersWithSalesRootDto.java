package softuni.xmlprocessing.domain.dtos.exportDtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

@XmlRootElement(name = "customers")
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomersWithSalesRootDto {

    @XmlElement(name = "customer")
    private Set<CustomerWithSalesExportDto> customers;

    public CustomersWithSalesRootDto() {
    }

    public Set<CustomerWithSalesExportDto> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<CustomerWithSalesExportDto> customers) {
        this.customers = customers;
    }
}
