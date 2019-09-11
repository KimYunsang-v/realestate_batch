package kr.ac.skuniv.realestate_batch.batch;

import kr.ac.skuniv.realestate_batch.util.OpenApiContents;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


@Slf4j
//@Configuration
//@Component
//@StepScope
@RequiredArgsConstructor
public class RealestateItemReader implements ItemReader<> {

    public void readerTest() throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://openapi.molit.go.kr:8081/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTrade?_wadl&type=xml");
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=aT2H0hFpjEfGK6X0R5dQd2XeSV3SJiiepRsqtkjjkItc%2FO35c4yW1DvMZWDjTO2nv3Hpw%2FZGBVZEVEUwG8L70A%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("LAWD_CD","UTF-8") + "=" + URLEncoder.encode("11110", "UTF-8")); /*각 지역별 코드*/
        urlBuilder.append("&" + URLEncoder.encode("DEAL_YMD","UTF-8") + "=" + URLEncoder.encode("201512", "UTF-8")); /*월 단위 신고자료*/

    }
}
