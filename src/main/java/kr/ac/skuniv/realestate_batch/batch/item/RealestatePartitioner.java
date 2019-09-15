package kr.ac.skuniv.realestate_batch.batch.item;

import kr.ac.skuniv.realestate_batch.util.OpenApiContents;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class RealestatePartitioner implements Partitioner {

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> map = new HashMap<>();
        int i = 0;
        for (OpenApiContents.OpenApiRequest myEnum : OpenApiContents.OpenApiRequest.values()) {
            log.warn("enum name => {}", myEnum.name());
            ExecutionContext context = new ExecutionContext();
            context.putString(OpenApiContents.API_KIND, myEnum.name());
            context.putString(OpenApiContents.URL, myEnum.getUrl());
            context.putString(OpenApiContents.BUILDING_TYPE, myEnum.getBuildingType());
            context.putString(OpenApiContents.DEAL_TYPE, myEnum.getDealType());
            map.put(OpenApiContents.PARTITION_KEY + i, context);
            i++;
        }
        return map;
    }
}
