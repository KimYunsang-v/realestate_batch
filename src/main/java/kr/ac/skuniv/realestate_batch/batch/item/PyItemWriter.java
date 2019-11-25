package kr.ac.skuniv.realestate_batch.batch.item;

import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.BuildingDealDto;
import kr.ac.skuniv.realestate_batch.domain.entity.BargainDate;
import kr.ac.skuniv.realestate_batch.domain.entity.BuildingEntity;
import kr.ac.skuniv.realestate_batch.domain.entity.CharterDate;
import kr.ac.skuniv.realestate_batch.domain.entity.RentDate;
import kr.ac.skuniv.realestate_batch.repository.BargainDateRepository;
import kr.ac.skuniv.realestate_batch.repository.BuildingEntityRepository;
import kr.ac.skuniv.realestate_batch.repository.CharterDateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@StepScope
@Configuration
@RequiredArgsConstructor
public class PyItemWriter implements ItemWriter<List<CharterDate>>, StepExecutionListener, InitializingBean {
    @Autowired
    private BuildingEntityRepository buildingEntityRepository;

    @Autowired
    private BargainDateRepository bargainDateRepository;

    @Autowired
    private CharterDateRepository charterDateRepository;

    @Override
    public void beforeStep(StepExecution stepExecution) {

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

    @Override
    public void write(List<? extends List<CharterDate>> list) throws Exception {
        log.info(list.size() + "    " + list.get(0));

        List<CharterDate> charterDateList = new ArrayList<>();
        list.forEach(item -> {
            item.forEach(charterDate -> {
                if(charterDate.getBuildingEntity().getArea() != null) {
                    Double area = charterDate.getBuildingEntity().getArea();
                    Double py = 3.3;
                    double v = area / py;
                    charterDate.setPyPrice(Double.valueOf(charterDate.getPrice()) / v);
                }
            });
            charterDateList.addAll(item);
        });

        log.warn("list size : " + charterDateList.size());

        charterDateRepository.saveAll(charterDateList);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
