package kr.ac.skuniv.realestate_batch.batch.item;

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
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.*;


@Slf4j
@Configuration
@Component
@StepScope
@RequiredArgsConstructor
public class RealestateItemReader implements ItemReader<BuildingDealDto>, StepExecutionListener {

    private final RestTemplate restTemplate;
    private Iterator<URI> uriIterator;
    @Value("${service.yaml.serviceKey")
    private String serviceKey;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        OpenApiContents.OpenApiRequest[] apiRequests = OpenApiContents.OpenApiRequest.values();

        ExecutionContext ctx = stepExecution.getExecutionContext();
        List<URI> uris = new ArrayList<>();
        Iterator<String> regionCodeIterator = OpenApiContents.regionMap.keySet().iterator();

        log.warn(ctx.get(OpenApiContents.URL).toString());

        for(OpenApiContents.OpenApiRequest uri : OpenApiContents.OpenApiRequest.values()) {

//            uris.(new URI(
//                sb.append(String.format(uri, serviceKey, regionCodes.))
//            ));
        }

//        for(String regionCode : regionCodes){
//            StringBuilder sb = new StringBuilder();
//            uris.add(new URI(
//                    sb.append(String.format(apiRequests))
//            ));
//        }

    }

    @Override
    public BuildingDealDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return null;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
