package kr.ac.skuniv.realestate_batch.batch;

import kr.ac.skuniv.realestate_batch.batch.item.DataWriteTasklet;
import kr.ac.skuniv.realestate_batch.batch.item.RealestatePartitioner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class DataWriteConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final RealestatePartitioner realestatePartitioner;
    private final DataWriteTasklet dataWriteTasklet;
    private final DataSource dataSource;


//    @Bean
//    public Job extractDiffDataJob() {
//        log.warn("----------diff data job");
//        return jobBuilderFactory.get("extractDiffDataJob")
//                .start(extractDiffDataPartitionStep())
//                .build();
//    }
//
//    @Bean
//    public Step extractDiffDataPartitionStep()
//            throws UnexpectedInputException, ParseException {
//        return stepBuilderFactory.get("extractDiffDataPartitionStep")
//                .partitioner("extractDiffDataPartitionStep", realestatePartitioner)
//                .step(extractDiffDataTrtStep())
//                .build();
//    }

//    @Bean
//    @Transactional
//    public Step extractDiffDataTrtStep() {
//        return stepBuilderFactory.get("extractDiffDataTrtStep")
//                .transactionManager(jpaTransactionManager())
//                .tasklet(dataWriteTasklet)
//                .build();
//    }
//
//    @Bean
//    @Primary
//    public JpaTransactionManager jpaTransactionManager() {
//        final JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setDataSource(dataSource);
//        return transactionManager;
//    }
}
