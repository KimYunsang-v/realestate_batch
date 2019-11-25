package kr.ac.skuniv.realestate_batch.batch;


import kr.ac.skuniv.realestate_batch.batch.item.*;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.BuildingDealDto;
import kr.ac.skuniv.realestate_batch.domain.entity.BargainDate;
import kr.ac.skuniv.realestate_batch.domain.entity.BuildingEntity;
import kr.ac.skuniv.realestate_batch.domain.entity.CharterDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class RealestateJobConfiguration extends DefaultBatchConfigurer {

    private final RealestateItemReader realestateItemReader;
    private final RealestateItemWriter realestateItemWriter;
    private final RealestatePartitioner realestatePartitioner;


    private final PyItemReader pyItemReader;
    private final PyItemWriter pyItemWriter;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final JobLauncher jobLauncher;


    private final DataSource dataSource;
    private Integer startYear = 2019;
    private Integer startMonth = 2;

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
                .partitioner("apiCallPartitionStep", realestatePartitioner)
                .step(apiCallStep())
                .build();
    }

    @Bean
    public Step apiCallStep() {
        return stepBuilderFactory.get("apiCallStep").<BuildingDealDto, BuildingDealDto>chunk(12)
                .reader(realestateItemReader)
                .writer(realestateItemWriter)
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
////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    @Bean
//    public Job apiCallJob() {
//        log.warn("----------api call job");
//        return jobBuilderFactory.get("apiCallJob")
//                .start(apiCallStep2())
//                .build();
//    }

    @Bean
    public Step apiCallStep2() {
        return stepBuilderFactory.get("apiCallStep2").<List<CharterDate>, List<CharterDate>>chunk(1)
                .reader(pyItemReader)
                .writer(pyItemWriter)
                .transactionManager(jpaTransactionManager())
                .build();
    }

//    private JpaItemWriter<Building> writer() {
//        JpaItemWriter<Building> writer =  new JpaItemWriter<>();
//        writer.setEntityManagerFactory(entityManagerFactory);
//        return writer;
//    }

//    @Bean
//    @Primary
//    public JpaTransactionManager jpaTransactionManager() {
//        final JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setDataSource(dataSource);
//        return transactionManager;
//    }
}
