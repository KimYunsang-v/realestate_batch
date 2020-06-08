package kr.ac.skuniv.realestate_batch.service;


import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.RentItemDto;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.SaleItemDto;
import kr.ac.skuniv.realestate_batch.domain.entity.Rent;
import kr.ac.skuniv.realestate_batch.domain.entity.Sale;
import kr.ac.skuniv.realestate_batch.util.CommonFunction;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class DataWriteService {

    public Sale createSaleEntity(SaleItemDto saleItemDto) {
        String price = CommonFunction.removeMoneyBlank(saleItemDto.getDealPrice());
        return Sale.builder()
            .area(saleItemDto.getArea())
            .date(CommonFunction.getDate(saleItemDto))
            .name(saleItemDto.getName())
            .city(CommonFunction.getCityCode(saleItemDto.getRegionCode()))
            .groop(CommonFunction.getGroopCode(saleItemDto.getRegionCode()))
            .dong(saleItemDto.getDong())
            .price(price)
            .pyPrice(getPyPrice(saleItemDto.getArea(), price))
            .build();
    }

    public Rent createRentEntity(RentItemDto rentItemDto) {
        String guaranteePrice = CommonFunction.removeMoneyBlank(rentItemDto.getGuaranteePrice());
        String monthlyPrice = CommonFunction.removeMoneyBlank(rentItemDto.getMonthlyPrice());
        if(Integer.parseInt(monthlyPrice) != 0){
            guaranteePrice = changeMonthlyPriceToGuaranteePrice(guaranteePrice, monthlyPrice);
        }

        return Rent.builder()
            .area(rentItemDto.getArea())
            .date(CommonFunction.getDate(rentItemDto))
            .name(rentItemDto.getName())
            .city(CommonFunction.getCityCode(rentItemDto.getRegionCode()))
            .groop(CommonFunction.getGroopCode(rentItemDto.getRegionCode()))
            .dong(rentItemDto.getDong())
            .price(guaranteePrice)
            .pyPrice(getPyPrice(rentItemDto.getArea(), guaranteePrice))
            .build();
    }

    private String changeMonthlyPriceToGuaranteePrice (String guaranteePrice, String monthlyPrice){
        return (Integer.parseInt(guaranteePrice) + (Integer.parseInt(monthlyPrice) * 100)) + "";
    }

    private Double getPyPrice(Double area, String price) {
        double py = 3.3;
        if(area == null) {
            return null;
        }
        double buildingPy = area / py;
        return Double.valueOf(price) / buildingPy;
    }
}
