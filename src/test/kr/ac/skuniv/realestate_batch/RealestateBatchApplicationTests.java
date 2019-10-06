package kr.ac.skuniv.realestate_batch;

import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.NaverLocationDto;
import kr.ac.skuniv.realestate_batch.util.OpenApiContents;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@PropertySource("classpath:serviceKey.yaml")
public class RealestateBatchApplicationTests {

    @Value("${naverKey}")
    private String naverKey;
    private final String keyParam = ",+CA&key=";
    private final String baseUrl = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=";

    @Autowired
    RestTemplate restTemplate;

    @Test
    public void contextLoads() {
        for(OpenApiContents.OpenApiRequest test : OpenApiContents.OpenApiRequest.values()){
            log.warn(String.format(test.getUrl(),"2000","3000"));

            //log.warn(test.getUrl());

        }
    }

    @Test
    public void contextLoads2() throws Exception {

        StringBuilder sb = new StringBuilder();
        sb.append(baseUrl).append("역삼동 역삼푸르지오");

        URL url = new URL(sb.toString());

        URI uri = new URI(sb.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-NCP-APIGW-API-KEY-ID","apilagcuwe");
        headers.add("X-NCP-APIGW-API-KEY","OMxqAg88OBTX9TsPuhy4Ytu4Tmf8aYvEsqOTHEgr");
        /*BufferedReader bf;
        bf = new BufferedReader(new InputStreamReader(url.openStream()));*/

        /*String line = "";
        String result = "";

        while((line=bf.readLine())!=null){
            result=result.concat(line);
        }*/

//        log.warn(result);

        ResponseEntity<NaverLocationDto> naverLocationDto = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity(headers),NaverLocationDto.class);

        log.warn(naverLocationDto.getBody().toString());

    }
}
