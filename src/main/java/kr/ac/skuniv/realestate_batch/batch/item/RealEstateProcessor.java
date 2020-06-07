package kr.ac.skuniv.realestate_batch.batch.item;

import java.util.List;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import kr.ac.skuniv.realestate_batch.domain.dto.abstractDto.BuildingDealDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@Component
@StepScope
@RequiredArgsConstructor
public class RealEstateProcessor implements ItemProcessor<List<BuildingDealDto>, List<BuildingDealDto>> {

	@Override
	public List<BuildingDealDto> process(List<BuildingDealDto> buildingDealDtos) throws Exception {
		log.info(buildingDealDtos.size() + "");
		return buildingDealDtos;
	}
}
