package subway.station.ui;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import subway.line.domain.Line;
import subway.line.repository.LineRepository;
import subway.line.ui.dto.StationAdditionRequest;
import subway.station.domain.DummyTerminalStation;
import subway.station.domain.Station;
import subway.station.repository.StationRepository;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static subway.utils.SectionFixture.JAMSIL_TO_JAMSILNARU;
import static subway.utils.StationFixture.JAMSIL_NARU_STATION;
import static subway.utils.StationFixture.JAMSIL_STATION;
import static subway.utils.TestUtils.toJson;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/schema.sql")
public class StationIntegrationTest {

    public static final String VALID_STATION_NAME = "서울대입구";
    public static final String VALID_UPSTREAM_NAME = "잠실";
    protected static final String SET_UP_LINE_NAME = "2호선";
    protected static final String VALID_LINE_NAME = "2호선";
    protected static final String VALID_DOWNSTREAM_NAME = "잠실나루";
    protected static final int DISTANCE = 5;

    @LocalServerPort
    int port;

    @Autowired
    LineRepository lineRepository;

    @Autowired
    StationRepository stationRepository;

    protected Line line;
    protected Station upstream;
    protected Station downstream;
    protected long lineId;
    protected long upstreamId;
    protected long downstreamId;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        upstream = JAMSIL_STATION;
        downstream = JAMSIL_NARU_STATION;
        line = new Line(VALID_LINE_NAME, 0, List.of(JAMSIL_TO_JAMSILNARU));

        upstreamId = stationRepository.createStation(upstream).getId();
        downstreamId = stationRepository.createStation(downstream).getId();
        lineId = lineRepository.createLine(line);
    }

    @Test
    @DisplayName("노선에 새로운 역을 추가할 수 있다.")
    void addStationSuccess() {
        given()
                .contentType(ContentType.JSON)
                .body(toJson(new StationAdditionRequest(VALID_STATION_NAME, upstream.getName(), downstream.getName(), DISTANCE - 1)))
                .log().all()
                .when()
                .post("/lines/" + lineId + "/stations")
                .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @ParameterizedTest(name = "역 이름 길이가 맞지 않으면 역을 추가할 수 없다.")
    @ValueSource(strings = {"서", "서울대입구서울대16자이름입니다"})
    void addStationFail1(String invalidStationName) {
        given()
                .contentType(ContentType.JSON)
                .body(toJson(new StationAdditionRequest(invalidStationName, upstream.getName(), downstream.getName(), DISTANCE - 1)))
                .log().all()
                .when()
                .post("/lines/" + lineId + "/stations")
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("노선 아이디가 존재하지 않으면 않으면 역을 추가할 수 없다.")
    void addStationFail2() {
        given()
                .contentType(ContentType.JSON)
                .body(toJson(new StationAdditionRequest(VALID_STATION_NAME, upstream.getName(), downstream.getName(), DISTANCE - 1)))
                .log().all()
                .when()
                .post("/lines/" + 0 + "/stations")
                .then()
                .log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("상행역이 없으면 역을 추가할 수 없다.")
    void addStationFail3() {
        given()
                .contentType(ContentType.JSON)
                .body(toJson(new StationAdditionRequest(VALID_STATION_NAME, "등록되지 않은 상행역", VALID_DOWNSTREAM_NAME, DISTANCE - 1)))
                .log().all()
                .when()
                .post("/lines/" + lineId + "/stations")
                .then()
                .log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("하행역이 없으면 역을 추가할 수 없다.")
    void addStationFail4() {
        given()
                .contentType(ContentType.JSON)
                .body(toJson(new StationAdditionRequest(VALID_STATION_NAME, VALID_UPSTREAM_NAME, "등록되지않은하행역", DISTANCE - 1)))
                .log().all()
                .when()
                .post("/lines/" + lineId + "/stations")
                .then()
                .log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("upstream-distance가 기존 상행역 하행역 간의 거리와 같거나 크면 추가할 수 없다.")
    void addStationFail5() {
        given()
                .contentType(ContentType.JSON)
                .body(toJson(new StationAdditionRequest(VALID_STATION_NAME, VALID_UPSTREAM_NAME, VALID_DOWNSTREAM_NAME, DISTANCE)))
                .log().all()
                .when()
                .post("/lines/" + lineId + "/stations")
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("상행역과 하행역이 Section을 이루지 않는 경우 추가할 수 없다.")
    void addStationFail6() {
        Line newLine = new Line(line);
        Station newStation = new Station(3L, "건대입구");
        newLine.addStation(newStation, downstream, DummyTerminalStation.getInstance(), DISTANCE);
        stationRepository.createStation(newStation);
        lineRepository.updateLine(newLine);

        ExtractableResponse<Response> notConnectedSection = given()
                .contentType(ContentType.JSON)
                .body(toJson(new StationAdditionRequest(VALID_STATION_NAME, VALID_UPSTREAM_NAME, newStation.getName(), DISTANCE - 1)))
                .log().all()
                .when()
                .post("/lines/" + lineId + "/stations")
                .then()
                .log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract();

        ExtractableResponse<Response> midToEndSection = given()
                .contentType(ContentType.JSON)
                .body(toJson(new StationAdditionRequest(VALID_STATION_NAME, VALID_UPSTREAM_NAME, DummyTerminalStation.STATION_NAME, DISTANCE - 1)))
                .log().all()
                .when()
                .post("/lines/" + lineId + "/stations")
                .then()
                .log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract();

        assertSoftly(softly -> {
            assertThat(notConnectedSection.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
            assertThat(midToEndSection.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        });
    }

    @Test
    @DisplayName("station-name이 이미 Line에 존재하는 경우 추가할 수 없다.")
    void addStationFail7() {
        given()
                .contentType(ContentType.JSON)
                .body(toJson(new StationAdditionRequest(VALID_UPSTREAM_NAME, VALID_UPSTREAM_NAME, VALID_DOWNSTREAM_NAME, DISTANCE - 1)))
                .log().all()
                .when()
                .post("/lines/" + lineId + "/stations")
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("역이 두 개인 경우 노선을 삭제할 수 있다.")
    void deleteStationSuccess1() {
        given()
                .log().all()
                .when()
                .delete("/lines/" + lineId + "/stations/" + upstreamId)
                .then()
                .log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(lineRepository.findLineById(lineId)).isEmpty();
    }

    @Test
    @DisplayName("역이 두 개인 경우 노선을 삭제할 수 있다.")
    void deleteStationSuccess2() {
        given()
                .log().all()
                .when()
                .delete("/lines/" + lineId + "/stations/" + downstreamId)
                .then()
                .log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(lineRepository.findLineById(lineId)).isEmpty();
    }

    @Test
    @DisplayName("역 id가 없는 경우 NOT_FOUND 반환한다")
    void deleteStationFail1() {
        given()
                .log().all()
                .when()
                .delete("/lines/" + lineId + "/stations/" + 0)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("노선 id가 없는 경우 NOT_FOUND 반환한다")
    void deleteStationFail2() {
        given()
                .log().all()
                .when()
                .delete("/lines/" + 0 + "/stations/" + upstreamId)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());

        assertThat(lineRepository.findLineById(lineId)).isPresent();
    }
}
