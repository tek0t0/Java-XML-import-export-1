package softuni.xmlprocessing.domain.dtos.exportDtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

@XmlRootElement(name = "cars")
@XmlAccessorType(XmlAccessType.FIELD)
public class CarByToyotaExportRootDto {

    @XmlElement(name = "car")
    private Set<CarByToyotaExportDto> cars;

    public CarByToyotaExportRootDto() {
    }

    public Set<CarByToyotaExportDto> getCars() {
        return cars;
    }

    public void setCars(Set<CarByToyotaExportDto> cars) {
        this.cars = cars;
    }
}
