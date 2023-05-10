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
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static subway.utils.TestUtils.toJson;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class IntegrationTest {

    public static final String VALID_STATION_NAME = "서울대입구";
    private static final String VALID_LINE_NAME = "2호선";
    public static final String VALID_UPSTREAM_NAME = "잠실";
    private static final String VALID_DOWNSTREAM_NAME = "잠실나루";
    private static final int DISTANCE = 5;
    public static final String EMPTY_STATION_NAME = "";

    @LocalServerPort
    int port;

    @Autowired
    LineDao lineDao;

    @Autowired
    StationDao stationDao;

    @Autowired
    SectionDao sectionDao;

    private Line line;
    private Station upstream;
    private Station downstream;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;

        line = lineDao.insert(new Line(VALID_LINE_NAME));
        upstream = stationDao.insert(new Station(VALID_UPSTREAM_NAME));
        downstream = stationDao.insert(new Station(VALID_DOWNSTREAM_NAME));
        sectionDao.insert(new Section(upstream.getId(), downstream.getId(), line.getId(), DISTANCE));
    }

    @Test
    @DisplayName("/line/stations에 post 요청을 보내면 노선에 새로운 역을 추가할 수 있다.")
    void addStation() {
        given()
                .contentType(ContentType.JSON)
                .body(toJson(new AddStationRequest(VALID_STATION_NAME, line.getName(), upstream.getName(), downstream.getName(), DISTANCE - 1)))
                .log().all()
                .when()
                .post("/line/stations")
                .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @ParameterizedTest(name = "/line/stations에 post 요청 역 이름 길이가 맞지 않으면 역을 추가할 수 없다.")
    @ValueSource(strings = {"서", "서울대입구서울대16자이름입니다"})
    void addStationFail1(String invalidStationName) {
        given()
                .contentType(ContentType.JSON)
                .body(toJson(new AddStationRequest(invalidStationName, line.getName(), upstream.getName(), downstream.getName(), DISTANCE - 1)))
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

    @Test
    @DisplayName("/line/stations에 post 요청 노선이 등록되어 있지 않으면 역을 추가할 수 없다.")
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

    @Test
    @DisplayName("/line/stations에 post 상행역이 없으면 역을 추가할 수 없다.")
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

    @Test
    @DisplayName("/line/stations에 post 하행역이 없으면 역을 추가할 수 없다.")
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

    @Test
    @DisplayName("/line/stations에 post upstream-distance가 기존 상행역 하행역 간의 거리와 같거나 크면 추가할 수 없다.")
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

    @Test
    @DisplayName("/line/stations에 상행역과 하행역이 Section을 이루지 않는 경우 추가할 수 없다.")
    void addStationFail7() {
        final Station newStation = stationDao.insert(new Station("건대입구"));
        sectionDao.insert(new Section(downstream.getId(), newStation.getId(), line.getId(), DISTANCE));

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
            softly.assertThat(notConnectedSection.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            softly.assertThat(midToEndSection.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        });
    }

    @Test
    @DisplayName("/line/stations에 post station-name이 이미 Line에 존재하는 경우 추가할 수 없다.")
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
