package softuni.xmlprocessing.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import softuni.xmlprocessing.domain.dtos.exportDtos.CustomerWithSalesExportDto;
import softuni.xmlprocessing.domain.dtos.exportDtos.CustomerOrderedExportDto;
import softuni.xmlprocessing.domain.dtos.exportDtos.CustomerOrderedExportRootDto;
import softuni.xmlprocessing.domain.dtos.exportDtos.CustomersWithSalesRootDto;
import softuni.xmlprocessing.domain.dtos.importDtos.CustomerImporRootDto;
import softuni.xmlprocessing.domain.dtos.importDtos.CustomerImportDto;
import softuni.xmlprocessing.domain.entities.Customer;
import softuni.xmlprocessing.domain.entities.Part;
import softuni.xmlprocessing.domain.entities.Sale;
import softuni.xmlprocessing.domain.repositories.CustomerRepo;
import softuni.xmlprocessing.services.CustomerService;
import softuni.xmlprocessing.utils.XmlParser;

import javax.xml.bind.JAXBException;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class CustomerServiceImpl implements CustomerService {
    private final static String CUSTOMERS_PATH = "src/main/resources/xmls/customers.xml";
    private final static String CUSTOMERS_ORDERED_PATH = "src/main/resources/xmls/outputs/ordered-customers.xml";
    private final static String CUSTOMERS_SALES_PATH = "src/main/resources/xmls/outputs/customers-total-sales.xml";
    private final CustomerRepo customerRepo;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;


    @Autowired
    public CustomerServiceImpl(CustomerRepo customerRepo, ModelMapper modelMapper, XmlParser xmlParser) {
        this.customerRepo = customerRepo;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }

    @Override
    public void seedCustomers() throws JAXBException {
        if (this.customerRepo.findAll().size() == 0) {
            CustomerImporRootDto customerImporRootDto = this.xmlParser.parseXml(CustomerImporRootDto.class, CUSTOMERS_PATH);
            for (CustomerImportDto customer : customerImporRootDto.getCustomers()) {
                this.customerRepo.saveAndFlush(this.modelMapper.map(customer, Customer.class));
            }
        }
    }

    @Override
    public String exportOrderedCustomers() throws JAXBException {
        StringBuilder sb = new StringBuilder();
        List<CustomerOrderedExportDto> dtos = this.customerRepo
                .getAllOrderByBirthDateAndYoungDriver()
                .stream()
                .map(c -> this.modelMapper.map(c, CustomerOrderedExportDto.class)).collect(Collectors.toList());
        CustomerOrderedExportRootDto rootDto = new CustomerOrderedExportRootDto();
        rootDto.setCustomers(dtos);
        try {
            File file = new File(CUSTOMERS_ORDERED_PATH);
            if (file.createNewFile()) {
                this.xmlParser.exportXml(rootDto, CustomerOrderedExportRootDto.class, CUSTOMERS_ORDERED_PATH);
                sb.append("File created: ")
                        .append(file.getName())
                        .append(System.lineSeparator())
                        .append("Customers Exported!").append(System.lineSeparator());
            } else {
                this.xmlParser.exportXml(rootDto, CustomerOrderedExportRootDto.class, CUSTOMERS_ORDERED_PATH);
                sb.append("File already exists.").append(System.lineSeparator())
                        .append("Customers Exported!").append(System.lineSeparator());
            }
        } catch (IOException e) {
            sb.append("An error occurred.").append(System.lineSeparator());
        }
        return sb.toString();
    }

    @Override
    public String getAllCustomersByTotalSales() {
        StringBuilder sb = new StringBuilder();
        Set<Customer> customersByCars = this.customerRepo.findCustomersByCars();
        Set<CustomerWithSalesExportDto> customersDto = new LinkedHashSet<>();
        for (Customer customerByCar : customersByCars) {
            CustomerWithSalesExportDto exportDto = this.modelMapper.map(customerByCar, CustomerWithSalesExportDto.class);
            exportDto.setBoughtCars(customerByCar.getSales().size());
            BigDecimal spentMoney = new BigDecimal("0");
            Set<Sale> sales = customerByCar.getSales();
            for (Sale sale : sales) {
                Set<Part> parts = sale.getCar().getParts();
                for (Part part : parts) {
                    spentMoney = spentMoney.add(part.getPrice());
                }
            }
            exportDto.setSpentMoney(spentMoney);
            customersDto.add(exportDto);
        }
        CustomersWithSalesRootDto customersWithSalesRootDto = new CustomersWithSalesRootDto();
        customersWithSalesRootDto.setCustomers(customersDto);
        try {
            File file = new File(CUSTOMERS_SALES_PATH);
            if (file.createNewFile()) {
                this.xmlParser.exportXml(customersWithSalesRootDto, CustomersWithSalesRootDto.class, CUSTOMERS_SALES_PATH);
                sb.append("File created: ")
                        .append(file.getName())
                        .append(System.lineSeparator())
                        .append("Customers with Sales Exported!").append(System.lineSeparator());
            } else {
                this.xmlParser.exportXml(customersWithSalesRootDto, CustomersWithSalesRootDto.class, CUSTOMERS_SALES_PATH);
                sb.append("File already exists.").append(System.lineSeparator())
                        .append("Customers with Sales Exported!").append(System.lineSeparator());
            }
        } catch (IOException | JAXBException e) {
            sb.append("An error occurred.").append(System.lineSeparator());
        }

        return sb.toString();
    }
}
