//package kr.ac.skuniv.realestate_batch.batch;
//
//import kr.ac.skuniv.realestate_batch.batch.item.PyItemReader;
//import kr.ac.skuniv.realestate_batch.batch.item.PyItemWriter;
//import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.BuildingDealDto;
//import kr.ac.skuniv.realestate_batch.domain.entity.BuildingEntity;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.orm.jpa.JpaTransactionManager;
//
//import javax.sql.DataSource;
//import java.util.List;
//
//@Slf4j
//@Configuration
//@RequiredArgsConstructor
//public class pyJobConfiguration extends DefaultBatchConfigurer {
//
//    private final DataSource dataSource;
//
//    private final JobBuilderFactory jobBuilderFactory;
//    private final StepBuilderFactory stepBuilderFactory;
//
//
//
////    @Bean
////    @Primary
////    public JpaTransactionManager jpaTransactionManager() {
////        final JpaTransactionManager transactionManager = new JpaTransactionManager();
////        transactionManager.setDataSource(dataSource);
////        return transactionManager;
////    }
//
//}
