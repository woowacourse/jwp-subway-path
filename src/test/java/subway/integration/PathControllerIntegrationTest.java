package subway.integration;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static subway.integration.common.JsonMapper.toJson;
import static subway.integration.line.LineStationSteps.노선에_역_추가_요청;
import static subway.integration.line.LineSteps.노선_생성_요청;
import static subway.integration.line.LineSteps.노선에_포함된_N번째_구간을_검증한다;
import static subway.integration.station.StationSteps.역_생성_요청;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.request.ShortestPathRequest;
import subway.dto.response.ShortestPathResponse;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("pathController 통합테스트 은(는)")
@Sql("/truncate.sql")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PathControllerIntegrationTest {

    private static final String API_URL = "/path-shorted";

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 최단_거리_조회_성공_기본_요금() {
        // given
        역_생성_요청("잠실역");
        역_생성_요청("사당역");
        역_생성_요청("경북대북문역");
        역_생성_요청("경북대정문역");
        노선_생성_요청("1호선", "잠실역", "사당역", 1, 0);
        노선_생성_요청("2호선", "사당역", "경북대북문역", 2, 0);
        노선에_역_추가_요청("2호선", "경북대북문역", "경북대정문역", 3);
        final ShortestPathRequest request = new ShortestPathRequest("잠실역", "경북대정문역");

        // when
        final ExtractableResponse<Response> response = 최단_거리_조회_요청(request);

        // then
        final ShortestPathResponse responseDto = response.as(ShortestPathResponse.class);
        노선에_포함된_N번째_구간을_검증한다(responseDto, 0, "잠실역", "사당역", 1);
        노선에_포함된_N번째_구간을_검증한다(responseDto, 1, "사당역", "경북대북문역", 2);
        노선에_포함된_N번째_구간을_검증한다(responseDto, 2, "경북대북문역", "경북대정문역", 3);
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(responseDto.getTotalDistance()).isEqualTo(6);
        assertThat(responseDto.getFee()).isEqualTo(1250);
    }

    @Test
    void 최단_거리_조회_성공_추가__요금() {
        // given
        역_생성_요청("잠실역");
        역_생성_요청("사당역");
        역_생성_요청("경북대북문역");
        역_생성_요청("경북대정문역");
        노선_생성_요청("1호선", "잠실역", "사당역", 5, 100);
        노선_생성_요청("2호선", "사당역", "경북대북문역", 5, 200);
        노선에_역_추가_요청("2호선", "경북대북문역", "경북대정문역", 10);
        final ShortestPathRequest request = new ShortestPathRequest("잠실역", "경북대정문역");

        // when
        final ExtractableResponse<Response> response = 최단_거리_조회_요청(request);

        // then
        final ShortestPathResponse responseDto = response.as(ShortestPathResponse.class);
        노선에_포함된_N번째_구간을_검증한다(responseDto, 0, "잠실역", "사당역", 5);
        노선에_포함된_N번째_구간을_검증한다(responseDto, 1, "사당역", "경북대북문역", 5);
        노선에_포함된_N번째_구간을_검증한다(responseDto, 2, "경북대북문역", "경북대정문역", 10);
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(responseDto.getTotalDistance()).isEqualTo(20);
        assertThat(responseDto.getFee()).isEqualTo(1650);
    }

    @Test
    void 시작역이_없으면_예외() {
        // given
        역_생성_요청("사당역");
        final ShortestPathRequest request = new ShortestPathRequest("잠실역", "사당역");

        // when
        final ExtractableResponse<Response> response = 최단_거리_조회_요청(request);

        // then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
    }

    @Test
    void 도착역이_없으면_예외() {
        // given
        역_생성_요청("잠실역");
        final ShortestPathRequest request = new ShortestPathRequest("잠실역", "사당역");

        // when
        final ExtractableResponse<Response> response = 최단_거리_조회_요청(request);

        // then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
    }

    @Test
    void 경로를_못찾으면_예외_역은_있으나_노선에_없는_경우() {
        // given
        역_생성_요청("사당역");
        역_생성_요청("잠실역");
        역_생성_요청("경북대입구역");
        노선_생성_요청("1호선", "잠실역", "사당역", 10, 100);
        final ShortestPathRequest request = new ShortestPathRequest("잠실역", "경북대입구역");

        // when
        final ExtractableResponse<Response> response = 최단_거리_조회_요청(request);

        // then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
    }

    @Test
    void 경로를_못찾으면_예외_역이_노선에_있으나_연결되지_않은_경우() {
        // given
        역_생성_요청("사당역");
        역_생성_요청("잠실역");
        역_생성_요청("경북대입구역");
        역_생성_요청("경북대북문역");
        노선_생성_요청("1호선", "잠실역", "사당역", 10, 100);
        노선_생성_요청("2호선", "경북대입구역", "경북대북문역", 10, 100);
        final ShortestPathRequest request = new ShortestPathRequest("잠실역", "경북대입구역");

        // when
        final ExtractableResponse<Response> response = 최단_거리_조회_요청(request);

        // then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 시작역이_null이거나_공백이면_예외(final String nullAndEmpty) {
        // given
        final ShortestPathRequest request = new ShortestPathRequest(nullAndEmpty, "사당역");

        // when
        final ExtractableResponse<Response> response = 최단_거리_조회_요청(request);

        // then
        assertThat(response.statusCode()).isEqualTo(UNPROCESSABLE_ENTITY.value());
    }

    private ExtractableResponse<Response> 최단_거리_조회_요청(final ShortestPathRequest request) {
        final String body = toJson(request);
        return given().log().all()
                .contentType(JSON)
                .body(body)
                .when()
                .get(API_URL)
                .then()
                .log().all()
                .extract();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 도착이_null이거나_공백이면_예외(final String nullAndEmpty) {
        // given
        final ShortestPathRequest request = new ShortestPathRequest("사당역", nullAndEmpty);

        // when
        final ExtractableResponse<Response> response = 최단_거리_조회_요청(request);

        // then
        assertThat(response.statusCode()).isEqualTo(UNPROCESSABLE_ENTITY.value());
    }
}
