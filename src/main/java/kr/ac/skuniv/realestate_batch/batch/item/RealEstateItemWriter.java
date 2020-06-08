package kr.ac.skuniv.realestate_batch.batch.item;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import kr.ac.skuniv.realestate_batch.domain.dto.RentDto;
import kr.ac.skuniv.realestate_batch.domain.dto.SaleDto;
import kr.ac.skuniv.realestate_batch.domain.dto.abstractDto.BuildingDealDto;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.RentItemDto;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.SaleItemDto;
import kr.ac.skuniv.realestate_batch.domain.entity.Rent;
import kr.ac.skuniv.realestate_batch.domain.entity.Sale;
import kr.ac.skuniv.realestate_batch.repository.RentRepository;
import kr.ac.skuniv.realestate_batch.repository.SaleRepository;
import kr.ac.skuniv.realestate_batch.service.DataWriteService;
import kr.ac.skuniv.realestate_batch.util.OpenApiContents;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@StepScope
@Configuration
@RequiredArgsConstructor
@PropertySource("classpath:serviceKey.yaml")
public class RealEstateItemWriter implements ItemWriter<BuildingDealDto>, StepExecutionListener {

    private List<Sale> sales = new ArrayList<>();
    private List<Rent> rents = new ArrayList<>();

    private final DataWriteService dataWriteService;

    private final RentRepository rentRepository;
    private final SaleRepository saleRepository;

    public void getEntity(BuildingDealDto item) {
        if(item.getDealType().equals(OpenApiContents.BARGAIN_NUM)){
            SaleDto saleDto  = (SaleDto) item;
            for (SaleItemDto saleItemDto : saleDto.getBody().getItem()) {
                Sale sale = dataWriteService.createSaleEntity(saleItemDto);
                sale.setType(item.getBuildingType());
                sales.add(sale);
            }
        } else {
            RentDto rentDto = (RentDto) item;
            for (RentItemDto rentItemDto : rentDto.getBody().getItem()) {
                Rent rent = dataWriteService.createRentEntity(rentItemDto);
                rent.setType(item.getBuildingType());
                rents.add(rent);
            }
        }
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
    }

    @Override
    public void write(List<? extends BuildingDealDto> items) {
        long start = System.currentTimeMillis();

        items.stream().forEach(this::getEntity);

        log.info("writer after step  " + sales.size());
        log.info("writer after step  " + rents.size());
        saleRepository.saveAll(sales);
        rentRepository.saveAll(rents);

        long end = System.currentTimeMillis();
        log.warn("1개 url save  " + (end-start)/1000 +" 초 걸림");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }
}
