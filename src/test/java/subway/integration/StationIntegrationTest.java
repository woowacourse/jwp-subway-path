package subway.integration;

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
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.LineDao;
import subway.domain.Line;
import subway.domain.LineName;
import subway.domain.Station;
import subway.dto.AddStationRequest;
import subway.entity.LineEntity;
import subway.repository.SubwayRepository;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.hamcrest.Matchers.containsString;
import static subway.domain.Line.EMPTY_ENDPOINT_STATION;
import static subway.utils.SectionFixture.JAMSIL_TO_JAMSILNARU;
import static subway.utils.StationFixture.JAMSIL_NARU_STATION;
import static subway.utils.StationFixture.JAMSIL_STATION;
import static subway.utils.TestUtils.toJson;

@SuppressWarnings("NonAsciiCharacters")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/schema.sql")
public class StationIntegrationTest {

    public static final String VALID_STATION_NAME = "서울대입구";
    public static final String VALID_UPSTREAM_NAME = "잠실";
    public static final String EMPTY_STATION_NAME = "";
    protected static final String SET_UP_LINE_NAME = "2호선";
    private static final String VALID_LINE_NAME = "2호선";
    private static final String VALID_DOWNSTREAM_NAME = "잠실나루";
    private static final int DISTANCE = 5;

    @LocalServerPort
    int port;

    @Autowired
    SubwayRepository subwayRepository;

    @Autowired
    LineDao lineDao;

    private Line line;
    private Station upstream;
    private Station downstream;
    private long upstreamId;
    private long downstreamId;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        upstream = JAMSIL_STATION;
        downstream = JAMSIL_NARU_STATION;
        line = new Line(new LineName(VALID_LINE_NAME), List.of(JAMSIL_TO_JAMSILNARU));

        lineDao.insert(new LineEntity.Builder().name(VALID_LINE_NAME).build());
        upstreamId = subwayRepository.addStation(upstream);
        downstreamId = subwayRepository.addStation(downstream);
        subwayRepository.updateLine(line);
    }

    @Test
    @DisplayName("/line/stations에 post 요청을 보내면 노선에 새로운 역을 추가할 수 있다.")
    void addStation() {
        given()
                .contentType(ContentType.JSON)
                .body(toJson(new AddStationRequest(VALID_STATION_NAME, line.getName().getName(), upstream.getName(), downstream.getName(), DISTANCE - 1)))
                .log().all()
                .when()
                .post("/line/stations")
                .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("location", containsString("/line/stations/"));
    }

    @ParameterizedTest(name = "/line/stations에 post 요청 역 이름 길이가 맞지 않으면 역을 추가할 수 없다.")
    @ValueSource(strings = {"서", "서울대입구서울대16자이름입니다"})
    void addStationFail1(String invalidStationName) {
        given()
                .contentType(ContentType.JSON)
                .body(toJson(new AddStationRequest(invalidStationName, line.getName().getName(), upstream.getName(), downstream.getName(), DISTANCE - 1)))
                .log().all()
                .when()
                .post("/line/stations")
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @ParameterizedTest(name = "/line/stations에 post 요청 노선 이름 길이가 맞지 않으면 역을 추가할 수 없다.")
    @ValueSource(strings = {"2", "2호선16자길이의노선이름입니다"})
    void addStationFail2(String invalidLineName) {
        given()
                .contentType(ContentType.JSON)
                .body(toJson(new AddStationRequest(VALID_STATION_NAME, invalidLineName, upstream.getName(), downstream.getName(), DISTANCE - 1)))
                .log().all()
                .when()
                .post("/line/stations")
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("/line/stations에 post 요청 노선이 등록되어 있지 않으면 역을 추가할 수 없다.")
    @Test
    void addStationFail3() {
        final String invalidLineName = "10호선";

        given()
                .contentType(ContentType.JSON)
                .body(toJson(new AddStationRequest(VALID_STATION_NAME, invalidLineName, upstream.getName(), downstream.getName(), DISTANCE - 1)))
                .log().all()
                .when()
                .post("/line/stations")
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("/line/stations에 post 상행역이 없으면 역을 추가할 수 없다.")
    @Test
    void addStationFail4() {
        given()
                .contentType(ContentType.JSON)
                .body(toJson(new AddStationRequest(VALID_STATION_NAME, VALID_LINE_NAME, "등록되지 않은 상행역", VALID_DOWNSTREAM_NAME, DISTANCE - 1)))
                .log().all()
                .when()
                .post("/line/stations")
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("/line/stations에 post 하행역이 없으면 역을 추가할 수 없다.")
    @Test
    void addStationFail5() {
        given()
                .contentType(ContentType.JSON)
                .body(toJson(new AddStationRequest(VALID_STATION_NAME, VALID_LINE_NAME, VALID_UPSTREAM_NAME, "등록되지않은하행역", DISTANCE - 1)))
                .log().all()
                .when()
                .post("/line/stations")
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("/line/stations에 post upstream-distance가 기존 상행역 하행역 간의 거리와 같거나 크면 추가할 수 없다.")
    @Test
    void addStationFail6() {
        given()
                .contentType(ContentType.JSON)
                .body(toJson(new AddStationRequest(VALID_STATION_NAME, VALID_LINE_NAME, VALID_UPSTREAM_NAME, VALID_DOWNSTREAM_NAME, DISTANCE)))
                .log().all()
                .when()
                .post("/line/stations")
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("/line/stations에 상행역과 하행역이 Section을 이루지 않는 경우 추가할 수 없다.")
    @Test
    void addStationFail7() {
        Line newLine = new Line(line);
        Station newStation = new Station("건대입구");
        newLine.addStation(newStation, downstream, EMPTY_ENDPOINT_STATION, DISTANCE);
        subwayRepository.addStation(newStation);
        subwayRepository.updateLine(newLine);

        ExtractableResponse<Response> notConnectedSection = given()
                .contentType(ContentType.JSON)
                .body(toJson(new AddStationRequest(VALID_STATION_NAME, VALID_LINE_NAME, VALID_UPSTREAM_NAME, newStation.getName(), DISTANCE - 1)))
                .log().all()
                .when()
                .post("/line/stations")
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract();

        ExtractableResponse<Response> midToEndSection = given()
                .contentType(ContentType.JSON)
                .body(toJson(new AddStationRequest(VALID_STATION_NAME, VALID_LINE_NAME, VALID_UPSTREAM_NAME, EMPTY_STATION_NAME, DISTANCE - 1)))
                .log().all()
                .when()
                .post("/line/stations")
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract();

        assertSoftly(softly -> {
            softly.assertThat(notConnectedSection.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            softly.assertThat(midToEndSection.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        });
    }

    @DisplayName("/line/stations에 post station-name이 이미 Line에 존재하는 경우 추가할 수 없다.")
    @Test
    void addStationFail8() {
        given()
                .contentType(ContentType.JSON)
                .body(toJson(new AddStationRequest(VALID_UPSTREAM_NAME, VALID_LINE_NAME, VALID_UPSTREAM_NAME, VALID_DOWNSTREAM_NAME, DISTANCE - 1)))
                .log().all()
                .when()
                .post("/line/stations")
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
