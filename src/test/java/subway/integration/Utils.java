package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.dto.LineCreateRequest;
import subway.dto.StationAddRequest;
import subway.dto.StationCreateReqeust;

public class Utils {

    public static ExtractableResponse<Response> initLine(
            String lineName, String upLineStationName, String downLineStationName, int distance) {

        createLine(lineName);
        createStation(upLineStationName);
        createStation(downLineStationName);
        return addStation(lineName, upLineStationName, downLineStationName, distance);
    }

    public static ExtractableResponse<Response> createStation(String stationName) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new StationCreateReqeust(stationName))
                .when().post("/stations/station")
                .then().log().all().
                extract();
        return response;
    }

    public static ExtractableResponse<Response> createLine(String lineName) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new LineCreateRequest(lineName))
                .when().post("/lines")
                .then().log().all().
                extract();
        return response;
    }

    public static ExtractableResponse<Response> addStation(
            String lineName, String upLineStationName, String downLineStationName, int distance) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new StationAddRequest(lineName, upLineStationName, downLineStationName, distance))
                .when().post("/stations")
                .then().log().all().
                extract();
        return response;
    }


}
