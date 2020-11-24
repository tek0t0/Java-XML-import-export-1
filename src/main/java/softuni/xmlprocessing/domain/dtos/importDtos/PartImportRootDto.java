package softuni.xmlprocessing.domain.dtos.importDtos;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

@XmlRootElement(name = "parts")
@XmlAccessorType(XmlAccessType.FIELD)
public class PartImportRootDto {

    @XmlElement(name = "part")
    private Set<PartImportDto> parts;

    public PartImportRootDto() {
    }

    public Set<PartImportDto> getParts() {
        return parts;
    }

    public void setParts(Set<PartImportDto> parts) {
        this.parts = parts;
    }
}
