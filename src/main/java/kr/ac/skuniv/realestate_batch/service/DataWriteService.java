package kr.ac.skuniv.realestate_batch.service;


import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.BargainItemDto;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.CharterAndRentItemDto;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.ItemDto;
import kr.ac.skuniv.realestate_batch.domain.entity.BargainDate;
import kr.ac.skuniv.realestate_batch.domain.entity.BuildingEntity;
import kr.ac.skuniv.realestate_batch.domain.entity.CharterDate;
import kr.ac.skuniv.realestate_batch.domain.entity.RentDate;
import kr.ac.skuniv.realestate_batch.repository.BuildingEntityRepository;
import kr.ac.skuniv.realestate_batch.util.CommonFunction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.function.Supplier;

@Service
@Log4j2
@RequiredArgsConstructor
public class DataWriteService {

    private final BuildingEntityRepository buildingEntityRepository;

    public BuildingEntity getBuildingEntity(ItemDto itemDto, String buildingType){
        int city = Integer.parseInt(itemDto.getRegionCode().substring(0,2));
        int groop = Integer.parseInt(itemDto.getRegionCode().substring(2));

        return buildingEntityRepository.findByCityAndGroopAndBuildingNumAndFloor(city, groop, itemDto.getBuildingNum(), itemDto.getFloor())
            .orElseGet(() -> new BuildingEntity().builder()
                .city(city).groop(groop).dong(itemDto.getDong())
                .name(itemDto.getName()).area(itemDto.getArea())
                .floor(itemDto.getFloor()).type(buildingType)
                .buildingNum(itemDto.getBuildingNum())
                .constructYear(String.valueOf(itemDto.getConstructYear()))
                .bargainDates(new HashSet<>())
                .charterDates(new HashSet<>())
                .rentDates(new HashSet<>())
                .build());
    }

    public BuildingEntity addNewBargainDate(BargainItemDto bargainItemDto, String buildingType){
        String price = bargainItemDto.getDealPrice().trim().replaceAll("[^0-9?!\\.]","");
        BuildingEntity buildingEntity = getBuildingEntity(bargainItemDto, buildingType);
        buildingEntity.getBargainDates().add(new BargainDate().builder()
            .date(CommonFunction.getDate(bargainItemDto))
            .buildingEntity(buildingEntity)
            .price(price)
            .pyPrice(getPyPrice(buildingEntity.getArea(), price)).build());

        return buildingEntity;
    }

    public BuildingEntity addNewCharterDateOrRentDate(CharterAndRentItemDto charterAndRentItemDto, String buildingType){
        String guaranteePrice = CommonFunction.removeMoneyBlank(charterAndRentItemDto.getGuaranteePrice());
        String monthlyPrice = CommonFunction.removeMoneyBlank(charterAndRentItemDto.getMonthlyPrice());
        BuildingEntity buildingEntity = getBuildingEntity(charterAndRentItemDto, buildingType);

        if(Integer.parseInt(monthlyPrice) == 0){
            buildingEntity.getCharterDates().add(new CharterDate().builder()
                .date(CommonFunction.getDate(charterAndRentItemDto))
                .buildingEntity(buildingEntity).price(guaranteePrice)
                .pyPrice(getPyPrice(buildingEntity.getArea(), guaranteePrice))
                .build());
        }else{
            buildingEntity.getRentDates().add(
                new RentDate().builder()
                    .date(CommonFunction.getDate(charterAndRentItemDto))
                    .buildingEntity(buildingEntity)
                    .guaranteePrice(CommonFunction.removeMoneyBlank(charterAndRentItemDto.getGuaranteePrice()))
                    .monthlyPrice(monthlyPrice)
                    .build());
        }

        return buildingEntity;
    }

    // public BuildingEntity addNewRentDate(CharterAndRentItemDto charterAndRentItemDto, String buildingType){
    //     String price = CommonFunction.removeMoneyBlank(charterAndRentItemDto.getMonthlyPrice());
    //     BuildingEntity buildingEntity = getBuildingEntity(charterAndRentItemDto, buildingType);
    //     buildingEntity.getRentDates().add(
    //         new RentDate().builder()
    //         .date(CommonFunction.getDate(charterAndRentItemDto))
    //         .buildingEntity(buildingEntity)
    //         .guaranteePrice(CommonFunction.removeMoneyBlank(charterAndRentItemDto.getGuaranteePrice()))
    //         .monthlyPrice(price)
    //         .build());
    //
    //     return buildingEntity;
    // }

    private Double getPyPrice(Double area, String price) {
        double py = 3.3;
        if(area == null) {
            return null;
        }
        double buildingPy = area / py;
        return Double.valueOf(price) / buildingPy;
    }
}
