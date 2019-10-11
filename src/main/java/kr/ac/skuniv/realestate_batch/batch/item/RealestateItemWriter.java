package kr.ac.skuniv.realestate_batch.batch.item;

import com.google.gson.Gson;
import kr.ac.skuniv.realestate_batch.domain.dto.BargainDto;
import kr.ac.skuniv.realestate_batch.domain.dto.CharterAndRentDto;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.BargainItemDto;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.BuildingDealDto;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.CharterAndRentItemDto;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.ItemDto;
import kr.ac.skuniv.realestate_batch.domain.entity.BuildingEntity;
import kr.ac.skuniv.realestate_batch.repository.BargainDateRepository;
import kr.ac.skuniv.realestate_batch.repository.BuildingEntityRepository;
import kr.ac.skuniv.realestate_batch.repository.CharterDateRepository;
import kr.ac.skuniv.realestate_batch.repository.RentDateRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@StepScope
@Configuration
@RequiredArgsConstructor
@PropertySource("classpath:serviceKey.yaml")
public class RealestateItemWriter implements ItemWriter<BuildingDealDto>, StepExecutionListener, InitializingBean {

    private static final Gson gson = new Gson();

    private String fileName;
    @Value("${filePath}")
    private String filePath;
    private BufferedWriter bufferedWriter;
    private String dealType;
    private String buildingType;
    private BuildingEntity buildingEntity;
    private List<? extends BuildingDealDto> saveItems;
    private List<BuildingEntity> buildingEntities = new ArrayList<>();

    @Autowired
    private DataWriteService dataWriteService;
    @Autowired
    private BuildingEntityRepository buildingEntityRepository;
    @Autowired
    private CharterDateRepository charterDateRepository;

    @Autowired
    private BargainDateRepository bargainDateRepository;
    @Autowired
    private RentDateRepository rentDateRepository;

    int city, groop;

    @Transactional
    public void saveBuilding(BuildingDealDto item) {

        if (item.getDealType().equals(OpenApiContents.BARGAIN_NUM)){
            BargainDto bargainDto = (BargainDto) item;
            for (BargainItemDto bargainItemDto : bargainDto.getBody().getItem()){
                dataWriteService.setData(bargainItemDto);
                buildingEntity = dataWriteService.buildBuildingEntity(bargainItemDto);
                buildingEntity.getBargainDates().add(dataWriteService.buildBargainDate(bargainItemDto));
                buildingEntities.add(buildingEntity);
            }
        } else {
            CharterAndRentDto charterAndRentDto = (CharterAndRentDto) item;
            for (CharterAndRentItemDto charterAndRentItemDto : charterAndRentDto.getBody().getItem()) {
                dataWriteService.setData(charterAndRentItemDto);
                buildingEntity = dataWriteService.buildBuildingEntity(charterAndRentItemDto);
                if (Integer.parseInt(charterAndRentItemDto.getMonthlyPrice().trim()) != 0) {
                    buildingEntity.getRentDates().add(dataWriteService.buildRentDate(charterAndRentItemDto));
                    buildingEntities.add(buildingEntity);
                    return;
                }
                buildingEntity.getCharterDates().add(dataWriteService.buildCharterDate(charterAndRentItemDto));
                buildingEntities.add(buildingEntity);
            }
        }
        buildingEntityRepository.saveAll(buildingEntities);
        buildingEntityRepository.flush();
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        ExecutionContext ctx = stepExecution.getExecutionContext();
        dealType = (String) ctx.get(OpenApiContents.DEAL_TYPE);
        buildingType = (String) ctx.get(OpenApiContents.BUILDING_TYPE);

        log.warn("deal type = " + dealType + "  building type = " + buildingType);
        fileName = (String) ctx.get(OpenApiContents.API_KIND);
        dataWriteService.setBuildingType(buildingType);
    }

    @Override
    public void write(List<? extends BuildingDealDto> items) throws Exception {
        saveItems = items;
        for (BuildingDealDto item : items){
            saveBuilding(item);
        }
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
