package subway.utils;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.springframework.http.MediaType;
import subway.line.ui.dto.LineCreationRequest;
import subway.line.ui.dto.StationAdditionRequest;

import static io.restassured.RestAssured.given;
import static subway.utils.TestUtils.toJson;

public class Steps extends BaseTest {

    public static ValidatableResponse 노선을_만든다(LineCreationRequest lineCreationRequest) {
        return given().log()
                      .all()
                      .contentType(MediaType.APPLICATION_JSON_VALUE)
                      .body(toJson(lineCreationRequest))
                      .when()
                      .post(LINE_URL)
                      .then()
                      .log()
                      .all();
    }

    public static ValidatableResponse 노선을_찾는다(String resourceLocation) {
        return given().log()
                      .all()
                      .when()
                      .get(resourceLocation)
                      .then()
                      .log()
                      .all();
    }

    public static ValidatableResponse 모든_노선을_찾는다() {
        return given().log()
                      .all()
                      .when()
                      .get(LINE_URL)
                      .then()
                      .log()
                      .all();
    }

    public static ValidatableResponse 역을_추가한다(long lineId, StationAdditionRequest stationAdditionRequest) {
        return given().log()
                      .all()
                      .contentType(ContentType.JSON)
                      .body(toJson(stationAdditionRequest))
                      .when()
                      .post("/lines/" + lineId + "/stations")
                      .then()
                      .log()
                      .all();
    }

    public static ValidatableResponse 역을_삭제한다(long lineId, long stationId) {
        return given().log()
                      .all()
                      .when()
                      .delete("/lines/" + lineId + "/stations/" + stationId)
                      .then()
                      .log()
                      .all();
    }
}
