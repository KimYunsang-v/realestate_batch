package kr.ac.skuniv.realestate_batch.batch.item;

import com.google.gson.Gson;
import kr.ac.skuniv.realestate_batch.domain.dto.BargainDto;
import kr.ac.skuniv.realestate_batch.domain.dto.CharterAndRentDto;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.BargainItemDto;
import kr.ac.skuniv.realestate_batch.domain.dto.abstractDto.BuildingDealDto;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.CharterAndRentItemDto;
import kr.ac.skuniv.realestate_batch.domain.entity.BuildingEntity;
import kr.ac.skuniv.realestate_batch.repository.BuildingEntityRepository;
import kr.ac.skuniv.realestate_batch.service.DataWriteService;
import kr.ac.skuniv.realestate_batch.util.OpenApiContents;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@StepScope
@Configuration
@RequiredArgsConstructor
@PropertySource("classpath:serviceKey.yaml")
public class RealEstateItemWriter implements ItemWriter<List<BuildingDealDto>>, StepExecutionListener, InitializingBean {

    private static final Gson gson = new Gson();

    private String fileName;
    @Value("${filePath}")
    private String filePath;
    private BufferedWriter bufferedWriter;
    private String dealType;
    private String buildingType;
    private BuildingEntity buildingEntity;
    private List<? extends List<BuildingDealDto>> saveItems;
    private List<BuildingEntity> buildingEntities = new ArrayList<>();

    private final DataWriteService dataWriteService;
    private final BuildingEntityRepository buildingEntityRepository;

    @Transactional
    public void saveBuilding(BuildingDealDto item) {
        long start = System.currentTimeMillis();
        if (item.getDealType().equals(OpenApiContents.BARGAIN_NUM)){
            BargainDto bargainDto = (BargainDto) item;
            for (BargainItemDto bargainItemDto : bargainDto.getBody().getItem()) {
                buildingEntities.add(dataWriteService.addNewBargainDate(bargainItemDto, buildingType));
            }
        } else {
            CharterAndRentDto charterAndRentDto = (CharterAndRentDto) item;
            for (CharterAndRentItemDto charterAndRentItemDto : charterAndRentDto.getBody().getItem()) {
                buildingEntities.add(dataWriteService.addNewCharterDateOrRentDate(charterAndRentItemDto, buildingType));
            }
        }

        long end = System.currentTimeMillis();

//        log.warn("1개 building save" + (end-start)/1000 +" 초 걸림");
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("before step");
        ExecutionContext ctx = stepExecution.getExecutionContext();
        dealType = (String) ctx.get(OpenApiContents.DEAL_TYPE);
        buildingType = (String) ctx.get(OpenApiContents.BUILDING_TYPE);
        fileName = (String) ctx.get(OpenApiContents.API_KIND);
    }

    @Override
    public void write(List<? extends List<BuildingDealDto>> items) throws Exception {
        saveItems = items;
        long start = System.currentTimeMillis();

        // for (BuildingDealDto item : items) {
        //     saveBuilding(item);
        // }
        // log.warn("deal type = " + dealType + "  building type = " + buildingType + "entity count = " + buildingEntities.size());
        // buildingEntityRepository.saveAll(buildingEntities);
        // buildingEntityRepository.flush();
        // buildingEntities.clear();

        long end = System.currentTimeMillis();

        log.warn("1개 url save  " + (end-start)/1000 +" 초 걸림");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("after step");
        return ExitStatus.COMPLETED;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
