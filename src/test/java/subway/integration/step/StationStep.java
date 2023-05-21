package subway.integration.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.dto.StationCreateRequest;

@SuppressWarnings("NonAsciiCharacters")
public class StationStep {
    public static Long 역을_생성한다(StationCreateRequest stationCreateRequest) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(stationCreateRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        String location = response.header("Location");
        return Long.parseLong(location.substring(location.lastIndexOf("/") + 1));
    }
}
