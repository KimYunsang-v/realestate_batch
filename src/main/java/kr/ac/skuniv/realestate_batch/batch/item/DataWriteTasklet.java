package kr.ac.skuniv.realestate_batch.batch.item;

import com.google.gson.Gson;
import kr.ac.skuniv.realestate_batch.domain.dto.BargainDto;
import kr.ac.skuniv.realestate_batch.domain.dto.CharterAndRentDto;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.BargainItemDto;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.BuildingDealDto;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.CharterAndRentItemDto;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.ItemDto;
import kr.ac.skuniv.realestate_batch.domain.entity.BargainDate;
import kr.ac.skuniv.realestate_batch.domain.entity.BuildingEntity;
import kr.ac.skuniv.realestate_batch.domain.entity.CharterDate;
import kr.ac.skuniv.realestate_batch.domain.entity.RentDate;
import kr.ac.skuniv.realestate_batch.repository.BuildingEntityRepository;
import kr.ac.skuniv.realestate_batch.service.DataWriteService;
import kr.ac.skuniv.realestate_batch.util.OpenApiContents;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.*;
import java.util.*;

@Slf4j
@Service
@StepScope
@RequiredArgsConstructor
@PropertySource("classpath:serviceKey.yaml")
public class DataWriteTasklet implements Tasklet, StepExecutionListener, InitializingBean{

    private static final Gson gson = new Gson();
    private final BuildingEntityRepository buildingEntityRepository;
    private final DataWriteService dataWriteService;
    private List<? extends BuildingDealDto> saveItems;
//    private final GoogleLocationApiService googleLocationApiService;
    @Value("${filePath}")
    private String filePath;
    private String fileName;
    private String dealType;
    private List newDataList;
    private String buildingType;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.warn("beforeStep !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        ExecutionContext ctx = stepExecution.getExecutionContext();
        saveItems = (List<BuildingDealDto>) ctx.get("items");
    }

    @Transactional
    void insertData() {
        for (BuildingDealDto saveItem : saveItems) {
            if (saveItem.getDealType().equals(OpenApiContents.BARGAIN_NUM)) {
                BargainDto bargainDto = (BargainDto) saveItem;
                for (BargainItemDto bargainItemDto : bargainDto.getBody().getItem()) {
                    dataWriteService.setData(bargainItemDto);
                    //BuildingEntity buildingEntity = dataWriteService.getBuildingEntity(bargainItemDto.getBuildingNum(), bargainItemDto.getFloor());
                    BargainDate bargainDate = dataWriteService.buildBargainDate(bargainItemDto);
                    //buildingEntity.getBargainDates().add(bargainDate);
                   // buildingEntityRepository.save(buildingEntity);
                }
            } else {
                CharterAndRentDto charterAndRentDto = (CharterAndRentDto) saveItem;
                for (CharterAndRentItemDto charterAndRentItemDto : charterAndRentDto.getBody().getItem()) {
                    //BuildingEntity buildingEntity = dataWriteService.getBuildingEntity(charterAndRentItemDto.getBuildingNum(), charterAndRentItemDto.getFloor());
                    dataWriteService.setData(charterAndRentItemDto);
                    //월세
                    if (Integer.parseInt(charterAndRentItemDto.getMonthlyPrice().trim()) != 0) {
                       // buildingEntity.getRentDates().add(dataWriteService.buildRentDate(charterAndRentItemDto));
                        //buildingEntityRepository.save(buildingEntity);
                        return;
                    }
                    //buildingEntity.getCharterDates().add(dataWriteService.buildCharterDate(charterAndRentItemDto));
                   //buildingEntityRepository.save(buildingEntity);
                }
                buildingEntityRepository.flush();
            }
        }
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        insertData();
        return RepeatStatus.FINISHED;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}