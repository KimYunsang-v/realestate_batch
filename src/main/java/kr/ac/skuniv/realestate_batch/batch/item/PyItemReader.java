package kr.ac.skuniv.realestate_batch.batch.item;

import kr.ac.skuniv.realestate_batch.domain.entity.BargainDate;
import kr.ac.skuniv.realestate_batch.domain.entity.BuildingEntity;
import kr.ac.skuniv.realestate_batch.domain.entity.CharterDate;
import kr.ac.skuniv.realestate_batch.repository.BargainDateRepository;
import kr.ac.skuniv.realestate_batch.repository.BuildingEntityRepository;
import kr.ac.skuniv.realestate_batch.repository.CharterDateRepository;
import kr.ac.skuniv.realestate_batch.util.OpenApiContents;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Iterator;
import java.util.List;

@Slf4j
@StepScope
@Configuration
@RequiredArgsConstructor
public class PyItemReader implements ItemReader<List<CharterDate>>, StepExecutionListener {

    @Autowired
    private BuildingEntityRepository buildingEntityRepository;
    @Autowired
    private BargainDateRepository bargainDateRepository;
    @Autowired
    private CharterDateRepository charterDateRepository;
    private Iterator<String> regionCodeIterator;

    long count;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        count = charterDateRepository.count();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

    @Override
    public List<CharterDate> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        long current = 0;

//        List<BargainDate> bargainDateList = bargainDateRepository.findByPyPriceIsNull();
//        while(current <= count){
//            List<BargainDate> byCount = bargainDateRepository.findByCount(current);
//            count += 1000;
//            return byCount;
//        }

        List<CharterDate> byCount = charterDateRepository.findByCount(current);

        return byCount;
    }
}
