package subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dto.request.CreateSectionRequest;
import subway.dto.request.LineRequest;
import subway.dto.request.RouteRequest;
import subway.dto.request.StationRequest;
import subway.dto.response.LineResponse;
import subway.dto.response.RouteResponse;
import subway.dto.response.StationResponse;
import subway.fixture.LineFixture;
import subway.fixture.LineFixture.이호선;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.fixture.LineFixture.신분당선;
import static subway.fixture.StationFixture.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @BeforeEach
    void clear() {
        jdbcTemplate.update("TRUNCATE TABLE station");
        jdbcTemplate.update("TRUNCATE TABLE line");
        jdbcTemplate.update("TRUNCATE TABLE section");
    }

    @Test
    void 노선에_역_등록_시나리오_테스트() {
        // 역을 등록한다.
        StationResponse stationResponse1 = addStation(역삼역.REQUEST);
        StationResponse stationResponse2 = addStation(삼성역.REQUEST);
        StationResponse stationResponse3 = addStation(잠실역.REQUEST);

        // 노선을 등록한다.
        LineResponse lineResponse = addLine(이호선.REQUEST);

        // 노선에 역을 등록한다.
        CreateSectionRequest createSectionRequest1 = new CreateSectionRequest(stationResponse1.getId(),
                stationResponse3.getId(), 5);
        CreateSectionRequest createSectionRequest2 = new CreateSectionRequest(stationResponse2.getId(),
                stationResponse3.getId(), 2);

        addSection(createSectionRequest1, lineResponse.getId());
        addSection(createSectionRequest2, lineResponse.getId());

        // 노선을 조회한다.
        LineResponse resultResponse = getLine(lineResponse.getId());

        assertThat(resultResponse.getStations())
                .usingRecursiveComparison()
                .isEqualTo(List.of(stationResponse1, stationResponse2, stationResponse3));
    }

    @Test
    void 노선에_역_제거_시나리오_테스트() {
        // 역을 등록한다.
        StationResponse stationResponse1 = addStation(역삼역.REQUEST);
        StationResponse stationResponse2 = addStation(삼성역.REQUEST);
        StationResponse stationResponse3 = addStation(잠실역.REQUEST);

        // 노선을 등록한다.
        LineResponse lineResponse = addLine(이호선.REQUEST);

        // 노선에 역을 등록한다.
        CreateSectionRequest createSectionRequest1 = new CreateSectionRequest(stationResponse1.getId(),
                stationResponse3.getId(), 5);
        CreateSectionRequest createSectionRequest2 = new CreateSectionRequest(stationResponse2.getId(),
                stationResponse3.getId(), 2);

        addSection(createSectionRequest1, lineResponse.getId());
        addSection(createSectionRequest2, lineResponse.getId());

        // 노선에 역을 제거한다.
        deleteStationFromLine(lineResponse.getId(), stationResponse2.getId());

        // 노선을 조회한다.
        LineResponse resultResponse = getLine(lineResponse.getId());

        assertThat(resultResponse.getStations())
                .usingRecursiveComparison()
                .isEqualTo(List.of(stationResponse1, stationResponse3));
    }

    @Test
    void 노선에_역이_두개일_떄_역_제거_시나리오_테스트() {
        // 역을 등록한다.
        StationResponse stationResponse1 = addStation(역삼역.REQUEST);
        StationResponse stationResponse2 = addStation(삼성역.REQUEST);

        // 노선을 등록한다.
        LineResponse lineResponse = addLine(이호선.REQUEST);

        // 노선에 역을 등록한다.
        CreateSectionRequest createSectionRequest = new CreateSectionRequest(stationResponse1.getId(),
                stationResponse2.getId(), 5);

        addSection(createSectionRequest, lineResponse.getId());

        // 노선에 역을 제거한다.
        deleteStationFromLine(lineResponse.getId(), stationResponse2.getId());

        // 노선을 조회한다.
        LineResponse resultResponse = getLine(lineResponse.getId());

        assertThat(resultResponse.getStations()).isEmpty();
    }

    @Test
    void 역에서_역으로_경로를_구한다() {
        // 역을 등록한다.
        StationResponse 강남 = addStation(강남역.REQUEST);
        StationResponse 신논현 = addStation(신논현역.REQUEST);
        StationResponse 종합운동장 = addStation(종합운동장역.REQUEST);

        // 노선을 등록한다.
        LineResponse 이호선 = addLine(LineFixture.이호선.REQUEST);
        LineResponse 신분당 = addLine(신분당선.REQUEST);
        LineResponse 구호선 = addLine(LineFixture.구호선.REQUEST);

        // 노선에 역을 등록한다.
        CreateSectionRequest 강남_신논현_1 = new CreateSectionRequest(강남.getId(), 신논현.getId(), 1);
        addSection(강남_신논현_1, 신분당.getId());

        CreateSectionRequest 신논현_종합운동장_1 = new CreateSectionRequest(신논현.getId(), 종합운동장.getId(), 1);
        addSection(신논현_종합운동장_1, 구호선.getId());

        CreateSectionRequest 강남_종합운동장_5 = new CreateSectionRequest(강남.getId(), 종합운동장.getId(), 5);
        addSection(강남_종합운동장_5, 이호선.getId());

        // 최단경로를 구한다.
        RouteRequest request = new RouteRequest(강남.getId(), 종합운동장.getId());

        RouteResponse routeResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().get("/stations/shortest-route")
                .then().log().all()
                .extract().as(RouteResponse.class);

        assertThat(routeResponse.getDistance()).isEqualTo(2);
        assertThat(routeResponse.getFare()).isEqualTo(1250);
        assertThat(routeResponse.getStations())
                .usingRecursiveComparison()
                .isEqualTo(List.of(강남, 신논현, 종합운동장));
    }

    private StationResponse addStation(final StationRequest request) {
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/stations")
                .then().log().all()
                .extract();

        String uri = createResponse.header("Location");

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all()
                .extract().as(StationResponse.class);
    }

    private LineResponse addLine(final LineRequest request) {
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/lines")
                .then().log().all()
                .extract();

        String uri = createResponse.header("Location");

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all()
                .extract().as(LineResponse.class);
    }


    private void addSection(final CreateSectionRequest request, final long lineId) {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/lines/" + lineId + "/stations")
                .then().log().all();
    }

    private LineResponse getLine(final long lineId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/" + lineId)
                .then().log().all()
                .extract().as(LineResponse.class);
    }

    private void deleteStationFromLine(final long lineId, final long stationId) {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/" + lineId + "/stations/" + stationId)
                .then().log().all();
    }
}
