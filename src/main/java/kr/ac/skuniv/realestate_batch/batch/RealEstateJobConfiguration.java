package kr.ac.skuniv.realestate_batch.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.orm.jpa.JpaTransactionManager;

import kr.ac.skuniv.realestate_batch.batch.item.RealEstateItemReader;
import kr.ac.skuniv.realestate_batch.batch.item.RealEstateItemWriter;
import kr.ac.skuniv.realestate_batch.batch.item.RealEstatePartitioner;
import kr.ac.skuniv.realestate_batch.domain.dto.abstractDto.BuildingDealDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class RealEstateJobConfiguration extends DefaultBatchConfigurer {

    private final RealEstateItemReader realEstateItemReader;
    private final RealEstateItemWriter realEstateItemWriter;
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
            .gridSize(6)
            .taskExecutor(taskExecutor())
            .build();
    }

    @Bean
    public Step apiCallStep() {
        return stepBuilderFactory.get("apiCallStep").<BuildingDealDto, BuildingDealDto>chunk(2)
                .reader(realEstateItemReader)
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

    @Bean
    public TaskExecutor taskExecutor(){
        return new SimpleAsyncTaskExecutor("spring_batch");
    }
}
