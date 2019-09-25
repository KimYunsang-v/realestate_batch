package kr.ac.skuniv.realestate_batch.batch.item;

import com.google.gson.Gson;
import kr.ac.skuniv.realestate_batch.domain.dto.BargainDto;
import kr.ac.skuniv.realestate_batch.domain.dto.CharterAndRentDto;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.BargainItemDto;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.BuildingDealDto;
import kr.ac.skuniv.realestate_batch.domain.dto.openApiDto.CharterAndRentItemDto;
import kr.ac.skuniv.realestate_batch.util.OpenApiContents;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Slf4j
@StepScope
@Configuration
@RequiredArgsConstructor
@PropertySource("classpath:serviceKey.yaml")
public class RealestateItemWriter implements ItemWriter<BuildingDealDto>, StepExecutionListener {

    private static final Gson gson = new Gson();

    private String fileName;
    @Value("${filePath}")
    private String filePath;
    private BufferedWriter bufferedWriter;
    private String dealType;
    private String buildingType;

    private void write(BufferedWriter bw, String content) throws IOException {
        bw.write(content);
        bw.newLine();
    }

    private void divisionItem(BuildingDealDto item, BufferedWriter bw) throws IOException {
        if (item.getDealType().equals(OpenApiContents.BARGAIN_NUM)){
            BargainDto bargainDto = (BargainDto) item;
            for (BargainItemDto bargainItemDto : bargainDto.getBody().getItem()){
                write(bw, gson.toJson(bargainItemDto));
            }
        } else {
            CharterAndRentDto charterAndRentDto = (CharterAndRentDto) item;
            for (CharterAndRentItemDto charterAndRentItemDto : charterAndRentDto.getBody().getItem()) {
                write(bw, gson.toJson(charterAndRentItemDto));
            }
        }
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        ExecutionContext ctx = stepExecution.getExecutionContext();
        dealType = (String) ctx.get(OpenApiContents.DEAL_TYPE);
        buildingType = (String) ctx.get(OpenApiContents.BUILDING_TYPE);


        fileName = (String) ctx.get(OpenApiContents.API_KIND);
        String fileFullPath = filePath + OpenApiContents.FILE_DELEMETER_WINDOWS + fileName;
        File f = new File(fileFullPath);
        log.warn("파일 이름 ====== " + f.getAbsolutePath());
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(fileFullPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(List<? extends BuildingDealDto> items) throws Exception {
        for (BuildingDealDto item : items){
            log.warn("item =======  " + item.toString());
            //divisionItem(item, bufferedWriter);
        }
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ExitStatus.COMPLETED;
    }
}
