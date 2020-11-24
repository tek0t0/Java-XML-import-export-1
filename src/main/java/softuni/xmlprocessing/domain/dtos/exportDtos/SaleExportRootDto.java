package softuni.xmlprocessing.domain.dtos.exportDtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;


@XmlRootElement(name = "sales")
@XmlAccessorType(XmlAccessType.FIELD)
public class SaleExportRootDto {

    @XmlElement(name = "sale")
    private Set<SaleExportDto> sales;

    public SaleExportRootDto() {
    }

    public Set<SaleExportDto> getSales() {
        return sales;
    }

    public void setSales(Set<SaleExportDto> sales) {
        this.sales = sales;
    }
}
