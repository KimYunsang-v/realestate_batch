package kr.ac.skuniv.realestate_batch.service;


import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.BargainItemDto;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.CharterAndRentItemDto;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.ItemDto;
import kr.ac.skuniv.realestate_batch.domain.entity.BargainDate;
import kr.ac.skuniv.realestate_batch.domain.entity.BuildingEntity;
import kr.ac.skuniv.realestate_batch.domain.entity.CharterDate;
import kr.ac.skuniv.realestate_batch.domain.entity.RentDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;

@Service
public class DataWriteService {

    private int city, groop;
    private String buildingType, address;
    private BuildingEntity buildingEntity;
    private Date date;

    public BuildingEntity buildBuildingEntity(ItemDto itemDto){
         buildingEntity =  new BuildingEntity().builder().city(city).groop(groop).dong(itemDto.getDong())
                .name(itemDto.getName()).area(itemDto.getArea()).floor(itemDto.getFloor()).type(buildingType)
                .buildingNum(itemDto.getBuildingNum()).constructYear(String.valueOf(itemDto.getConstructYear()))
                .bargainDates(new HashSet<BargainDate>())
                .charterDates(new HashSet<CharterDate>())
                .rentDates(new HashSet<RentDate>())
                .build();

         setLocation();
         return buildingEntity;
    }

    public BargainDate buildBargainDate(BargainItemDto bargainItemDto){
        return new BargainDate().builder()
                .date(date)
                .buildingEntity(buildingEntity)
                .price(bargainItemDto.getDealPrice().trim()).build();
    }

    public CharterDate buildCharterDate(CharterAndRentItemDto charterAndRentItemDto){
        return new CharterDate().builder().date(date).buildingEntity(buildingEntity).price(charterAndRentItemDto.getGuaranteePrice().trim()).build();
    }

    public RentDate buildRentDate(CharterAndRentItemDto charterAndRentItemDto){
        return new RentDate().builder().date(date).buildingEntity(buildingEntity)
                .guaranteePrice(charterAndRentItemDto.getGuaranteePrice())
                .monthlyPrice(charterAndRentItemDto.getMonthlyPrice()).build();
    }

    public void setData(ItemDto itemDto) {
        address = itemDto.getDong() + itemDto.getName();
        city = Integer.parseInt(itemDto.getRegionCode().substring(0,2));
        groop = Integer.parseInt(itemDto.getRegionCode().substring(2));
        date = new GregorianCalendar(itemDto.getYear(), itemDto.getMonthly() - 1, Integer.parseInt(itemDto.getDays())).getTime();
    }

    public void setLocation() {
        /*GoogleLocationDto googleLocationDto = googleLocationApiService.googleLocationApiCall(address.replaceAll(" ",""));
                if (googleLocationDto != null){
                    building.setLatitude(googleLocationDto.getLatitude());
                    building.setLongitude(googleLocationDto.getLongitude());
                }*/
    }
}