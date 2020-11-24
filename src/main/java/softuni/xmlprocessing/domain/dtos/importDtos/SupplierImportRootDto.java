package softuni.xmlprocessing.domain.dtos.importDtos;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "suppliers")
@XmlAccessorType(XmlAccessType.FIELD)
public class SupplierImportRootDto {

    @XmlElement(name = "supplier")
    private List<SupplierImportDto> suppliers;


    public SupplierImportRootDto() {
    }

    public List<SupplierImportDto> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<SupplierImportDto> suppliers) {
        this.suppliers = suppliers;
    }
}
