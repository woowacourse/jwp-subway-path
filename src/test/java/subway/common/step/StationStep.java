package subway.common.step;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.dto.StationRequest;

import static io.restassured.RestAssured.given;

public class StationStep {

    public static ExtractableResponse<Response> createStation(final StationRequest stationRequest) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRequest)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    public static Long createStationAndGetId(final StationRequest stationRequest) {
        final ExtractableResponse<Response> response = createStation(stationRequest);
        final JsonPath jsonPath = response.jsonPath();
        return jsonPath.getLong("id");
    }
}
