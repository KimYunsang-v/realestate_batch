package kr.ac.skuniv.realestate_batch;


import kr.ac.skuniv.realestate_batch.util.OpenApiContents;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class batchTest implements StepExecutionListener {

    @Override
    @Test
    public void beforeStep(StepExecution stepExecution) {
        ExecutionContext ctx = stepExecution.getExecutionContext();

        log.warn(ctx.get(OpenApiContents.URL).toString());
    }

    @Override
    @Test
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
