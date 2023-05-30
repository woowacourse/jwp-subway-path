package subway.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import subway.adapter.in.web.line.dto.CreateLineRequest;
import subway.adapter.in.web.section.dto.AddStationToLineRequest;
import subway.adapter.in.web.station.dto.CreateStationRequest;
import subway.application.port.in.line.dto.response.LineQueryResponse;
import subway.application.port.in.route.dto.response.RouteQueryResponse;
import subway.application.port.in.station.dto.response.StationQueryResponse;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class IntegrationTest {

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }


    protected StationQueryResponse addStation(final CreateStationRequest request) {
        String uri = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/stations")
                .then().log().all()
                .extract().header("location");

        return findStationByUri(uri);
    }

    protected StationQueryResponse findStationByUri(final String uri) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all()
                .extract().as(StationQueryResponse.class);
    }

    protected void addSection(final AddStationToLineRequest request, final long lineId) {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/lines/{lineId}/stations", lineId)
                .then().log().all();
    }

    protected LineQueryResponse addLine(final CreateLineRequest request) {
        String uri = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/lines")
                .then().log().all()
                .extract().header("location");

        return findLineByUri(uri);
    }

    protected LineQueryResponse findLineByUri(final String uri) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all()
                .extract().as(LineQueryResponse.class);
    }

    protected LineQueryResponse findLine(final long lineId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", lineId)
                .then().log().all()
                .extract().as(LineQueryResponse.class);
    }

    protected void removeStationFromLine(final long lineId, final long stationId) {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{lineId}/stations/{stationId}", lineId, stationId)
                .then().log().all();
    }

    protected void deleteStation(final long stationId) {
        RestAssured
                .given().log().all()
                .when()
                .delete("/stations/{stationId}", stationId)
                .then().log().all();
    }

    protected RouteQueryResponse findRoute(final long sourceStationId, final long targetStationId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("sourceStationId", sourceStationId)
                .queryParam("targetStationId", targetStationId)
                .when().get("/route")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(RouteQueryResponse.class);
    }

    protected RouteQueryResponse findRoute(final long sourceStationId, final long targetStationId, final int age) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("sourceStationId", sourceStationId)
                .queryParam("targetStationId", targetStationId)
                .queryParam("age", age)
                .when().get("/route")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(RouteQueryResponse.class);
    }
}
