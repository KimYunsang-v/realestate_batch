package kr.ac.skuniv.realestate_batch.batch.item;

import com.google.gson.Gson;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.BargainItemDto;
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
    private final BuildingEntityRepository buildingRepository;
    private final DataWriteService dataWriteService;
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
        fileName = (String) ctx.get(OpenApiContents.API_KIND);
        log.warn("fileName => {}", fileName);
        dealType = (String) ctx.get(OpenApiContents.DEAL_TYPE);
        log.warn("dealType => {}", dealType);
        buildingType = (String) ctx.get(OpenApiContents.BUILDING_TYPE);
        log.warn("buildingType => {}", buildingType);

        newDataList = new ArrayList<CharterAndRentItemDto>();
        if (dealType.equals(OpenApiContents.BARGAIN_NUM)) {
            newDataList = new ArrayList<BargainItemDto>();
        }

        String newFileFullPath = filePath + OpenApiContents.FILE_DELEMETER_WINDOWS + fileName;
        try (
                BufferedReader newBr = new BufferedReader(new FileReader(new File(newFileFullPath)))
        ) {
            loadDataList(newBr, newDataList);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDataList(BufferedReader br, List list) throws IOException {
        String line = null;
        if (dealType.equals(OpenApiContents.BARGAIN_NUM)) {
            while ((line = br.readLine()) != null) {
                list.add(gson.fromJson(line.trim(), BargainItemDto.class));
            }
            return;
        }
        while ((line = br.readLine()) != null) {
            //CharterAndRentItemDto charterWithRentItemDto = gson.fromJson(line.trim(), CharterAndRentItemDto.class);
            list.add(gson.fromJson(line.trim(), CharterAndRentItemDto.class));
        }
    }

    @Transactional
    void insertData(List newDataList) {

        newDataList.forEach(item -> {
            log.warn("insert item => {}", item.toString());
            ItemDto itemDto = (ItemDto) item;
            dataWriteService.setData(itemDto);
            BuildingEntity buildingEntity = dataWriteService.buildBuildingEntity(itemDto);

            // 매매일 경우
            if(dealType.equals(OpenApiContents.BARGAIN_NUM)){
                log.warn("매매!!");
                BargainItemDto bargainItemDto = (BargainItemDto) item;

                BargainDate bargainDate = dataWriteService.buildBargainDate(bargainItemDto);

                buildingEntity.getBargainDates().add(bargainDate);

                log.warn("insert building trade info => {}", buildingEntity.toString());
                buildingRepository.save(buildingEntity);
                return;
            }
            // 전월세의 경우
            CharterAndRentItemDto charterWithRentItemDto = (CharterAndRentItemDto) item;

            log.warn("building create => {}", buildingEntity.toString());
            if (Integer.parseInt(charterWithRentItemDto.getMonthlyPrice().trim()) != 0) {
                log.warn("월세!!!");
                // 월세
                RentDate rentDate = dataWriteService.buildRentDate(charterWithRentItemDto);
                buildingEntity.getRentDates().add(rentDate);
                log.warn("insert building trade info => {}", buildingEntity.toString());
                buildingRepository.save(buildingEntity);
                return;
            }
            // 전세
            log.warn("전세!!!");
            CharterDate charterDate = dataWriteService.buildCharterDate(charterWithRentItemDto);
            buildingEntity.getCharterDates().add(charterDate);
            log.warn("insert building trade info => {}", buildingEntity.toString());
            buildingRepository.save(buildingEntity);
        });

        buildingRepository.flush();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        insertData(newDataList);
        return RepeatStatus.FINISHED;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}