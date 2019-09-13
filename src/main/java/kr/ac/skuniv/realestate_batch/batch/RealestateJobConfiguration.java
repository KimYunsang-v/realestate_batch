package kr.ac.skuniv.realestate_batch.batch;


import kr.ac.skuniv.realestate_batch.batch.item.RealestateItemReader;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.BuildingDealDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class RealestateJobConfiguration extends DefaultBatchConfigurer {

    private final RealestateItemReader realestateItemReader;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

//    @Bean
//    public Job apiCallJob() {
//        return jobBuilderFactory.get("apiCallJob")
//                .start(apiCallStep())
//                .build();
//    }

//    @Bean
//    public Step apiCallStep() {
//        return stepBuilderFactory.get("apiCallStep").<BuildingDealDto, BuildingDealDto>chunk(6)
//                .reader(realestateItemReader)
//                .build();
//    }
}
