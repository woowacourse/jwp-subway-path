package subway.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.integration.IntegrationFixture.GANGNAM;
import static subway.integration.IntegrationFixture.JAMSIL;
import static subway.integration.IntegrationFixture.LINE_2;
import static subway.integration.IntegrationFixture.LINE_3;
import static subway.integration.IntegrationFixture.OBJECT_MAPPER;
import static subway.integration.IntegrationFixture.SAMSUNG;
import static subway.integration.IntegrationFixture.SEONGLENUG;
import static subway.integration.IntegrationFixture.jsonSerialize;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.service.dto.LineRequest;
import subway.service.dto.LineResponse;
import subway.service.dto.SectionRequest;
import subway.service.dto.StationRequest;
import subway.service.dto.StationResponse;

@DisplayName("지하철 노선 관련 기능")
public class LineIntegrationTest extends IntegrationTest {

    private final LineRequest lineRequest1 = new LineRequest("분당");
    private final LineRequest lineRequest2 = new LineRequest(LINE_2.getName().getValue());

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        final ExtractableResponse<Response> response = RestAssured
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
        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest2)
                .when().post("/lines")
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() throws JsonProcessingException {
        // given when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();

        // then
        final String string = response.asString();
        final LineResponse[] responses = OBJECT_MAPPER.readValue(string, LineResponse[].class);
        assertAll(
                () -> assertThat(responses)
                        .extracting(LineResponse::getId, LineResponse::getName)
                        .contains(
                                tuple(1L, "2호선"),
                                tuple(2L, "3호선")
                        )
        );
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        final Long lineId = LINE_3.getId();

        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final LineResponse result = response.as(LineResponse.class);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(lineId),
                () -> assertThat(result.getName()).isEqualTo(LINE_3.getName().getValue()),
                () -> assertThat(result.getStations())
                        .extracting(StationResponse::getName)
                        .containsExactly(GANGNAM.getName(), SEONGLENUG.getName(), SAMSUNG.getName())
        );
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        final Long lineId = 1L;
        final ExtractableResponse<Response> response = RestAssured
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
        //given
        final Long lineId = LINE_2.getId();

        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("노선에 역을 최초 등록한다.")
    @Test
    void addInitialStationInLine() throws JsonProcessingException {
        final SectionRequest request = new SectionRequest(JAMSIL.getName(), GANGNAM.getName(), 10);

        final String json = jsonSerialize(request);

        given().body(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + LINE_2.getId() + "/register")
                .then().statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("노선에서 역을 삭제한다.")
    @Test
    void deleteStationInLine() throws JsonProcessingException {
        final StationRequest stationRequest = new StationRequest(SEONGLENUG.getName());

        given().body(jsonSerialize(stationRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/" + LINE_3.getId() + "/unregister")
                .then().statusCode(HttpStatus.NO_CONTENT.value());
    }
}
