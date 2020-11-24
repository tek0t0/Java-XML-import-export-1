package softuni.xmlprocessing.domain.dtos.exportDtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

@XmlRootElement(name = "parts")
@XmlAccessorType(XmlAccessType.FIELD)
public class PartExportRootDto {

    @XmlElement(name = "part")
    private Set<PartsExportDto> parts;

    public PartExportRootDto() {
    }

    public Set<PartsExportDto> getParts() {
        return parts;
    }

    public void setParts(Set<PartsExportDto> parts) {
        this.parts = parts;
    }
}
