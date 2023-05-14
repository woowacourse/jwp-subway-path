package subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import subway.adapter.in.web.line.dto.CreateLineRequest;
import subway.adapter.in.web.section.dto.AddStationToLineRequest;
import subway.adapter.in.web.station.dto.CreateStationRequest;
import subway.application.port.in.line.dto.response.LineQueryResponse;
import subway.application.port.in.station.dto.response.StationQueryResponse;
import subway.fixture.LineFixture.이호선;
import subway.fixture.StationFixture.삼성역;
import subway.fixture.StationFixture.역삼역;
import subway.fixture.StationFixture.잠실역;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void clear() {
        jdbcTemplate.update("TRUNCATE TABLE station");
        jdbcTemplate.update("TRUNCATE TABLE line");
        jdbcTemplate.update("TRUNCATE TABLE section");
    }

    @Test
    void 노선에_역_등록_시나리오_테스트() {
        // 역을 등록한다.
        StationQueryResponse stationResponse1 = addStation(역삼역.REQUEST);
        StationQueryResponse stationResponse2 = addStation(삼성역.REQUEST);
        StationQueryResponse stationResponse3 = addStation(잠실역.REQUEST);

        // 노선을 등록한다.
        LineQueryResponse lineResponse = addLine(이호선.REQUEST);

        // 노선에 역을 등록한다.
        AddStationToLineRequest addStationToLineRequest1 = new AddStationToLineRequest(stationResponse1.getId(),
                stationResponse3.getId(), 5);
        AddStationToLineRequest addStationToLineRequest2 = new AddStationToLineRequest(stationResponse2.getId(),
                stationResponse3.getId(), 2);

        addSection(addStationToLineRequest1, lineResponse.getId());
        addSection(addStationToLineRequest2, lineResponse.getId());

        // 노선을 조회한다.
        LineQueryResponse resultResponse = findLine(lineResponse.getId());

        assertThat(resultResponse.getStations())
                .usingRecursiveComparison()
                .isEqualTo(List.of(stationResponse1, stationResponse2, stationResponse3));
    }

    @Test
    void 노선에서_역_제거_시나리오_테스트() {
        // 역을 등록한다.
        StationQueryResponse stationResponse1 = addStation(역삼역.REQUEST);
        StationQueryResponse stationResponse2 = addStation(삼성역.REQUEST);
        StationQueryResponse stationResponse3 = addStation(잠실역.REQUEST);

        // 노선을 등록한다.
        LineQueryResponse lineQueryResponse = addLine(이호선.REQUEST);

        // 노선에 역을 등록한다.
        AddStationToLineRequest addStationToLineRequest1 = new AddStationToLineRequest(stationResponse1.getId(),
                stationResponse3.getId(), 5);
        AddStationToLineRequest addStationToLineRequest2 = new AddStationToLineRequest(stationResponse2.getId(),
                stationResponse3.getId(), 2);

        addSection(addStationToLineRequest1, lineQueryResponse.getId());
        addSection(addStationToLineRequest2, lineQueryResponse.getId());

        // 노선에 역을 제거한다.
        deleteStationFromLine(lineQueryResponse.getId(), stationResponse2.getId());

        // 노선을 조회한다.
        LineQueryResponse resultResponse = findLine(lineQueryResponse.getId());

        assertThat(resultResponse.getStations())
                .usingRecursiveComparison()
                .isEqualTo(List.of(stationResponse1, stationResponse3));
    }

    @Test
    void 노선에_역이_두개일_떄_역_제거_시나리오_테스트() {
        // 역을 등록한다.
        StationQueryResponse stationResponse1 = addStation(역삼역.REQUEST);
        StationQueryResponse stationResponse2 = addStation(삼성역.REQUEST);

        // 노선을 등록한다.
        LineQueryResponse lineQueryResponse = addLine(이호선.REQUEST);

        // 노선에 역을 등록한다.
        AddStationToLineRequest addStationToLineRequest = new AddStationToLineRequest(stationResponse1.getId(),
                stationResponse2.getId(), 5);

        addSection(addStationToLineRequest, lineQueryResponse.getId());

        // 노선에 역을 제거한다.
        deleteStationFromLine(lineQueryResponse.getId(), stationResponse2.getId());

        // 노선을 조회한다.
        LineQueryResponse resultResponse = findLine(lineQueryResponse.getId());

        assertThat(resultResponse.getStations()).isEmpty();
    }

    private StationQueryResponse addStation(final CreateStationRequest request) {
        String uri = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/stations")
                .then().log().all()
                .extract().header("location");

        return findStationByUri(uri);
    }

    private StationQueryResponse findStationByUri(final String uri) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all()
                .extract().as(StationQueryResponse.class);
    }

    private void addSection(final AddStationToLineRequest request, final long lineId) {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/lines/" + lineId + "/stations")
                .then().log().all();
    }

    private LineQueryResponse addLine(final CreateLineRequest request) {
        String uri = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/lines")
                .then().log().all()
                .extract().header("location");

        return findLineByUri(uri);
    }

    private LineQueryResponse findLineByUri(final String uri) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all()
                .extract().as(LineQueryResponse.class);
    }

    private LineQueryResponse findLine(final long lineId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/" + lineId)
                .then().log().all()
                .extract().as(LineQueryResponse.class);
    }

    private void deleteStationFromLine(final long lineId, final long stationId) {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/" + lineId + "/stations/" + stationId)
                .then().log().all();
    }
}
