package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static subway.integration.TestFixture.SEOULYEOK_ID;
import static subway.integration.TestFixture.SICHUNG_ID;
import static subway.integration.TestFixture.SINDORIM_ID;
import static subway.integration.TestFixture.JONGGAK_ID;
import static subway.integration.TestFixture.JONGROSAMGA_ID;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.test.context.ActiveProfiles;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationDeleteRequest;
import subway.dto.StationRegisterRequest;

@ActiveProfiles("test")
@DisplayName("지하철 노선 관련 기능")
class LineIntegrationTest extends IntegrationTest {
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        lineRequest1 = new LineRequest("신분당선", "bg-red-600");
        lineRequest2 = new LineRequest("구신분당선", "bg-red-600");
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
                .then().log().all().
                extract();

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
                .then().log().all().
                extract();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
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
        Long lineId1 = Long.parseLong(createResponse1.header("Location").split("/")[2]);

        ExtractableResponse<Response> createResponse2 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest2)
                .when().post("/lines")
                .then().log().all().
                extract();
        Long lineId2 = Long.parseLong(createResponse2.header("Location").split("/")[2]);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new StationRegisterRequest(JONGGAK_ID, JONGROSAMGA_ID, 10))
                .when().post("/lines/{lineId}/stations", lineId1)
                .then().log().all().
                extract();

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new StationRegisterRequest(SINDORIM_ID, SEOULYEOK_ID, 10))
                .when().post("/lines/{lineId}/stations", lineId2)
                .then().log().all().
                extract();

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

        List<LineResponse> lineResponses = response.jsonPath().getList(".", LineResponse.class);

        assertThat(lineResponses.get(lineId1.intValue() - 1).getStations())
                .extracting("id", "name")
                .containsExactly(
                        tuple(JONGGAK_ID, "종각"),
                        tuple(JONGROSAMGA_ID, "종로3가")
                );
        assertThat(lineResponses.get(lineId2.intValue() - 1).getStations())
                .extracting("id", "name")
                .containsExactly(
                        tuple(SINDORIM_ID, "신도림"),
                        tuple(SEOULYEOK_ID, "서울역")
                );
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
                .then().log().all().
                extract();

        Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new StationRegisterRequest(SEOULYEOK_ID, SICHUNG_ID, 10))
                .when().post("/lines/{lineId}/stations", lineId)
                .then().log().all().
                extract();

        // when
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
        assertThat(resultResponse.getStations())
                .extracting("id", "name")
                .containsExactly(
                        tuple(SEOULYEOK_ID, "서울역"),
                        tuple(SICHUNG_ID, "시청")
                );
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest2)
                .when().put("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();
        Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new StationRegisterRequest(SEOULYEOK_ID, SINDORIM_ID, 10))
                .when().post("/lines/{lineId}/stations", lineId)
                .then().log().all().
                extract();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("빈 노선에 두 역을 등록한다")
    @Test
    void registerStationsToEmptyLine() {
        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new StationRegisterRequest(SEOULYEOK_ID, SINDORIM_ID, 10))
                .when().post("/lines/5/stations")
                .then().log().all().
                extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("역이 등록된 노선에 이미 등록된 두 역을 등록하면 예외가 발생한다")
    @Test
    void throwExceptionWhenRegisterSameStations() {
        //given
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new StationRegisterRequest(SEOULYEOK_ID, SINDORIM_ID, 10))
                .when().post("/lines/2/stations")
                .then().log().all().
                extract();

        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new StationRegisterRequest(SEOULYEOK_ID, SINDORIM_ID, 10))
                .when().post("/lines/2/stations")
                .then().log().all().
                extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("역이 등록된 노선에 등록되지 않은 두 역을 등록하면 예외가 발생한다")
    @Test
    void throwExceptionWhenRegisterStationsToNonEmptyLine() {
        //given
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new StationRegisterRequest(SEOULYEOK_ID, SINDORIM_ID, 10))
                .when().post("/lines/2/stations")
                .then().log().all().
                extract();

        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new StationRegisterRequest(SICHUNG_ID, JONGGAK_ID, 10))
                .when().post("/lines/2/stations")
                .then().log().all().
                extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("노선에서 기준 역의 상행에 하나의 역을 등록한다")
    @Test
    void registerUpperStation() {
        //given
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new StationRegisterRequest(SEOULYEOK_ID, SINDORIM_ID, 10))
                .when().post("/lines/5/stations")
                .then().log().all().
                extract();

        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new StationRegisterRequest(SICHUNG_ID, SEOULYEOK_ID, 5))
                .when().post("/lines/5/stations")
                .then().log().all().
                extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("노선에서 기준 역의 하행에 하나의 역을 등록한다")
    @Test
    void registerLowerStation() {
        //given
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new StationRegisterRequest(SEOULYEOK_ID, SINDORIM_ID, 10))
                .when().post("/lines/5/stations")
                .then().log().all().
                extract();

        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new StationRegisterRequest(SEOULYEOK_ID, SICHUNG_ID, 5))
                .when().post("/lines/5/stations")
                .then().log().all().
                extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("노선에 잘못된 거리 정보를 가지는 하나의 역을 등록하면 예외가 발생한다.")
    @Test
    void registerStationFailWhenWrongDistance() {
        //given
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new StationRegisterRequest(SEOULYEOK_ID, SINDORIM_ID, 10))
                .when().post("/lines/2/stations")
                .then().log().all().
                extract();

        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new StationRegisterRequest(SEOULYEOK_ID, SICHUNG_ID,15))
                .when().post("/lines/2/stations")
                .then().log().all().
                extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("노선에 이미 등록된 역을 등록하면 예외가 발생한다.")
    @Test
    void registerStationFailWhenStationExist() {
        //given
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new StationRegisterRequest(SEOULYEOK_ID, SINDORIM_ID, 10))
                .when().post("/lines/2/stations")
                .then().log().all().
                extract();

        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new StationRegisterRequest(SINDORIM_ID, SEOULYEOK_ID, 5))
                .when().post("/lines/2/stations")
                .then().log().all().
                extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("노선의 역을 제거한다")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();
        Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new StationRegisterRequest(SINDORIM_ID, SEOULYEOK_ID, 10))
                .when().post("/lines/{lineId}/stations", lineId)
                .then().log().all().
                extract();

        // when
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new StationDeleteRequest(SINDORIM_ID))
                .when().delete("/lines/{lineId}/stations", lineId)
                .then().log().all()
                .extract();

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();
        LineResponse lineResponse = response.body().as(LineResponse.class);

        // then
        assertThat(lineResponse.getStations()).isEmpty();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
