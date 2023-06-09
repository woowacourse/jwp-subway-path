package subway.integration.step;

import io.restassured.RestAssured;
import org.springframework.http.MediaType;
import subway.dto.request.StationCreateRequest;

@SuppressWarnings("NonAsciiCharacters")
public class StationStep {
    public static void 역을_생성한다(StationCreateRequest stationCreateRequest) {
        RestAssured
                .given().log().all()
                .body(stationCreateRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }
}
