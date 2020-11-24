package softuni.xmlprocessing.services.impl;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.xmlprocessing.domain.dtos.exportDtos.CarExportDto;
import softuni.xmlprocessing.domain.dtos.exportDtos.CarWithPartsExportRootDto;
import softuni.xmlprocessing.domain.dtos.exportDtos.SaleExportDto;
import softuni.xmlprocessing.domain.dtos.exportDtos.SaleExportRootDto;
import softuni.xmlprocessing.domain.entities.Car;
import softuni.xmlprocessing.domain.entities.Customer;
import softuni.xmlprocessing.domain.entities.Part;
import softuni.xmlprocessing.domain.entities.Sale;
import softuni.xmlprocessing.domain.repositories.CarRepo;
import softuni.xmlprocessing.domain.repositories.CustomerRepo;
import softuni.xmlprocessing.domain.repositories.SalesRepo;
import softuni.xmlprocessing.services.SaleService;
import softuni.xmlprocessing.utils.XmlParser;


import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class SalesServiceImpl implements SaleService {
    private static final String SALES_EXPORT_PATH = "src/main/resources/xmls/outputs/sales-discounts.xml";
    private final SalesRepo salesRepo;
    private final CarRepo carRepo;
    private final CustomerRepo customerRepo;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;

    @Autowired
    public SalesServiceImpl(SalesRepo salesRepo, CarRepo carRepo, CustomerRepo customerRepo, ModelMapper modelMapper, XmlParser xmlParser) {
        this.salesRepo = salesRepo;
        this.carRepo = carRepo;
        this.customerRepo = customerRepo;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }

    @Override
    public void seedSales() {
        if (this.salesRepo.findAll().size() > 0) {
        } else {
            Sale sale = new Sale();
            sale.setCar(getRandomCar());
            sale.setCustomer(getRandomCustomer());
            sale.setDiscount(getRandomDiscount());

            Sale sale1 = new Sale();
            sale1.setCar(getRandomCar());
            sale1.setCustomer(getRandomCustomer());
            sale1.setDiscount(getRandomDiscount());

            Sale sale2 = new Sale();
            sale2.setCar(getRandomCar());
            sale2.setCustomer(getRandomCustomer());
            sale2.setDiscount(getRandomDiscount());

            this.salesRepo.saveAndFlush(sale);
            this.salesRepo.saveAndFlush(sale1);
            this.salesRepo.saveAndFlush(sale2);
        }
    }

    @Override
    public String getAllSales() {
        StringBuilder sb = new StringBuilder();
        Set<Sale> sales = this.salesRepo.getAllBy();
        Set<SaleExportDto> saleExportDtoSet = new LinkedHashSet<>();

        for (Sale sale : sales) {
            SaleExportDto saleExportDto = new SaleExportDto();
            Car car = sale.getCar();
            CarExportDto carExportDto = this.modelMapper.map(car, CarExportDto.class);
            saleExportDto.setCar(carExportDto);
            saleExportDto.setCustomer(sale.getCustomer().getName());
            saleExportDto.setDiscount((double) sale.getDiscount() / 100);

            BigDecimal totalPrice = new BigDecimal("0");
            Set<Part> parts = car.getParts();
            for (Part part : parts) {
                totalPrice = totalPrice.add(part.getPrice());
            }
            saleExportDto.setPrice(totalPrice);
            double v = 1 - ((double) sale.getDiscount() / 100);
            BigDecimal finalPrice = totalPrice.multiply(new BigDecimal(v)).setScale(2, RoundingMode.HALF_UP);
            saleExportDto.setPriceWithDiscount(finalPrice);
            saleExportDtoSet.add(saleExportDto);
        }
        SaleExportRootDto saleExportRootDto = new SaleExportRootDto();
        saleExportRootDto.setSales(saleExportDtoSet);

        try {
            File file = new File(SALES_EXPORT_PATH);
            if (file.createNewFile()) {
                this.xmlParser.exportXml(saleExportRootDto, SaleExportRootDto.class, SALES_EXPORT_PATH);
                sb.append("File created: ")
                        .append(file.getName())
                        .append(System.lineSeparator())
                        .append("Sales Exported!").append(System.lineSeparator());
            } else {
                sb.append("File already exists.").append(System.lineSeparator())
                        .append("Sales Exported!").append(System.lineSeparator());
                this.xmlParser.exportXml(saleExportRootDto, SaleExportRootDto.class, SALES_EXPORT_PATH);
            }
        } catch (IOException | JAXBException e) {
            sb.append("An error occurred.").append(System.lineSeparator());
        }
        return sb.toString();
    }

    private int getRandomDiscount() {
        List<Integer> discounts = Arrays.asList(0, 5, 10, 15, 20, 30, 40, 50);
        Random random = new Random();
        return discounts.get(random.nextInt(discounts.size()));
    }

    private Customer getRandomCustomer() {
        Random random = new Random();
        long id = (long) random.nextInt((int) this.customerRepo.count()) + 1;
        return this.customerRepo.findById(id).get();
    }

    private Car getRandomCar() {
        Random random = new Random();
        long id = (long) random.nextInt((int) this.carRepo.count()) + 1;
        return this.carRepo.findById(id).get();
    }


}
