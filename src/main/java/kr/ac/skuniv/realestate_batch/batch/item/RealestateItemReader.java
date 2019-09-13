package kr.ac.skuniv.realestate_batch.batch.item;

import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.BuildingDealDto;
import kr.ac.skuniv.realestate_batch.util.OpenApiContents;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Slf4j
@Configuration
@Component
@StepScope
@RequiredArgsConstructor
public class RealestateItemReader implements ItemReader<BuildingDealDto>, StepExecutionListener {

    private final RestTemplate restTemplate;
    private List<URI> uris;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        Set<String> regionCodes = OpenApiContents.regionMap.keySet();

        for(String regionCode : regionCodes){
            StringBuilder sb = new StringBuilder();
            uris.add(new URI(

            ));
        }

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
