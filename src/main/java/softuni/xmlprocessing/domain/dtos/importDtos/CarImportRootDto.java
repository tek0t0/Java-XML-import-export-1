package softuni.xmlprocessing.domain.dtos.importDtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

@XmlRootElement(name = "cars")
@XmlAccessorType(XmlAccessType.FIELD)
public class CarImportRootDto {

    @XmlElement(name = "car")
    private Set<CarImportDto> cars;

    public CarImportRootDto() {
    }

    public Set<CarImportDto> getCars() {
        return cars;
    }

    public void setCars(Set<CarImportDto> cars) {
        this.cars = cars;
    }
}
