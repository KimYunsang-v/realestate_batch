package kr.ac.skuniv.realestate_batch.batch.item;

import kr.ac.skuniv.realestate_batch.domain.dto.BargainDto;
import kr.ac.skuniv.realestate_batch.domain.dto.CharterAndRentDto;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.BuildingDealDto;
import kr.ac.skuniv.realestate_batch.util.OpenApiContents;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;


@Slf4j
@Configuration
@Component
@StepScope
@RequiredArgsConstructor
@PropertySource("classpath:serviceKey.yaml")
public class RealestateItemReader implements ItemReader<BuildingDealDto>, StepExecutionListener {

    @Value("${serviceKey}")
    private String serviceKey;
    private final RestTemplate restTemplate;
    private Iterator<URI> uriIterator;

    private String currentUri;
    @Value("#{jobParameters['requestDate']}")
    private String currentDate;
    private String currentRegionCode;
    private String currentBuildingType;
    private String currentDealType;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        ExecutionContext ctx = stepExecution.getExecutionContext();
        List<URI> uris = new ArrayList<>();
        Iterator<String> regionCodeIterator = OpenApiContents.regionMap.keySet().iterator();

        setVariable(ctx);
        List<URI> urlList = new ArrayList<>();

        while(regionCodeIterator.hasNext()) {
            currentRegionCode = regionCodeIterator.next();
            urlList.add(getUri());
        }
        uriIterator = urlList.iterator();
    }

    private void setVariable(ExecutionContext ctx) {
        currentUri = (String) ctx.get(OpenApiContents.URL);
        currentBuildingType = (String) ctx.get(OpenApiContents.BUILDING_TYPE);
        currentDealType = (String) ctx.get(OpenApiContents.DEAL_TYPE);
        //currentDate = "201101";
    }

    private URI getUri() {
        StringBuilder sb = new StringBuilder();
        URI resultUri;
        try {
            resultUri = new URI(
                    sb.append(String.format(currentUri, serviceKey, currentRegionCode, currentDate)).toString()
            );
            log.warn(resultUri.toString());
            return resultUri;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setBuildingWithDeal(BuildingDealDto buildingDealDto) {
        buildingDealDto.setDealType(currentDealType);
        buildingDealDto.setBuildingType(currentBuildingType);
    }

    @Override
    public BuildingDealDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        while(uriIterator.hasNext()){
            if (currentDealType.equals(OpenApiContents.BARGAIN_NUM)){
                URI uri = uriIterator.next();
                BargainDto bargainDto = restTemplate.getForObject(uri, BargainDto.class);
                setBuildingWithDeal(bargainDto);
                return bargainDto;
            }
            URI uri = uriIterator.next();
            CharterAndRentDto charterAndRentDto = restTemplate.getForObject(uri, CharterAndRentDto.class);
            setBuildingWithDeal(charterAndRentDto);
            return charterAndRentDto;
        }
        return null;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        return null;
    }
}
