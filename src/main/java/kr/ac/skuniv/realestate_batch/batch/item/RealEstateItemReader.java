package kr.ac.skuniv.realestate_batch.batch.item;

import kr.ac.skuniv.realestate_batch.domain.dto.SaleDto;
import kr.ac.skuniv.realestate_batch.domain.dto.RentDto;
import kr.ac.skuniv.realestate_batch.domain.dto.abstractDto.BuildingDealDto;
import kr.ac.skuniv.realestate_batch.util.OpenApiContents;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.*;


@Slf4j
@Configuration
@Component
@StepScope
@RequiredArgsConstructor
@PropertySource("classpath:serviceKey.yaml")
public class RealEstateItemReader implements ItemReader<BuildingDealDto>, StepExecutionListener {

    @Value("${serviceKey}")
    private String serviceKey;
    private final RestTemplate restTemplate;
    private Iterator<URI> uriIterator;
    private List<URI> urlList;

    private String currentUri;
//    @Value("#{jobParameters['requestDate']}")
    private String currentDate="201910";
    private String currentRegionCode;
    private String currentBuildingType;
    private String currentDealType;

    @SneakyThrows
    @Override
    public void beforeStep(StepExecution stepExecution) {
        ExecutionContext ctx = stepExecution.getExecutionContext();
        Iterator<String> regionCodeIterator = OpenApiContents.regionMap.keySet().iterator();

        currentUri = (String) ctx.get(OpenApiContents.URL);
        currentBuildingType = (String) ctx.get(OpenApiContents.BUILDING_TYPE);
        currentDealType = (String) ctx.get(OpenApiContents.DEAL_TYPE);
        urlList = new ArrayList<>();

        regionCodeIterator.forEachRemaining(code -> {
            StringBuilder sb = new StringBuilder();
            try {
                urlList.add(new URI(
                    sb.append(String.format(currentUri, serviceKey, code, currentDate)).toString()
                ));
            } catch (Exception e){
                log.error(e.getMessage());
            }
        });

        log.info("read before step " + urlList.size());
        uriIterator = urlList.iterator();
    }

    @Override
    public BuildingDealDto read()  {
        long start = System.currentTimeMillis();

        BuildingDealDto buildingDealDto;

        while(uriIterator.hasNext()){
            if (currentDealType.equals(OpenApiContents.BARGAIN_NUM)){
                URI uri = uriIterator.next();
                SaleDto saleDto = restTemplate.getForObject(uri, SaleDto.class);
                saleDto.setDealType(currentDealType);
                saleDto.setBuildingType(currentBuildingType);
                buildingDealDto = saleDto;
                return buildingDealDto;
            }
            URI uri = uriIterator.next();
            RentDto rentDto = restTemplate.getForObject(uri, RentDto.class);
            rentDto.setDealType(currentDealType);
            rentDto.setBuildingType(currentBuildingType);
            buildingDealDto = rentDto;
            return buildingDealDto;
        }

        long end = System.currentTimeMillis();
        log.warn("1개 url read" + (end-start)/1000 +" 초 걸림");
        return null;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
