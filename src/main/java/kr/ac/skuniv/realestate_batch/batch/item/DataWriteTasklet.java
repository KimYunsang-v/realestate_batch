package kr.ac.skuniv.realestate_batch.batch.item;

import antlr.StringUtils;
import com.google.gson.Gson;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.BargainItemDto;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.CharterAndRentItemDto;
import kr.ac.skuniv.realestate_batch.domain.entity.BargainDate;
import kr.ac.skuniv.realestate_batch.domain.entity.Building;
import kr.ac.skuniv.realestate_batch.domain.entity.CharterDate;
import kr.ac.skuniv.realestate_batch.domain.entity.RentDate;
import kr.ac.skuniv.realestate_batch.repository.BuildingRepository;
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
    private final BuildingRepository buildingRepository;
//    private final GoogleLocationApiService googleLocationApiService;
    @Value("${filePath}")
    private String filePath;
    private String fileName;
    private String dealType;
    private List newDataList;
    private String buildingType;

    private int city;
    private int groop;


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

            // 매매일 경우
            if(dealType.equals(OpenApiContents.BARGAIN_NUM)){
                log.warn("매매!!");
                BargainItemDto bargainItemDto = (BargainItemDto) item;
                city = Integer.parseInt(bargainItemDto.getRegionCode().substring(0,2));
                groop = Integer.parseInt(bargainItemDto.getRegionCode().substring(2));

                Building building = buildingBuilder();
                building.setDong(bargainItemDto.getDong());
                building.setName(bargainItemDto.getName());
                building.setArea(bargainItemDto.getArea());
                building.setFloor(bargainItemDto.getFloor());
                building.setBuildingNum(bargainItemDto.getBuildingNum());
                building.setConstructYear(String.valueOf(bargainItemDto.getConstructYear()));

                String address = bargainItemDto.getDong() + bargainItemDto.getName();
                /*GoogleLocationDto googleLocationDto = googleLocationApiService.googleLocationApiCall(address.replaceAll(" ",""));
                if (googleLocationDto != null){
                    building.setLatitude(googleLocationDto.getLatitude());
                    building.setLongitude(googleLocationDto.getLongitude());
                }*/

                //String[] splitDays = bargainItemDto.getDays().split(OpenApiContents.DELETEMETER_DATE);
//                int startDay = Integer.parseInt(splitDays[0]);
//                int endDay = Integer.parseInt(splitDays[1]);
                //for (int i = startDay; i <= endDay; i++) {
                Date date = new GregorianCalendar(bargainItemDto.getYear(), bargainItemDto.getMonthly() - 1, Integer.parseInt(bargainItemDto.getDays())).getTime();
                BargainDate bargainDate = new BargainDate();
                bargainDate.setBuilding(building);
                bargainDate.setDate(date);
                bargainDate.setPrice(bargainItemDto.getDealPrice().trim());

                building.getBargainDates().add(bargainDate);
                //}
                log.warn("insert building trade info => {}", building.toString());
                buildingRepository.save(building);
                return;
            }

            // 전월세의 경우
            CharterAndRentItemDto charterWithRentItemDto = (CharterAndRentItemDto) item;
            city = Integer.parseInt(charterWithRentItemDto.getRegionCode().substring(0, 2));
            groop = Integer.parseInt(charterWithRentItemDto.getRegionCode().substring(2));
            //Building building = buildingRepository.findByCityAndGroopAndBuildingNumAndFloor(city, groop, charterWithRentItemDto.getBuildingNum(), charterWithRentItemDto.getFloor());
//            if (building == null) {
////                building = new Building.Builder().city(city).groop(groop).dong(charterWithRentItemDto.getDong())
////                        .name(charterWithRentItemDto.getName()).area(charterWithRentItemDto.getArea()).floor(charterWithRentItemDto.getFloor()).type(buildingType)
////                        .buildingNum(charterWithRentItemDto.getBuildingNum()).constructYear(String.valueOf(charterWithRentItemDto.getConstructYear()))
////                        .bargainDates(new HashSet<BargainDate>())
////                        .charterDates(new HashSet<CharterDate>())
////                        .rentDates(new HashSet<RentDate>())
////                        .build();
//                Bui
//                String address = charterWithRentItemDto.getDong() + charterWithRentItemDto.getName();
////                GoogleLocationDto googleLocationDto = googleLocationApiService.googleLocationApiCall(address.replaceAll(" ",""));
////                if (googleLocationDto != null){
////                    building.setLatitude(googleLocationDto.getLatitude());
////                    building.setLongitude(googleLocationDto.getLongitude());
////                }
//            }

            Building building = buildingBuilder();
            building.setDong(charterWithRentItemDto.getDong());
            building.setName(charterWithRentItemDto.getName());
            building.setArea(charterWithRentItemDto.getArea());
            building.setFloor(charterWithRentItemDto.getFloor());
            building.setBuildingNum(charterWithRentItemDto.getBuildingNum());
            building.setConstructYear(String.valueOf(charterWithRentItemDto.getConstructYear()));

            log.warn("building create => {}", building.toString());
