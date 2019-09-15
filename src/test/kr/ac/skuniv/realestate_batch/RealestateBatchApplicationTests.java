package kr.ac.skuniv.realestate_batch;

import kr.ac.skuniv.realestate_batch.util.OpenApiContents;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RealestateBatchApplicationTests {


    @Test
    public void contextLoads() {
        for(OpenApiContents.OpenApiRequest test : OpenApiContents.OpenApiRequest.values()){
            log.warn(String.format(test.getUrl(),"2000","3000"));

            //log.warn(test.getUrl());
        }
    }

    @Test
    public void contextLoads2() {
        Map<String, String> testMap = OpenApiContents.regionMap;

        for(String test : testMap.keySet()){
            log.warn(test);
        }
    }


}
