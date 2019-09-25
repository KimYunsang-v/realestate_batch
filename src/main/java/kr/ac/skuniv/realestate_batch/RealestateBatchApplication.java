package kr.ac.skuniv.realestate_batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@EnableBatchProcessing
public class RealestateBatchApplication {
    public static void main(String[] args){
        SpringApplication.run(RealestateBatchApplication.class, args);
    }
}