package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import subway.dto.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static subway.fixture.StationFixture.EXPRESS_BUS_TERMINAL_STATION;
import static subway.fixture.StationFixture.SAPYEONG_STATION;
import static subway.steps.LineSteps.*;
import static subway.steps.StationSteps.역_생성하고_아이디_반환;

class LineIntegrationTest extends IntegrationTest {
    public static final LineCreateRequest LINE_NINE_CREATE_REQUEST = new LineCreateRequest("9호선", "BROWN");
    public static final StationCreateRequest EXPRESS_BUS_TERMINAL_REQUEST = new StationCreateRequest(EXPRESS_BUS_TERMINAL_STATION.getName());
    public static final StationCreateRequest SAPYEONG_STATION_REQUEST = new StationCreateRequest(SAPYEONG_STATION.getName());
    public static final StationCreateRequest NEW_STATION_REQUEST = new StationCreateRequest("새 역");

    @Test
    void findAllLinesTest() {
        final long station1Id = 역_생성하고_아이디_반환(EXPRESS_BUS_TERMINAL_REQUEST);
        final long station2Id = 역_생성하고_아이디_반환(SAPYEONG_STATION_REQUEST);
        final long newStationId = 역_생성하고_아이디_반환(NEW_STATION_REQUEST);

        final long lineId = 노선_생성하고_아이디_반환(LINE_NINE_CREATE_REQUEST);

        노선에_최초의_역_2개_추가_요청(
                lineId,
                new InitialSectionCreateRequest(
                        lineId, station1Id, station2Id, 5
                ));
        존재하는_노선에_역_1개_추가_요청(station1Id, newStationId, lineId);

        final ExtractableResponse<Response> response = 전체_노선_조회_요청();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                () -> assertThat(response.body()).isNotNull()
        );
    }

    @Test
    void createNewLineTest() {
        final ExtractableResponse<Response> response = 노선_생성_요청(LINE_NINE_CREATE_REQUEST);

        assertAll(
                () -> assertThat(response.statusCode())
                        .isEqualTo(CREATED.value()),
                () -> assertThat(response.header("Location"))
                        .isNotBlank()
        );
    }

    @Test
    void createInitialSectionTest() {
        final long station1Id = 역_생성하고_아이디_반환(EXPRESS_BUS_TERMINAL_REQUEST);
        final long station2Id = 역_생성하고_아이디_반환(SAPYEONG_STATION_REQUEST);
        final long lineId = 노선_생성하고_아이디_반환(LINE_NINE_CREATE_REQUEST);

        final ExtractableResponse<Response> response = 노선에_최초의_역_2개_추가_요청(
                lineId,
                new InitialSectionCreateRequest(
                        lineId, station1Id, station2Id, 5
                ));

        assertThat(response.statusCode()).isEqualTo(CREATED.value());
    }

    @Test
    void createStationInLineTest() {
        final StationResponse stationResponse1 = createNewStation(EXPRESS_BUS_TERMINAL_REQUEST);
        final StationResponse stationResponse2 = createNewStation(SAPYEONG_STATION_REQUEST);
        final StationResponse newStationResponse = createNewStation(NEW_STATION_REQUEST);

        final LineResponse lineResponse = createNewLine(LINE_NINE_CREATE_REQUEST).as(LineResponse.class);

        final InitialSectionCreateRequest initialSectionCreateRequest = new InitialSectionCreateRequest(
                lineResponse.getId(), stationResponse1.getId(), stationResponse2.getId(), 5
        );

        노선에_최초의_역_2개_추가_요청(lineResponse, initialSectionCreateRequest);

        final ExtractableResponse<Response> response = addStationInLine(stationResponse1, newStationResponse, lineResponse);

        assertThat(response.statusCode()).isEqualTo(CREATED.value());
    }

    private ExtractableResponse<Response> addStationInLine(final StationResponse stationResponse1, final StationResponse stationResponse2, final LineResponse lineResponse) {
        final SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(
                stationResponse1.getId(),
                stationResponse2.getId(),
                3);

        return RestAssured
                .given()
                .contentType(APPLICATION_JSON_VALUE)
                .body(sectionCreateRequest)

                .when()
                .post("/lines/" + lineResponse.getId() + "/stations")

                .then()
                .extract();
    }

    @Test
    void deleteStationInLineTest() {
        final StationResponse stationResponse1 = createNewStation(EXPRESS_BUS_TERMINAL_REQUEST);
        final StationResponse stationResponse2 = createNewStation(SAPYEONG_STATION_REQUEST);
        final StationResponse newStationResponse = createNewStation(NEW_STATION_REQUEST);

        final LineResponse lineResponse = createNewLine(LINE_NINE_CREATE_REQUEST).as(LineResponse.class);

        final InitialSectionCreateRequest initialSectionCreateRequest = new InitialSectionCreateRequest(
                lineResponse.getId(), stationResponse1.getId(), stationResponse2.getId(), 5
        );

        노선에_최초의_역_2개_추가_요청(lineResponse, initialSectionCreateRequest);
        addStationInLine(stationResponse1, newStationResponse, lineResponse);

        final ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(APPLICATION_JSON_VALUE)

                .when()
                .delete("/lines/" + lineResponse.getId() + "/stations/" + newStationResponse.getId())

                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
    }

    private StationResponse createNewStation(final StationCreateRequest stationCreateRequest) {
        return RestAssured
                .given()
                .contentType(APPLICATION_JSON_VALUE)
                .body(stationCreateRequest)

                .when()
                .post("/stations")

                .then()
                .extract()
                .as(StationResponse.class);
    }

    private ExtractableResponse<Response> createNewLine(final LineCreateRequest lineCreateRequest) {
        return RestAssured
                .given()
                .contentType(APPLICATION_JSON_VALUE)
                .body(lineCreateRequest)

                .when()
                .post("/lines")

                .then()
                .extract();
    }
}
