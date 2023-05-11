package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.LineRequest;
import subway.dto.SectionRequest;
import subway.exception.ErrorCode;

@DisplayName("지하철 노선 관련 기능")
public class LineIntegrationTest extends IntegrationTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    @Sql("classpath:/init.sql")
    @DisplayName("지하철 노선을 생성한다.")
    void createLine() {
        // when
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new LineRequest("이호선", "bg-green-600"))
            .when().post("/lines")
            .then().log().all().
            extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @Test
    @Sql("classpath:/init.sql")
    @DisplayName("빈 이름과 빈 색상으로 노선을 생성한다.")
    void createLine_blank_name_and_color() {
        // when
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new LineRequest("", ""))
            .when().post("/lines")
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errorCode", equalTo(ErrorCode.INVALID_REQUEST.name()))
            .body("errorMessage", containsInAnyOrder("호선 이름을 입력해 주세요.", "호선 색상을 입력해 주세요."));
    }

    @Test
    @Sql("classpath:/init.sql")
    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    void createLineWithDuplicateName() {
        // given
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new LineRequest("이호선", "bg-green-600"))
            .when().post("/lines")
            .then().log().all().
            extract();

        // when
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new LineRequest("이호선", "bg-green-600"))
            .when().post("/lines")
            .then().log().all().
            extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Sql("classpath:/init.sql")
    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    void createSection() {
        // given
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new LineRequest("이호선", "bg-green-600"))
            .when().post("/lines")
            .then().log().all().
            extract();

        Map<String, String> params1 = new HashMap<>();
        params1.put("name", "강남역");
        RestAssured.given().log().all()
            .body(params1)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all()
            .extract();

        Map<String, String> params2 = new HashMap<>();
        params2.put("name", "역삼역");
        RestAssured.given().log().all()
            .body(params2)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all()
            .extract();

        // when
        final SectionRequest sectionRequest = new SectionRequest(1L, 1L, 2L, 10);
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(sectionRequest)
            .when().post("/lines/sections")
            .then().log().all().
            extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @Sql("classpath:/init.sql")
    @DisplayName("지하철 노선 생성 시 노선 아이디, 시작역 아이디, 끝역 아이디, 거리 정보를 빈 값으로 추가한다.")
    void createSection_empty_section_request() {
        // expected
        final SectionRequest sectionRequest = new SectionRequest(null, null, null, null);
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(sectionRequest)
            .when().post("/lines/sections")
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errorCode", equalTo(ErrorCode.INVALID_REQUEST.name()))
            .body("errorMessage", containsInAnyOrder("호선 아이디를 입력해 주세요.", "시작역 아이디를 입력해 주세요.",
                "끝역 아이디를 입력해 주세요.", "거리를 입력해 주세요."));
    }

    @Test
    @Sql("classpath:/init.sql")
    @DisplayName("지하철 노선 생성 시 올바르지 않은 타입의 노선 아이디, 시작역 아이디, 끝역 아이디, 거리 정보를 추가한다.")
    void createSection_invalid_type_section_request() {
        // expected
        final SectionRequest sectionRequest = new SectionRequest(0L, 0L, 0L, 0);
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(sectionRequest)
            .when().post("/lines/sections")
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errorCode", equalTo(ErrorCode.INVALID_REQUEST.name()))
            .body("errorMessage", containsInAnyOrder("올바른 호선 아이디를 입력해 주세요.",
                "올바른 시작역 아이디를 입력해 주세요.", "올바른 끝역 아이디를 입력해 주세요."));
    }

    @Test
    @Sql("classpath:/init.sql")
    @DisplayName("지하철 노선 전체 목록을 조회한다.")
    void getLines() {
        // given
        // 노선 추가
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new LineRequest("이호선", "bg-green-600"))
            .when().post("/lines")
            .then().log().all().
            extract();

        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new LineRequest("팔호선", "bg-pink-600"))
            .when().post("/lines")
            .then().log().all().
            extract();

        // 역 추가
        Map<String, String> params1 = new HashMap<>();
        params1.put("name", "강남역");
        RestAssured.given().log().all()
            .body(params1)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all()
            .extract();

        Map<String, String> params2 = new HashMap<>();
        params2.put("name", "역삼역");
        RestAssured.given().log().all()
            .body(params2)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all()
            .extract();

        Map<String, String> params3 = new HashMap<>();
        params3.put("name", "남위례역");
        RestAssured.given().log().all()
            .body(params3)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all()
            .extract();

        Map<String, String> params4 = new HashMap<>();
        params4.put("name", "산성역");
        RestAssured.given().log().all()
            .body(params4)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all()
            .extract();

        // 구간 추가
        final SectionRequest sectionRequest1 = new SectionRequest(1L, 1L, 2L, 10);
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(sectionRequest1)
            .when().post("/lines/sections")
            .then().log().all().extract();

        final SectionRequest sectionRequest2 = new SectionRequest(2L, 3L, 4L, 10);
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(sectionRequest2)
            .when().post("/lines/sections")
            .then().log().all().extract();

        // expected
        RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines")
            .then().log().all()
            .body("size", Matchers.is(2))
            .body("[0].name", equalTo("이호선"))
            .body("[0].color", equalTo("bg-green-600"))
            .body("[0].stationResponses[0].name", equalTo("강남역"))
            .body("[0].stationResponses[1].name", equalTo("역삼역"))
            .body("[1].name", equalTo("팔호선"))
            .body("[1].color", equalTo("bg-pink-600"))
            .body("[1].stationResponses[0].name", equalTo("남위례역"))
            .body("[1].stationResponses[1].name", equalTo("산성역"));
    }

    @Test
    @Sql("classpath:/init.sql")
    @DisplayName("지하철 노선 목록을 조회한다.")
    void getLineById() {
        // given
        // 노선 추가
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new LineRequest("이호선", "bg-green-600"))
            .when().post("/lines")
            .then().log().all().
            extract();

        // 역 추가
        Map<String, String> params1 = new HashMap<>();
        params1.put("name", "강남역");
        RestAssured.given().log().all()
            .body(params1)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all()
            .extract();

        Map<String, String> params2 = new HashMap<>();
        params2.put("name", "역삼역");
        RestAssured.given().log().all()
            .body(params2)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all()
            .extract();

        // 구간 추가
        final SectionRequest sectionRequest = new SectionRequest(1L, 1L, 2L, 10);
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(sectionRequest)
            .when().post("/lines/sections")
            .then().log().all().extract();

        // expected
        RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines/{id}", 1)
            .then().log().all()
            .body("name", equalTo("이호선"))
            .body("color", equalTo("bg-green-600"))
            .body("stationResponses[0].name", equalTo("강남역"))
            .body("stationResponses[1].name", equalTo("역삼역"));
    }

    @Test
    @Sql("classpath:/init.sql")
    @DisplayName("지하철 노선을 수정한다.")
    void updateLine() {
        // given
        ExtractableResponse<Response> createResponse = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new LineRequest("이호선", "bg-green-600"))
            .when().post("/lines")
            .then().log().all().
            extract();

        // when
        Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new LineRequest("팔호선", "bg-pink-600"))
            .when().put("/lines/{lineId}", lineId)
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @Sql("classpath:/init.sql")
    @DisplayName("지하철 노선을 제거한다.")
    void deleteLine() {
        // given
        final ExtractableResponse<Response> createResponse = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new LineRequest("이호선", "bg-green-600"))
            .when().post("/lines")
            .then().log().all()
            .extract();

        // when
        final Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        final ExtractableResponse<Response> deleteResponse = RestAssured
            .given().log().all()
            .when().delete("/lines/{lineId}", lineId)
            .then().log().all()
            .extract();

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
