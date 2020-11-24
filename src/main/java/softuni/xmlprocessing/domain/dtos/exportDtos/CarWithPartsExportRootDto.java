package softuni.xmlprocessing.domain.dtos.exportDtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

@XmlRootElement(name = "cars")
@XmlAccessorType(XmlAccessType.FIELD)
public class CarWithPartsExportRootDto {

    @XmlElement(name = "car")
    private Set<CarWithPartsExportDto> cars;

    public CarWithPartsExportRootDto() {
    }

    public Set<CarWithPartsExportDto> getCars() {
        return cars;
    }

    public void setCars(Set<CarWithPartsExportDto> cars) {
        this.cars = cars;
    }
}
