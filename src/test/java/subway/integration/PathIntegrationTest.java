package subway.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.LineDao;
import subway.domain.Line;
import subway.domain.LineName;
import subway.domain.Station;
import subway.dto.SubwayPathRequest;
import subway.entity.LineEntity;
import subway.repository.SubwayRepository;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static subway.utils.LineFixture.LINE_NUMBER_TWO;
import static subway.utils.SectionFixture.JAMSIL_TO_JAMSILNARU;
import static subway.utils.StationFixture.*;

@SuppressWarnings("NonAsciiCharacters")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/schema.sql")
public class PathIntegrationTest {

    private static final String VALID_LINE_NAME = "2호선";

    @LocalServerPort
    int port;

    @Autowired
    SubwayRepository subwayRepository;

    @Autowired
    LineDao lineDao;

    private Line line;
    private Station upstream;
    private Station downstream;
    private long 잠실역_아이디;
    private long 잠실나루역_아이디;
    private long 노선_아이디;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        upstream = JAMSIL_STATION;
        downstream = JAMSIL_NARU_STATION;
        line = new Line(new LineName(VALID_LINE_NAME), List.of(JAMSIL_TO_JAMSILNARU));

        lineDao.insert(new LineEntity.Builder().name(VALID_LINE_NAME).build());
        잠실역_아이디 = subwayRepository.addStation(upstream);
        잠실나루역_아이디 = subwayRepository.addStation(downstream);
        노선_아이디 = subwayRepository.updateLine(line);
    }

    /**
     * 2호선 (LINE_NUMBER_TWO)
     * 잠실 --(5)-->  선릉 --(5)--> 잠실나루
     * JAMSIL : Station id = 1
     * JAMSIL_NARU : Station id = 2
     * SULLENG : Station id = 3
     */
    @DisplayName("/line/paths에 get 출발역과 도착역을 전달 받아 최단 경로를 전달한다.")
    @Test
    void findShortestPath() {
        // given
        long 선릉역_아이디 = subwayRepository.addStation(SULLEUNG_STATION);
        line.addStation(SULLEUNG_STATION, JAMSIL_STATION, JAMSIL_NARU_STATION, 3);
        subwayRepository.updateLine(line);

        given().log().all()
                .contentType(ContentType.JSON)
                .body(new SubwayPathRequest(잠실역_아이디, 잠실나루역_아이디))
                .when()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/paths")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("stations", containsInAnyOrder("잠실", "선릉", "잠실나루"))
                .body("distance", equalTo(5.0F));
    }

    @DisplayName("/line/paths에 get 출발역과 도착역을 잘못 입력하는 경우 예외가 발생한다.")
    @Test
    void findWrongShortestPath() {
        // given
        subwayRepository.addStation(SULLEUNG_STATION);
        subwayRepository.updateLine(LINE_NUMBER_TWO);

        long 선릉역_아이디 = 0L;
        long 잠실나루역_아이디 = 0L;

        given().log().all()
                .contentType(ContentType.JSON)
                .body(new SubwayPathRequest(선릉역_아이디, 잠실나루역_아이디))
                .when()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/paths")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
