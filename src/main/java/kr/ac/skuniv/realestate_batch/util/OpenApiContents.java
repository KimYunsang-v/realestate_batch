package kr.ac.skuniv.realestate_batch.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

public final class OpenApiContents {
    public static String APART = "apart";
    public static String OFFICETELS = "officetel";
    public static String HOUSING = "house";

    public static String BARGAIN_NUM = "1";
    public static String CHARTER_RENT_NUM = "2";

    @Getter
    @AllArgsConstructor
    public enum OpenApiRequest {
        APART_BARGAIN("http://openapi.molit.go.kr:8081/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTrade?_wadl&type=xml"),
        APART_CHARTER_RENT("http://openapi.molit.go.kr:8081/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptRent?_wadl&type=xml"),
        OFFICETELS_BARGAIN("http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcOffiTrade?_wadl&type=xml"),
        OFFICETELS_CHARTER_RENT("http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcOffiRent?_wadl&type=xml"),
        MUTILGENERATION_BARGAIN("http://openapi.molit.go.kr:8081/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcSHTrade?_wadl&type=xml"),
        MUTILGENERATION_CHARTER_RENT("http://openapi.molit.go.kr:8081/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcSHRent?_wadl&type=xml");

        private String url;
    };


}
