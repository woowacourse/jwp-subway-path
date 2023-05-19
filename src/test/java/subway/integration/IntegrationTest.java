package subway.integration;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.Station;
import subway.dto.LineResponse;
import subway.dto.PathResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = {"classpath:truncate.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class IntegrationTest {
    @LocalServerPort
    int port;

    public static RequestSpecification given() {
        return RestAssured.given().log().all();
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    protected void createStation(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/stations").
                then().
                log().all().
                statusCode(HttpStatus.CREATED.value());
    }

    protected void createLine(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/lines").
                then().
                log().all().
                statusCode(HttpStatus.CREATED.value());
    }


    protected void createLineStation(Long id, String preStationId, String stationId, String distance) {
        Map<String, String> params = new HashMap<>();
        params.put("lineId", String.valueOf(id));
        params.put("preStationId", preStationId);
        params.put("stationId", stationId);
        params.put("distance", distance);

        given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/lines/" + id + "/stations").
                then().
                log().all().
                statusCode(HttpStatus.CREATED.value());
    }

    protected List<LineResponse> getLines() {
        return
                given().
                        when().
                        get("/lines").
                        then().
                        log().all().
                        extract().
                        jsonPath().getList(".", LineResponse.class);
    }

    protected LineResponse getLine(Long id) {
        return given().when().
                get("/lines/" + id).
                then().
                log().all().
                extract().as(LineResponse.class);
    }

    protected List<Station> getLineStations(Long id) {
        return given().
                when().
                get("/lines/" + id + "/stations").
                then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                extract().
                jsonPath().getList("stations", Station.class);
    }

    protected void deleteLineStation(Long lineId, Long stationId) {
        given().
                when().
                delete("/lines/" + lineId + "/stations/" + stationId).
                then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value());
    }

    protected PathResponse getShortestPath(Long departureStationId, Long arrivalStationId) {
        return given().
                when().
                get("/path?departure={departure}&arrival={arrival}", departureStationId, arrivalStationId).
                then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                extract().as(PathResponse.class);
    }
}
