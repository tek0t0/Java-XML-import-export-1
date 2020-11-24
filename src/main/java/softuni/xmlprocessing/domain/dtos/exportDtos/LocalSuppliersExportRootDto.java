package softuni.xmlprocessing.domain.dtos.exportDtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

@XmlRootElement(name = "suppliers")
@XmlAccessorType(XmlAccessType.FIELD)
public class LocalSuppliersExportRootDto {

    @XmlElement(name = "supplier")
    private Set<LocalSuppliersExportDto> suppliers;

    public LocalSuppliersExportRootDto() {
    }

    public Set<LocalSuppliersExportDto> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(Set<LocalSuppliersExportDto> suppliers) {
        this.suppliers = suppliers;
    }
}
