package softuni.xmlprocessing.domain.dtos.importDtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

@XmlRootElement(name = "customers")
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerImporRootDto {

    @XmlElement(name = "customer")
    private Set<CustomerImportDto> customers;

    public CustomerImporRootDto() {
    }

    public Set<CustomerImportDto> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<CustomerImportDto> customers) {
        this.customers = customers;
    }
}
