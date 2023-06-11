package subway.fixture;

import io.restassured.RestAssured;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;
import subway.dto.SectionRequest;
import subway.dto.StationRequest;

public class IntegrationPostRequestFixture {

    public static void addLineRequest(final LineRequest line) {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(line)
                .when().post("/lines")
                .then().log().all();
    }

    public static void addStationRequest(final StationRequest stationRequest) {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRequest)
                .when().post("/stations")
                .then().log().all();
    }

    public static void addSectionRequest(final SectionRequest sectionRequest) {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/sections")
                .then().log().all();
    }
}
