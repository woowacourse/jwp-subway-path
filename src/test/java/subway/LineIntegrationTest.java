package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import subway.dto.InitialSectionCreateRequest;
import subway.dto.SectionCreateRequest;
import subway.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static subway.steps.LineSteps.*;
import static subway.steps.StationSteps.*;

class LineIntegrationTest extends IntegrationTest {

    @Test
    void 전체_노선을_조회한다() {

        final long 노선_9호선_아이디 = 노선_생성하고_아이디_반환(노선_9호선);
        final long 고속터미널_아이디 = 역_생성하고_아이디_반환(역_고속터미널);
        final long 사평역_아이디 = 역_생성하고_아이디_반환(역_사평역);

        노선에_최초의_역_2개_추가_요청(
                new InitialSectionCreateRequest(
                        노선_9호선_아이디,
                        고속터미널_아이디,
                        사평역_아이디,
                        5
                ));

        존재하는_노선에_역_1개_추가_요청(
                노선_9호선_아이디,
                new SectionCreateRequest(
                        고속터미널_아이디,
                        사평역_아이디,
                        3
                ));

        final ExtractableResponse<Response> response = 전체_노선_조회_요청();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                () -> assertThat(response.body()).isNotNull()
        );
    }

    @Test
    void 신규_노선을_등록한다() {
        final ExtractableResponse<Response> response = 노선_생성_요청(노선_9호선);

        assertAll(
                () -> assertThat(response.statusCode())
                        .isEqualTo(CREATED.value()),
                () -> assertThat(response.header("Location"))
                        .isNotBlank()
        );
    }

    @Test
    void 새_노선에_최초의_역_2개를_추가한다() {
        final long station1Id = 역_생성하고_아이디_반환(역_고속터미널);
        final long station2Id = 역_생성하고_아이디_반환(역_사평역);
        final long lineId = 노선_생성하고_아이디_반환(노선_9호선);

        final ExtractableResponse<Response> response = 노선에_최초의_역_2개_추가_요청(
                new InitialSectionCreateRequest(
                        lineId, station1Id, station2Id, 5
                ));

        final List<StationResponse> expectedStations = List.of(
                new StationResponse(station1Id, "고속터미널"),
                new StationResponse(station2Id, "사평역")
        );

        final List<StationResponse> receivedStations = response.jsonPath()
                .getList("stations", StationResponse.class);

        assertAll(
                () -> assertThat(receivedStations)
                        .usingRecursiveComparison()
                        .isEqualTo(expectedStations),
                () -> assertThat(response.statusCode()).isEqualTo(CREATED.value()));
    }

    @Test
    void 기존_노선에_새로운_역을_추가한다() {
        final long station1Id = 역_생성하고_아이디_반환(역_고속터미널);
        final long station2Id = 역_생성하고_아이디_반환(역_사평역);
        final long newStationId = 역_생성하고_아이디_반환(역_새역);

        final long lineId = 노선_생성하고_아이디_반환(노선_9호선);

        노선에_최초의_역_2개_추가_요청(
                new InitialSectionCreateRequest(
                        lineId, station1Id, station2Id, 5
                ));

        final ExtractableResponse<Response> response = 존재하는_노선에_역_1개_추가_요청(lineId,
                new SectionCreateRequest(station1Id, newStationId, 3));

        assertThat(response.statusCode()).isEqualTo(CREATED.value());
    }

    @Test
    void 노선에_역을_제거한다() {
        final long station1Id = 역_생성하고_아이디_반환(역_고속터미널);
        final long station2Id = 역_생성하고_아이디_반환(역_사평역);
        final long newStationId = 역_생성하고_아이디_반환(역_새역);

        final long lineId = 노선_생성하고_아이디_반환(노선_9호선);

        노선에_최초의_역_2개_추가_요청(
                new InitialSectionCreateRequest(
                        lineId, station1Id, station2Id, 5
                ));

        존재하는_노선에_역_1개_추가_요청(
                lineId,
                new SectionCreateRequest(
                        station1Id, newStationId, 3
                ));

        final ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(APPLICATION_JSON_VALUE)

                .when()
                .delete("/lines/" + lineId + "/stations/" + newStationId)

                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
    }

    @Test
    void 존재하는_노선에_역_1개를_추가한다() {
        final long station1Id = 역_생성하고_아이디_반환(역_고속터미널);
        final long station2Id = 역_생성하고_아이디_반환(역_사평역);
        final long newStationId = 역_생성하고_아이디_반환(역_새역);

        final long lineId = 노선_생성하고_아이디_반환(노선_9호선);

        노선에_최초의_역_2개_추가_요청(
                new InitialSectionCreateRequest(
                        lineId, station1Id, station2Id, 3
                ));

        final ExtractableResponse<Response> response = 존재하는_노선에_역_1개_추가_요청(
                lineId,
                new SectionCreateRequest(
                        station1Id, newStationId, 1
                ));

        assertThat(response.statusCode()).isEqualTo(CREATED.value());
    }
}
