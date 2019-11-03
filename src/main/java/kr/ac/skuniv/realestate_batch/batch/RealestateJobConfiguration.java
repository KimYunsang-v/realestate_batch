package kr.ac.skuniv.realestate_batch.batch;


import kr.ac.skuniv.realestate_batch.batch.item.DataWriteTasklet;
import kr.ac.skuniv.realestate_batch.batch.item.RealestateItemReader;
import kr.ac.skuniv.realestate_batch.batch.item.RealestateItemWriter;
import kr.ac.skuniv.realestate_batch.batch.item.RealestatePartitioner;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.BuildingDealDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class RealestateJobConfiguration extends DefaultBatchConfigurer {

    private final RealestateItemReader realestateItemReader;
    private final RealestateItemWriter realestateItemWriter;
    private final RealestatePartitioner realestatePartitioner;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final DataWriteTasklet dataWriteTasklet;
    private final DataSource dataSource;

    @Bean
    public Job apiCallJob() {
        log.warn("----------api call job");
        return jobBuilderFactory.get("apiCallJob")
                .start(apiCallPartitionStep())
                //.next(dataWritePartitionStep())
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

//    @Bean
//    public Step dataWritePartitionStep()
//            throws UnexpectedInputException, ParseException {
//        return stepBuilderFactory.get("dataWritePartitionStep")
//                .partitioner("dataWritePartitionStep", realestatePartitioner)
//                .step(DataWriteStep())
//                .build();
//    }

    @Bean
    public Step apiCallStep() {
        return stepBuilderFactory.get("apiCallStep").<BuildingDealDto, BuildingDealDto>chunk(12)
                .reader(realestateItemReader)
                .writer(realestateItemWriter)
                .transactionManager(jpaTransactionManager())
                .build();
    }

//    @Bean
//    @Transactional
//    public Step DataWriteStep() {
//        return stepBuilderFactory.get("DataWriteStep")
//                .transactionManager(jpaTransactionManager())
//                .tasklet(dataWriteTasklet)
//                .build();
//    }

    @Bean
    @Primary
    public JpaTransactionManager jpaTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
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
