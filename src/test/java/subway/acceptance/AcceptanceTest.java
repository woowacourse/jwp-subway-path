package subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import subway.dto.request.CreateSectionRequest;
import subway.dto.request.LineRequest;
import subway.dto.request.StationRequest;
import subway.dto.response.LineResponse;
import subway.dto.response.StationResponse;
import subway.fixture.LineFixture.이호선;
import subway.fixture.StationFixture.삼성역;
import subway.fixture.StationFixture.역삼역;
import subway.fixture.StationFixture.잠실역;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
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

    private StationResponse addStation(final StationRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/stations")
                .then().log().all()
                .extract().as(StationResponse.class);
    }

    private LineResponse addLine(final LineRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/lines")
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
                .then().log().all()
                .extract().as(StationResponse.class);
    }
}