//            String[] splitDays = charterWithRentItemDto.getDays().split(OpenApiContents.DELETEMETER_DATE);
//            int startDay = Integer.parseInt(splitDays[0]);
//            int endDay = Integer.parseInt(splitDays[1]);
           // log.warn("start day => {}, end day => {}", startDay, endDay);
            if (Integer.parseInt(charterWithRentItemDto.getMonthlyPrice().trim()) != 0) {
                log.warn("월세!!!");
                // 월세
               // for (int i = startDay; i <= endDay; i++) {
                Date date = new GregorianCalendar(charterWithRentItemDto.getYear(), charterWithRentItemDto.getMonthly() - 1, Integer.parseInt(charterWithRentItemDto.getDays())).getTime();
//                    RentDate rentDate = new RentDate.Builder().building(building).date(date)
//                            .guaranteePrice(charterWithRentItemDto.getGuaranteePrice().trim())
//                            .monthlyPrice(charterWithRentItemDto.getMonthlyPrice().trim())
//                            .build();
//                    building.getRentDates().add(rentDate);

                RentDate rentDate = new RentDate();

                rentDate.setDate(date);
                rentDate.setBuilding(building);
                rentDate.setGuaranteePrice(charterWithRentItemDto.getGuaranteePrice().trim());
                rentDate.setMonthlyPrice(charterWithRentItemDto.getMonthlyPrice().trim());

                building.getRentDates().add(rentDate);
                //}
                log.warn("insert building trade info => {}", building.toString());
                buildingRepository.save(building);
                return;
            }
            // 전세
            log.warn("전세!!!");
            //for (int i = startDay; i <= endDay; i++) {
            Date date = new GregorianCalendar(charterWithRentItemDto.getYear(), charterWithRentItemDto.getMonthly() - 1, Integer.parseInt(charterWithRentItemDto.getDays())).getTime();
//                CharterDate charterDate = new CharterDate.Builder().building(building).date(date)
//                        .price(charterWithRentItemDto.getGuaranteePrice().trim())
//                        .build();


            CharterDate charterDate = new CharterDate();
            charterDate.setBuilding(building);
            charterDate.setDate(date);
            charterDate.setPrice(charterWithRentItemDto.getGuaranteePrice().trim());

                building.getCharterDates().add(charterDate);

            //}
            log.warn("insert building trade info => {}", building.toString());
            buildingRepository.save(building);
        });

        buildingRepository.flush();
    }

    private Building buildingBuilder(){
        Building building = new Building();
        building.setCity(city);
        building.setGroop(groop);
        building.setType(buildingType);

        building.setBargainDates(new HashSet<BargainDate>());
        building.setCharterDates(new HashSet<CharterDate>());
        building.setRentDates(new HashSet<RentDate>());

        return building;
    }

//    private Building CharterAndRentItemDto2buildingBuilder(CharterAndRentItemDto charterAndRentItemDto){
//        Building building = new Building();
//        building.setCity(city);
//        building.setGroop(groop);
//        building.setDong(charterAndRentItemDto.getDong());
//        building.setName(charterAndRentItemDto.getName());
//        building.setArea(charterAndRentItemDto.getArea());
//        building.setFloor(charterAndRentItemDto.getFloor());
//        building.setType(buildingType);
//        building.setBuildingNum(charterAndRentItemDto.getBuildingNum());
//        building.setConstructYear(String.valueOf(charterAndRentItemDto.getConstructYear()));
//        building.setBargainDates(new HashSet<BargainDate>());
//        building.setCharterDates(new HashSet<CharterDate>());
//        building.setRentDates(new HashSet<RentDate>());
//
//        return building;
//    }

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
