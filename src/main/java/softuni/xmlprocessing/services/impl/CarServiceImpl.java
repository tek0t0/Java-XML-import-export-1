package softuni.xmlprocessing.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import softuni.xmlprocessing.domain.dtos.exportDtos.*;
import softuni.xmlprocessing.domain.dtos.importDtos.CarImportDto;
import softuni.xmlprocessing.domain.dtos.importDtos.CarImportRootDto;

import softuni.xmlprocessing.domain.entities.Car;
import softuni.xmlprocessing.domain.entities.Part;
import softuni.xmlprocessing.domain.repositories.CarRepo;
import softuni.xmlprocessing.domain.repositories.PartRepo;
import softuni.xmlprocessing.services.CarService;
import softuni.xmlprocessing.utils.XmlParser;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;

import java.io.File;
import java.io.IOException;
import java.util.*;


@Service
public class CarServiceImpl implements CarService {
    private final static String CARS_PATH = "src/main/resources/xmls/cars.xml";
    private final static String CARS_TOYOTAS_PATH = "src/main/resources/xmls/outputs/toyota-cars.xml";
    private final static String CARS_WITH_PARTS_PATH = "src/main/resources/xmls/outputs/cars-and-parts.xml";
    private final CarRepo carRepo;
    private final PartRepo partRepo;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;

    @Autowired
    public CarServiceImpl(CarRepo carRepo, PartRepo partRepo, ModelMapper modelMapper, XmlParser xmlParser) {
        this.carRepo = carRepo;
        this.partRepo = partRepo;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }

    @Transactional
    @Override
    public void seedCars() throws Exception {
        if (this.carRepo.findAll().size() == 0) {
            CarImportRootDto carImportRootDto = this.xmlParser.parseXml(CarImportRootDto.class, CARS_PATH);
            for (CarImportDto carDto : carImportRootDto.getCars()) {
                Car car = this.modelMapper.map(carDto, Car.class);
                car.setParts(this.getRandomParts());
                this.carRepo.saveAndFlush(car);
            }
        }
    }

    @Override
    public String findByToyota() {
        StringBuilder sb = new StringBuilder();
        Set<CarByToyotaExportDto> toExport = new LinkedHashSet<>();
        Set<Car> toyotas = this.carRepo.findAllByMakeOrderByModelAscTravelledDistanceDesc("Toyota");
        for (Car toyota : toyotas) {
            CarByToyotaExportDto car = this.modelMapper.map(toyota, CarByToyotaExportDto.class);

            toExport.add(car);
        }
        CarByToyotaExportRootDto rootDto = new CarByToyotaExportRootDto();
        rootDto.setCars(toExport);

        try {
            File file = new File(CARS_TOYOTAS_PATH);
            if (file.createNewFile()) {
                this.xmlParser.exportXml(rootDto, CarByToyotaExportRootDto.class, CARS_TOYOTAS_PATH);
                sb.append("File created: ")
                        .append(file.getName())
                        .append(System.lineSeparator())
                        .append("Cars Exported!").append(System.lineSeparator());
            } else {
                this.xmlParser.exportXml(rootDto, CarByToyotaExportRootDto.class, CARS_TOYOTAS_PATH);
                sb.append("File already exists.").append(System.lineSeparator())
                        .append("Cars Exported!").append(System.lineSeparator());
            }
        } catch (IOException | JAXBException e) {
            sb.append("An error occurred.").append(System.lineSeparator());
        }
        return sb.toString();
    }

    @Override
    public String getCarsWithParts() {
        StringBuilder sb = new StringBuilder();
        List<Car> cars = this.carRepo.findAll();
        Set<CarWithPartsExportDto> carsDtos = new LinkedHashSet<>();
        for (Car car : cars) {
            CarWithPartsExportDto carWithPartsExportDto = this.modelMapper.map(car, CarWithPartsExportDto.class);
            Set<Part> parts = car.getParts();
            Set<PartsExportDto> partsExportDtoSet = new LinkedHashSet<>();
            for (Part part : parts) {
                PartsExportDto partsExportDto = this.modelMapper.map(part, PartsExportDto.class);
                partsExportDtoSet.add(partsExportDto);
            }
            PartExportRootDto partExportRootDto = new PartExportRootDto();
            partExportRootDto.setParts(partsExportDtoSet);
            carWithPartsExportDto.setParts(partExportRootDto);
            carsDtos.add(carWithPartsExportDto);
        }
        CarWithPartsExportRootDto carWithPartsExportRootDto = new CarWithPartsExportRootDto();
        carWithPartsExportRootDto.setCars(carsDtos);

        try {
            File file = new File(CARS_WITH_PARTS_PATH);
            if (file.createNewFile()) {
                this.xmlParser.exportXml(carWithPartsExportRootDto, CarWithPartsExportRootDto.class, CARS_WITH_PARTS_PATH);
                sb.append("File created: ")
                        .append(file.getName())
                        .append(System.lineSeparator())
                        .append("Cars and parts Exported!").append(System.lineSeparator());
            } else {
                this.xmlParser.exportXml(carWithPartsExportRootDto, CarWithPartsExportRootDto.class, CARS_WITH_PARTS_PATH);
                sb.append("File already exists.").append(System.lineSeparator()).
                        append("Cars and parts Exported!").append(System.lineSeparator());
            }
        } catch (IOException | JAXBException e) {
            sb.append("An error occurred.").append(System.lineSeparator());
        }
        return sb.toString();
    }

    private Set<Part> getRandomParts() throws Exception {
        Set<Part> parts = new HashSet<>();
        int num = getRandomPartCount();
        for (int i = 10; i < num; i++) {
            Part part = this.getRandomPart();
            parts.add(part);
        }
        return parts;
    }

    private Part getRandomPart() throws Exception {
        Random random = new Random();
        long index = (long) random.nextInt((int) this.partRepo.count()) + 1;
        Optional<Part> part = this.partRepo.findById(index);
        if (part.isPresent()) {
            return part.get();
        } else {
            throw new Exception("Part dont exists");
        }
    }

    private int getRandomPartCount() {
        Random random = new Random();
        return random.nextInt(10) + 11;
    }
}
