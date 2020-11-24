package softuni.xmlprocessing.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import softuni.xmlprocessing.domain.dtos.exportDtos.LocalSuppliersExportDto;
import softuni.xmlprocessing.domain.dtos.exportDtos.LocalSuppliersExportRootDto;
import softuni.xmlprocessing.domain.dtos.importDtos.SupplierImportDto;
import softuni.xmlprocessing.domain.dtos.importDtos.SupplierImportRootDto;
import softuni.xmlprocessing.domain.entities.Supplier;
import softuni.xmlprocessing.domain.repositories.SupplierRepo;
import softuni.xmlprocessing.services.SupplierService;
import softuni.xmlprocessing.utils.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class SupplierServiceImpl implements SupplierService {
    private final static String SUPPLIERS_PATH = "src/main/resources/xmls/suppliers.xml";
    private final static String LOCAL_SUPPLIERS_PATH = "src/main/resources/xmls/outputs/local-suppliers.xml";
    private final SupplierRepo supplierRepo;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;

    @Autowired
    public SupplierServiceImpl(SupplierRepo supplierRepo, ModelMapper modelMapper, XmlParser xmlParser) {
        this.supplierRepo = supplierRepo;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }

    @Override
    public void seedSuppliers() throws JAXBException {
        if (supplierRepo.findAll().size() == 0) {
            SupplierImportRootDto supplierImportRootDto = this.xmlParser.parseXml(SupplierImportRootDto.class, SUPPLIERS_PATH);
            for (SupplierImportDto supplier : supplierImportRootDto.getSuppliers()) {
                this.supplierRepo.saveAndFlush(this.modelMapper.map(supplier, Supplier.class));
            }
        }

    }

    @Override
    public String getAllLocalSuppliers() {
        StringBuilder sb = new StringBuilder();
        Set<Supplier> allByImporterIsFalseOrderById = this.supplierRepo.findAllByImporterIsFalseOrderById();
        Set<LocalSuppliersExportDto> localSupplierDtos = new LinkedHashSet<>();
        for (Supplier supplier : allByImporterIsFalseOrderById) {
            LocalSuppliersExportDto supplierExport = this.modelMapper.map(supplier, LocalSuppliersExportDto.class);
            supplierExport.setPartsCount(supplier.getParts().size());
            localSupplierDtos.add(supplierExport);
        }
        LocalSuppliersExportRootDto localSuppliersExportRootDto = new LocalSuppliersExportRootDto();
        localSuppliersExportRootDto.setSuppliers(localSupplierDtos);

        try {
            File file = new File(LOCAL_SUPPLIERS_PATH);
            if (file.createNewFile()) {
                this.xmlParser
                        .exportXml(localSuppliersExportRootDto, LocalSuppliersExportRootDto.class, LOCAL_SUPPLIERS_PATH);
                sb.append("File created: ")
                        .append(file.getName())
                        .append(System.lineSeparator())
                        .append("Local Suppliers Exported!").append(System.lineSeparator());
            } else {
                this.xmlParser
                        .exportXml(localSuppliersExportRootDto, LocalSuppliersExportRootDto.class, LOCAL_SUPPLIERS_PATH);
                sb.append("File already exists.").append(System.lineSeparator())
                        .append("Local Suppliers Exported!").append(System.lineSeparator());
            }
        } catch (IOException | JAXBException e) {
            sb.append("An error occurred.").append(System.lineSeparator());
        }
        return sb.toString();
    }
}
