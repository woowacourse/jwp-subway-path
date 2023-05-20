package subway.integration;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static subway.exception.ErrorCode.INVALID_REQUEST;
import static subway.exception.ErrorCode.LINE_NAME_DUPLICATED;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.application.dto.LineRequest;
import subway.application.dto.SectionRequest;
import subway.application.dto.StationRequest;

@DisplayName("지하철 노선 관련 기능")
public class LineIntegrationTest extends IntegrationTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    @DisplayName("지하철 노선을 생성한다.")
    void createLine() {
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new LineRequest("이호선", "bg-green-600"))
            .when().post("/lines")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .header("Location", "/lines/1");
    }

    @Test
    @DisplayName("빈 이름과 빈 색상으로 노선을 생성한다.")
    void createLine_blank_name_and_color() {
        // when
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new LineRequest("", "", 1000))
            .when().post("/lines")
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errorCode", equalTo(INVALID_REQUEST.name()))
            .body("errorMessage", containsInAnyOrder("호선 이름을 입력해 주세요.", "호선 색상을 입력해 주세요."));
    }

    @Test
    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    void createLine_duplicate_name() {
        // given
        final LineRequest lineRequest = new LineRequest("이호선", "bg-green-600");
        saveLine(lineRequest);

        // expected
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new LineRequest("이호선", "bg-green-600"))
            .when().post("/lines")
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errorCode", equalTo(LINE_NAME_DUPLICATED.name()))
            .body("errorMessage[0]", equalTo("노선 이름은 중복될 수 없습니다."));
    }

    @Test
    @DisplayName("기존에 존재하는 역을 이용하여 지하철 노선을 생성한다.")
    void createSection() {
        // given
        saveLine(new LineRequest("이호선", "bg-green-600"));
        saveStation(new StationRequest("강남역"));
        saveStation(new StationRequest("역삼역"));

        // expected
        final SectionRequest sectionRequest = new SectionRequest(1L, 1L, 2L, 10);
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(sectionRequest)
            .when().post("/lines/sections")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("지하철 노선 생성 시 노선 아이디, 시작역 아이디, 끝역 아이디, 거리 정보를 빈 값으로 추가한다.")
    void createSection_empty_section_request() {
        final SectionRequest sectionRequest = new SectionRequest(null, null, null, null);
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(sectionRequest)
            .when().post("/lines/sections")
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errorCode", equalTo(INVALID_REQUEST.name()))
            .body("errorMessage", containsInAnyOrder("호선 아이디를 입력해 주세요.", "시작역 아이디를 입력해 주세요.",
                "끝역 아이디를 입력해 주세요.", "거리를 입력해 주세요."));
    }

    @Test
    @DisplayName("지하철 노선 생성 시 올바르지 않은 타입의 노선 아이디, 시작역 아이디, 끝역 아이디, 거리 정보를 추가한다.")
    void createSection_invalid_type_section_request() {
        final SectionRequest sectionRequest = new SectionRequest(0L, 0L, 0L, 0);
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(sectionRequest)
            .when().post("/lines/sections")
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errorCode", equalTo(INVALID_REQUEST.name()))
            .body("errorMessage", containsInAnyOrder("올바른 호선 아이디를 입력해 주세요.",
                "올바른 시작역 아이디를 입력해 주세요.", "올바른 끝역 아이디를 입력해 주세요."));
    }

    @Test
    @DisplayName("지하철 노선 전체 목록을 조회한다.")
    void getAllLines() {
        // given
        saveLine(new LineRequest("이호선", "bg-green-600"));
        saveLine(new LineRequest("팔호선", "bg-pink-600", 1000));
        saveStation(new StationRequest("강남역"));
        saveStation(new StationRequest("역삼역"));
        saveStation(new StationRequest("남위례역"));
        saveStation(new StationRequest("산성역"));
        saveSection(new SectionRequest(1L, 1L, 2L, 10));
        saveSection(new SectionRequest(2L, 3L, 4L, 10));

        // expected
        RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines")
            .then().log().all()
            .body("size", Matchers.is(2))
            .body("[0].name", equalTo("이호선"))
            .body("[0].color", equalTo("bg-green-600"))
            .body("[0].extraFare", equalTo(0))
            .body("[0].stationResponses[0].name", equalTo("강남역"))
            .body("[0].stationResponses[1].name", equalTo("역삼역"))
            .body("[1].name", equalTo("팔호선"))
            .body("[1].color", equalTo("bg-pink-600"))
            .body("[1].extraFare", equalTo(1000))
            .body("[1].stationResponses[0].name", equalTo("남위례역"))
            .body("[1].stationResponses[1].name", equalTo("산성역"));
    }

    @Test
    @DisplayName("지하철 노선 전체 목록을 조회한다. (노선에 등록된 역이 없는 경우)")
    void getAllLines_when_not_exists_station() {
        // given
        saveLine(new LineRequest("이호선", "bg-green-600"));
        saveLine(new LineRequest("팔호선", "bg-pink-600", 1000));

        // expected
        RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines")
            .then().log().all()
            .body("size", Matchers.is(2))
            .body("[0].name", equalTo("이호선"))
            .body("[0].color", equalTo("bg-green-600"))
            .body("[0].extraFare", equalTo(0))
            .body("[1].name", equalTo("팔호선"))
            .body("[1].color", equalTo("bg-pink-600"))
            .body("[1].extraFare", equalTo(1000));
    }

    @Test
    @DisplayName("지하철 노선 목록을 조회한다.")
    void getLineById() {
        // given
        saveLine(new LineRequest("이호선", "bg-green-600", 1000));
        saveStation(new StationRequest("강남역"));
        saveStation(new StationRequest("역삼역"));
        saveSection(new SectionRequest(1L, 1L, 2L, 10));

        // expected
        RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines/{id}", 1)
            .then().log().all()
            .body("name", equalTo("이호선"))
            .body("color", equalTo("bg-green-600"))
            .body("extraFare", equalTo(1000))
            .body("stationResponses[0].name", equalTo("강남역"))
            .body("stationResponses[1].name", equalTo("역삼역"));
    }

    @Test
    @DisplayName("지하철 노선 목록을 조회한다. (노선에 등록된 역이 없는 경우)")
    void getLineById_when_not_exists_station() {
        // given
        saveLine(new LineRequest("이호선", "bg-green-600", 1000));
        saveStation(new StationRequest("강남역"));
        saveStation(new StationRequest("역삼역"));
        saveSection(new SectionRequest(1L, 1L, 2L, 10));

        // expected
        RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines/{id}", 1)
            .then().log().all()
            .body("name", equalTo("이호선"))
            .body("color", equalTo("bg-green-600"))
            .body("extraFare", equalTo(1000));
    }

    @Test
    @DisplayName("지하철 노선을 수정한다.")
    void updateLine() {
        // given
        final LineRequest lineRequest = new LineRequest("이호선", "bg-green-600");
        saveLine(lineRequest);

        // expected
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new LineRequest("팔호선", "bg-pink-600"))
            .when().put("/lines/{lineId}", 1)
            .then().log().all()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("지하철 노선을 제거한다.")
    void deleteLine() {
        // given
        final LineRequest lineRequest = new LineRequest("이호선", "bg-green-600");
        saveLine(lineRequest);

        // expected
        RestAssured
            .given().log().all()
            .when().delete("/lines/{lineId}", 1)
            .then().log().all()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("지하철 노선의 역을 제거한다.")
    void deleteStationInLine() {
        // given
        saveLine(new LineRequest("이호선", "bg-green-600"));
        saveStation(new StationRequest("강남역"));
        saveStation(new StationRequest("역삼역"));
        saveSection(new SectionRequest(1L, 1L, 2L, 10));

        // expected
        RestAssured
            .given().log().all()
            .when().delete("/lines/{id}/stations/{stationId}", 1, 1)
            .then().log().all()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private void saveLine(final LineRequest lineRequest) {
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(lineRequest)
            .when().post("/lines")
            .then().log().all();
    }

    private void saveStation(final StationRequest stationRequest) {
        RestAssured.given().log().all()
            .body(stationRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all();
    }

    private void saveSection(final SectionRequest sectionRequest) {
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(sectionRequest)
            .when().post("/lines/sections")
            .then().log().all().extract();
    }
}
