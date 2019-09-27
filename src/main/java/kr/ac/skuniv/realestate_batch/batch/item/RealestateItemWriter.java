package kr.ac.skuniv.realestate_batch.batch.item;

import com.google.gson.Gson;
import kr.ac.skuniv.realestate_batch.domain.dto.BargainDto;
import kr.ac.skuniv.realestate_batch.domain.dto.CharterAndRentDto;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.BargainItemDto;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.BuildingDealDto;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.CharterAndRentItemDto;
import kr.ac.skuniv.realestate_batch.domain.entity.BargainDate;
import kr.ac.skuniv.realestate_batch.domain.entity.Building;
import kr.ac.skuniv.realestate_batch.domain.entity.CharterDate;
import kr.ac.skuniv.realestate_batch.domain.entity.RentDate;
import kr.ac.skuniv.realestate_batch.repository.BuildingRepository;
import kr.ac.skuniv.realestate_batch.util.OpenApiContents;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.transaction.Transactional;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;

@Slf4j
@StepScope
@Configuration
@RequiredArgsConstructor
@PropertySource("classpath:serviceKey.yaml")
public class RealestateItemWriter implements ItemWriter<BuildingDealDto>, StepExecutionListener {

    private static final Gson gson = new Gson();

    private String fileName;
    @Value("${filePath}")
    private String filePath;
    private BufferedWriter bufferedWriter;
    private String dealType;
    private String buildingType;

    private final BuildingRepository buildingRepository;

    int city, groop;

    private void write(BufferedWriter bw, String content) throws IOException {
        bw.write(content);
        bw.newLine();
    }

    private void divisionItem(BuildingDealDto item, BufferedWriter bw) throws IOException {
        if (item.getDealType().equals(OpenApiContents.BARGAIN_NUM)){
            BargainDto bargainDto = (BargainDto) item;
            for (BargainItemDto bargainItemDto : bargainDto.getBody().getItem()){
                write(bw, gson.toJson(bargainItemDto));
            }
        } else {
            CharterAndRentDto charterAndRentDto = (CharterAndRentDto) item;
            for (CharterAndRentItemDto charterAndRentItemDto : charterAndRentDto.getBody().getItem()) {
                write(bw, gson.toJson(charterAndRentItemDto));
            }
        }
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        ExecutionContext ctx = stepExecution.getExecutionContext();
        dealType = (String) ctx.get(OpenApiContents.DEAL_TYPE);
        buildingType = (String) ctx.get(OpenApiContents.BUILDING_TYPE);

        fileName = (String) ctx.get(OpenApiContents.API_KIND);
        String fileFullPath = filePath + OpenApiContents.FILE_DELEMETER_WINDOWS + fileName;
        File f = new File(fileFullPath);
        log.warn("파일 이름 ====== " + f.getAbsolutePath());
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(fileFullPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(List<? extends BuildingDealDto> items) throws Exception {
        for (BuildingDealDto item : items){
            log.warn("item =======  " + item.toString());
            //divisionItem(item, bufferedWriter);
        }
    }

    @Transactional
    void insertData(List newDataList) {

        newDataList.forEach(item -> {
            log.warn("insert item => {}", item.toString());

            if(dealType.equals(OpenApiContents.BARGAIN_NUM)){
                log.warn("매매!!");
                BargainItemDto bargainItemDto = (BargainItemDto) item;
                city = Integer.parseInt(bargainItemDto.getRegionCode().substring(0,2));
                groop = Integer.parseInt(bargainItemDto.getRegionCode().substring(2));

                Building building = buildingBuilder(bargainItemDto);

                String address = bargainItemDto.getDong() + bargainItemDto.getName();
                /*GoogleLocationDto googleLocationDto = googleLocationApiService.googleLocationApiCall(address.replaceAll(" ",""));
                if (googleLocationDto != null){
                    building.setLatitude(googleLocationDto.getLatitude());
                    building.setLongitude(googleLocationDto.getLongitude());
                }*/

                String[] splitDays = bargainItemDto.getDays().split(OpenApiContents.DELETEMETER_DATE);
                int startDay = Integer.parseInt(splitDays[0]);
                int endDay = Integer.parseInt(splitDays[1]);
                for (int i = startDay; i <= endDay; i++) {
                    Date date = new GregorianCalendar(bargainItemDto.getYear(), bargainItemDto.getMonthly() - 1, i).getTime();
                    BargainDate bargainDate = new BargainDate();
                    bargainDate.setBuilding(building);
                    bargainDate.setDate(date);
                    bargainDate.setPrice(bargainItemDto.getDealPrice().trim());

                    building.getBargainDates().add(bargainDate);
                }
                log.warn("insert building trade info => {}", building.toString());
                buildingRepository.save(building);
                return;
            }

        });
    }

    private Building buildingBuilder(BargainItemDto bargainItemDto){
        Building building = new Building();
        building.setCity(city);
        building.setGroop(groop);
        building.setDong(bargainItemDto.getDong());
        building.setName(bargainItemDto.getName());
        building.setArea(bargainItemDto.getArea());
        building.setFloor(bargainItemDto.getFloor());
        building.setType(Integer.parseInt(buildingType));
        building.setBuildingNum(bargainItemDto.getBuildingNum());
        building.setConstructYear(String.valueOf(bargainItemDto.getConstructYear()));
        building.setBargainDates(new HashSet<BargainDate>());
        building.setCharterDates(new HashSet<CharterDate>());
        building.setRentDates(new HashSet<RentDate>());

        return building;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ExitStatus.COMPLETED;
    }
}
