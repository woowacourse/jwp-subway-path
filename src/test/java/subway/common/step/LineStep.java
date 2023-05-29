package subway.common.step;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;
import subway.dto.StationAddRequest;

import static io.restassured.RestAssured.given;

public class LineStep {

    public static ExtractableResponse<Response> createLine(final LineRequest lineRequest) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    public static Long createLineAndGetId(final LineRequest lineRequest) {
        final ExtractableResponse<Response> response = createLine(lineRequest);
        final JsonPath jsonPath = response.jsonPath();
        return jsonPath.getLong("id");
    }

    public static ExtractableResponse<Response> addStationToLine(final StationAddRequest stationAddRequest, final Long lineId) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("lineId", lineId)
                .body(stationAddRequest)
                .when()
                .post("/lines/{lineId}/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> findStationsByLineId(final Long id) {
        return given().log().all()
                .pathParam("lineId", id)
                .when()
                .get("/lines/{lineId}")
                .then().log().all()
                .extract();
    }
}
