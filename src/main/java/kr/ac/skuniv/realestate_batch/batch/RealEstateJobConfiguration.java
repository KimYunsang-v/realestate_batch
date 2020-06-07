package kr.ac.skuniv.realestate_batch.batch;


import java.util.List;

import kr.ac.skuniv.realestate_batch.batch.item.*;
import kr.ac.skuniv.realestate_batch.domain.dto.abstractDto.BuildingDealDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.sql.DataSource;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class RealEstateJobConfiguration extends DefaultBatchConfigurer {

    private final RealEstateItemReader realEstateItemReader;
    private final RealEstateItemWriter realEstateItemWriter;
    private final RealEstateProcessor realEstateProcessor;
    private final RealEstatePartitioner realEstatePartitioner;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final DataSource dataSource;

    @Bean
    public Job apiCallJob() {
        log.warn("----------api call job");
        return jobBuilderFactory.get("apiCallJob")
                .start(apiCallPartitionStep())
                .build();
    }

    @Bean
    public Step apiCallPartitionStep()
            throws UnexpectedInputException, ParseException {
        return stepBuilderFactory.get("apiCallPartitionStep")
                .partitioner("apiCallPartitionStep", realEstatePartitioner)
                .step(apiCallStep())
                .build();
    }

    @Bean
    public Step apiCallStep() {
        return stepBuilderFactory.get("apiCallStep").<List<BuildingDealDto>, List<BuildingDealDto>>chunk(5)
                .reader(realEstateItemReader)
                .processor(realEstateProcessor)
                .writer(realEstateItemWriter)
                .transactionManager(jpaTransactionManager())
                .build();
    }

    @Bean
    public JobExecutionListener listener() {
        return new JobListener();
    }

    @Bean
    @Primary
    public JpaTransactionManager jpaTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }
}
