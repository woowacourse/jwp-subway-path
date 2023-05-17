package subway.ui;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.dto.StationRequest;

import static io.restassured.RestAssured.given;

public class StationStep {

    public static Long createStation(final StationRequest stationRequest) {
        final ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRequest)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        final JsonPath jsonPath = response.jsonPath();
        return jsonPath.getLong("id");
    }
}
