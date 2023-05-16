package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.integration.step.LineStep.노선_생성_요청;
import static subway.integration.step.SectionStep.구간_생성_요청;
import static subway.integration.step.SectionStep.구간_생성_요청을_생성한다;
import static subway.integration.step.StationStep.역_생성_요청;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionCreateRequest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("지하철 노선 관련 기능")
public class LineIntegrationTest extends IntegrationTest {
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        lineRequest1 = new LineRequest("2호선");
        lineRequest2 = new LineRequest("8호선");
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all()
                .extract();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all()
                .extract();

        // when
        Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse resultResponse = response.as(LineResponse.class);
        assertThat(resultResponse.getId()).isEqualTo(lineId);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> createResponse1 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        ExtractableResponse<Response> createResponse2 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest2)
                .when().post("/lines")
                .then().log().all()
                .extract();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<Long> expectedLineIds = Stream.of(createResponse1, createResponse2)
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    @DisplayName("지하철 구간을 생성한다.")
    @Test
    void createSection() {
        // given
        LineRequest 노선_생성_요청 = new LineRequest("2호선");

        노선_생성_요청(노선_생성_요청);

        역_생성_요청("잠실역", 1L);
        역_생성_요청("잠실새내역", 1L);

        // when
        SectionCreateRequest 요청 = 구간_생성_요청을_생성한다(1L, 2L, 10);
        ExtractableResponse<Response> response = 구간_생성_요청(요청, 1L);

        // then
        assertAll(
                () -> AssertionsForClassTypes.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> AssertionsForClassTypes.assertThat(response.header(HttpHeaders.LOCATION))
                        .isEqualTo("/lines/1/sections/1")
        );
    }


    @Test
    @DisplayName("기존에 존재하는 노선에 구간을 추가한다.")
    void createSectionInExistLine() {
        // given
        LineRequest 노선_생성_요청 = new LineRequest("2호선");

        노선_생성_요청(노선_생성_요청);

        역_생성_요청("잠실역", 1L);
        역_생성_요청("잠실새내역", 1L);
        역_생성_요청("종합운동장역", 1L);
        구간_생성_요청(구간_생성_요청을_생성한다(1L, 2L, 10), 1L);

        // when
        SectionCreateRequest 새로운_구간_생성_요청 = 구간_생성_요청을_생성한다(2L, 3L, 7);
        ExtractableResponse<Response> response = 구간_생성_요청(새로운_구간_생성_요청, 1L);

        // then
        assertAll(
                () -> AssertionsForClassTypes.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> AssertionsForClassTypes.assertThat(response.header(HttpHeaders.LOCATION))
                        .isEqualTo("/lines/1/sections/2")
        );
    }

    @DisplayName("역이 두 개 존재하는 노선의 지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        LineRequest 노선_생성_요청 = new LineRequest("2호선");
        노선_생성_요청(노선_생성_요청);

        역_생성_요청("잠실역", 1L);
        역_생성_요청("잠실새내역", 1L);
        구간_생성_요청(구간_생성_요청을_생성한다(1L, 2L, 10), 1L);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/lines/1/stations/1")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
