package subway.integration;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.AddLineRequest;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static subway.utils.TestUtils.toJson;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/schema.sql")
public class LineStationIntegrationTest extends StationIntegrationTest {

    private static final String LINE_URL = "/lines";
    private static final String VALID_LINE_NAME = "9호선";
    private static final String VALID_UPSTREAM_NAME = "잠실";
    private static final String VALID_DOWNSTREAM_NAME = "잠실나루";
    private static final int FIVE_DISTANCE = 5;

    /**
     * 저장되어 있는 노선 정보
     * 2호선
     * JAMSIL_TO_JAMSILNARU (섹션) : JAMSIL_STATION --(5)--> JAMSIL_NARU_STATION
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @DisplayName("/lines POST 요청을 하면 지하철 노선을 생성한다.")
    @Test
    void addLineSuccess() {
        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(toJson(new AddLineRequest("3호선", "장산", "서울대입구서울15자이름입니다", 1)))
                .when().post(LINE_URL)
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", containsString("/lines/"));
    }

    @ParameterizedTest(name = "/lines post 요청 상행역 이름 길이가 맞지 않으면 역을 추가할 수 없다.")
    @ValueSource(strings = {"서", "서울대입구서울대16자이름입니다"})
    void addLineFail1(String invalidStationName) {
        given().log().all()
                .contentType(ContentType.JSON)
                .body(toJson(new AddLineRequest(VALID_LINE_NAME, VALID_UPSTREAM_NAME, invalidStationName, FIVE_DISTANCE)))
                .when()
                .post(LINE_URL)
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @ParameterizedTest(name = "/lines post 요청 하행역 이름 길이가 맞지 않으면 역을 추가할 수 없다.")
    @ValueSource(strings = {"서", "서울대입구서울대16자이름입니다"})
    void addLineFail2(String invalidStationName) {
        given().log().all()
                .contentType(ContentType.JSON)
                .body(toJson(new AddLineRequest(VALID_LINE_NAME, invalidStationName, VALID_DOWNSTREAM_NAME, FIVE_DISTANCE)))
                .when()
                .post(LINE_URL)
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @ParameterizedTest(name = "/lines post 요청 노선 이름 길이가 맞지 않으면 역을 추가할 수 없다.")
    @ValueSource(strings = {"서", "서울대입구서울대16자이름입니다"})
    void addLineFail3(String invalidLineName) {
        given().log().all()
                .contentType(ContentType.JSON)
                .body(toJson(new AddLineRequest(invalidLineName, VALID_UPSTREAM_NAME, VALID_DOWNSTREAM_NAME, FIVE_DISTANCE)))
                .when()
                .post(LINE_URL)
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("/lines post 요청 거리가 1보다 작으면 역을 추가할 수 없다.")
    void addLineFail4() {
        given().log().all()
                .contentType(ContentType.JSON)
                .body(toJson(new AddLineRequest(VALID_LINE_NAME, VALID_UPSTREAM_NAME, VALID_DOWNSTREAM_NAME, 0)))
                .when()
                .post(LINE_URL)
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("/lines post 노선 이름이 이미 존재하면 역을 추가할 수 없다.")
    void addLineFail5() {
        given().log().all()
                .contentType(ContentType.JSON)
                .body(toJson(new AddLineRequest(super.SET_UP_LINE_NAME, VALID_UPSTREAM_NAME, VALID_DOWNSTREAM_NAME, FIVE_DISTANCE)))
                .when()
                .post(LINE_URL)
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("/lines/{id} GET 요청을 하면 해당 id의 노선을 조회한다.")
    @Test
    void getLineSuccess() {
        given().log().all()
                .when().get(LINE_URL + "/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("lineName", equalTo("2호선"))
                .body("stationNames", equalToObject(List.of("잠실", "잠실나루")));
    }

    @DisplayName("/lines GET 요청을 하면 모든 노선을 조회한다.")
    @Test
    void getLinesSuccess() {
        given().log().all()
                .when().get(LINE_URL)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("[0].lineName", equalTo("2호선"))
                .body("[0].stationNames", equalToObject(List.of("잠실", "잠실나루")));
    }

    @DisplayName("/lines/{id} GET 요청시 Line id가 존재하지 않는 경우 예외를 던진다.")
    @Test
    void getLinesFail() {
        given().log().all()
                .when().get(LINE_URL + "/0")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
