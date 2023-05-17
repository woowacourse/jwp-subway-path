package subway.util;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;

public class Request {
    
    
    public static final String LINES = "/lines";
    public static final String SECTIONS = "/sections";
    public static final String STATIONS = "/stations";
    
    public static long postLineRequest(final LineRequest lineRequest) {
        return Long.parseLong(postRequestOnPath(lineRequest, LINES)
                .header("Location")
                .split("/")
                [2]);
    }
    
    public static long postSectionRequest(final Object sectionRequest) {
        return Long.parseLong(postRequestOnPath(sectionRequest, SECTIONS)
                .header("Location")
                .split("/")
                [2]);
    }
    
    public static long postStationRequest(final Object stationRequest) {
        return Long.parseLong(postRequestOnPath(stationRequest, STATIONS)
                .header("Location")
                .split("/")
                [2]);
    }
    
    public static ExtractableResponse<Response> postRequestOnPath(final Object request, final String path) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(path)
                .then().log().all()
                .extract();
    }
    
}
