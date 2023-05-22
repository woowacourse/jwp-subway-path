package subway.integration;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import subway.controller.dto.request.AddInitStationToLineRequest;
import subway.controller.dto.request.AddStationToLineRequest;
import subway.controller.dto.request.LineRequest;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/data-test.sql")
public class IntegrationTest {

    protected final LineRequest lineRequest1 = new LineRequest("신분당선", "bg-red-600", 1000L);
    protected final LineRequest lineRequest2 = new LineRequest("구신분당선", "bg-red-600", 900L);
    protected final Map<String, String> stationParam1 = Map.of("name", "강남역");
    protected final Map<String, String> stationParam2 = Map.of("name", "역삼역");
    protected final Map<String, String> stationParam3 = Map.of("name", "삼성역");


    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    protected ExtractableResponse<Response> saveLine1() {
        return given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(lineRequest1)
            .when().post("/lines")
            .then().log().all()
            .extract();
    }

    protected ExtractableResponse<Response> saveLine2() {
        return given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(lineRequest2)
            .when().post("/lines")
            .then().log().all()
            .extract();
    }

    protected ExtractableResponse<Response> saveStation1() {
        return given().log().all()
            .body(stationParam1)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all()
            .extract();
    }

    protected ExtractableResponse<Response> saveStation2() {
        return given().log().all()
            .body(stationParam2)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all()
            .extract();
    }

    protected ExtractableResponse<Response> saveStation3() {
        return given().log().all()
            .body(stationParam3)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all()
            .extract();
    }

    protected ExtractableResponse<Response> saveInitStationToLine(final AddInitStationToLineRequest request) {
        return given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/line/station/init")
            .then().log().all()
            .extract();
    }

    protected ExtractableResponse<Response> saveAdditionalStationToLine(final AddStationToLineRequest request) {
        return given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/line/station")
            .then().log().all()
            .extract();
    }
}
