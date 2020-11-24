package softuni.xmlprocessing.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import softuni.xmlprocessing.domain.dtos.importDtos.PartImportDto;
import softuni.xmlprocessing.domain.dtos.importDtos.PartImportRootDto;
import softuni.xmlprocessing.domain.entities.Part;
import softuni.xmlprocessing.domain.entities.Supplier;
import softuni.xmlprocessing.domain.repositories.PartRepo;
import softuni.xmlprocessing.domain.repositories.SupplierRepo;
import softuni.xmlprocessing.services.PartService;
import softuni.xmlprocessing.utils.XmlParser;

import java.util.Optional;
import java.util.Random;


@Service
public class PartServiceImpl implements PartService {
    private final static String PARTS_PATH = "src/main/resources/xmls/parts.xml";
    private final PartRepo partRepo;
    private final SupplierRepo supplierRepo;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;

    @Autowired
    public PartServiceImpl(PartRepo partRepo, SupplierRepo supplierRepo, ModelMapper modelMapper, XmlParser xmlParser) {
        this.partRepo = partRepo;
        this.supplierRepo = supplierRepo;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }

    @Override
    public void seedParts() throws Exception {
        if (this.partRepo.findAll().size() == 0) {
            PartImportRootDto partImportRootDto = this.xmlParser.parseXml(PartImportRootDto.class, PARTS_PATH);
            for (PartImportDto partDto : partImportRootDto.getParts()) {
                Part part = this.modelMapper.map(partDto, Part.class);
                part.setSupplier(this.getRandomSupplier());
                this.partRepo.saveAndFlush(part);
            }

        }
    }

    private Supplier getRandomSupplier() throws Exception {
        Random random = new Random();
        long index = (long) random.nextInt((int) this.supplierRepo.count()) + 1;
        Optional<Supplier> supplier = this.supplierRepo.findById(index);
        if (supplier.isPresent()) {
            return supplier.get();
        } else {
            throw new Exception("Supplier dont exists");
        }

    }
}
