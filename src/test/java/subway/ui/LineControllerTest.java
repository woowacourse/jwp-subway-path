package subway.ui;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineAndStationsResponse;
import subway.dto.StationAddRequest;
import subway.integration.IntegrationTest;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static subway.fixture.WebFixture.*;
import static subway.ui.LineStep.*;
import static subway.ui.StationStep.createStation;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LineControllerTest extends IntegrationTest {

    @Test
    void 모든_호선의_역을_조회한다() {
        //given
        final Long lineId = createLine(일호선_남색_요청);
        final Long fromStationId = createStation(후추_요청);
        final Long toStationId = createStation(디노_요청);
        addStationToLine(new StationAddRequest(fromStationId, toStationId, 7), lineId);

        //when
        final ExtractableResponse<Response> response = given().log().all()
                .when()
                .get("/lines/stations")
                .then().log().all()
                .extract();

        //then
        final List<LineAndStationsResponse> lineAndStationsResponses = response.as(new TypeRef<List<LineAndStationsResponse>>() {
        });
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            final LineAndStationsResponse lineAndStationsResponse = lineAndStationsResponses.get(0);
            softly.assertThat(lineAndStationsResponse.getLineResponse().getId()).isEqualTo(1L);
            softly.assertThat(lineAndStationsResponse.getLineResponse().getName()).isEqualTo("일호선");
            softly.assertThat(lineAndStationsResponse.getLineResponse().getColor()).isEqualTo("남색");
            softly.assertThat(lineAndStationsResponse.getStationResponses().get(0).getId()).isEqualTo(1L);
            softly.assertThat(lineAndStationsResponse.getStationResponses().get(0).getName()).isEqualTo("후추");
            softly.assertThat(lineAndStationsResponse.getStationResponses().get(1).getId()).isEqualTo(2L);
            softly.assertThat(lineAndStationsResponse.getStationResponses().get(1).getName()).isEqualTo("디노");
        });
    }

    @Test
    void 호선의_역을_조회한다() {
        //given
        final Long lineId = createLine(일호선_남색_요청);
        final Long fromStationId = createStation(후추_요청);
        final Long toStationId = createStation(디노_요청);
        addStationToLine(new StationAddRequest(fromStationId, toStationId, 7), lineId);

        //when
        final ExtractableResponse<Response> response = given().log().all()
                .pathParam("lineId", lineId)
                .when()
                .get("/lines/{lineId}/stations")
                .then().log().all()
                .extract();

        //then
        final LineAndStationsResponse lineAndStationsResponse = response.as(LineAndStationsResponse.class);
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(lineAndStationsResponse.getLineResponse().getId()).isEqualTo(1L);
            softly.assertThat(lineAndStationsResponse.getLineResponse().getName()).isEqualTo("일호선");
            softly.assertThat(lineAndStationsResponse.getLineResponse().getColor()).isEqualTo("남색");
            softly.assertThat(lineAndStationsResponse.getStationResponses().get(0).getId()).isEqualTo(1L);
            softly.assertThat(lineAndStationsResponse.getStationResponses().get(0).getName()).isEqualTo("후추");
            softly.assertThat(lineAndStationsResponse.getStationResponses().get(1).getId()).isEqualTo(2L);
            softly.assertThat(lineAndStationsResponse.getStationResponses().get(1).getName()).isEqualTo("디노");
        });
    }

    @Test
    void 호선의_역을_제거한다() {
        //given
        final Long lineId = createLine(일호선_남색_요청);
        final Long 후추_id = createStation(후추_요청);
        final Long 디노_id = createStation(디노_요청);
        final Long 조앤_id = createStation(조앤_요청);
        addStationToLine(new StationAddRequest(후추_id, 디노_id, 7), lineId);
        addStationToLine(new StationAddRequest(디노_id, 조앤_id, 4), lineId);

        //when
        final ExtractableResponse<Response> response = given().log().all()
                .pathParam("lineId", lineId)
                .pathParam("stationId", 디노_id)
                .when()
                .delete("/lines/{lineId}/stations/{stationId}")
                .then().log().all()
                .extract();

        //then
        final LineAndStationsResponse lineAndStationsResponse = findStationsByLineId(lineId).as(LineAndStationsResponse.class);
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(lineAndStationsResponse.getStationResponses()).hasSize(2);
            softly.assertThat(doesNotContain(lineAndStationsResponse, "디노")).isTrue();
        });
    }

    private boolean doesNotContain(final LineAndStationsResponse lineAndStationsResponse, final String stationName) {
        return lineAndStationsResponse.getStationResponses().stream()
                .noneMatch(stationResponse -> stationName.equals(stationResponse.getName()));
    }

    @Test
    void 호선에_역을_추가한다() {
        //given
        final Long lineId = createLine(일호선_남색_요청);
        final Long 후추_id = createStation(후추_요청);
        final Long 디노_id = createStation(디노_요청);

        //when
        final ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("lineId", lineId)
                .body(new StationAddRequest(후추_id, 디노_id, 7))
                .when()
                .post("/lines/{lineId}/stations")
                .then().log().all()
                .extract();

        //then
        final LineAndStationsResponse lineAndStationsResponse = findStationsByLineId(lineId).as(LineAndStationsResponse.class);
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(lineAndStationsResponse.getLineResponse().getId()).isEqualTo(1L);
            softly.assertThat(lineAndStationsResponse.getLineResponse().getName()).isEqualTo("일호선");
            softly.assertThat(lineAndStationsResponse.getLineResponse().getColor()).isEqualTo("남색");
            softly.assertThat(lineAndStationsResponse.getStationResponses().get(0).getId()).isEqualTo(1L);
            softly.assertThat(lineAndStationsResponse.getStationResponses().get(0).getName()).isEqualTo("후추");
            softly.assertThat(lineAndStationsResponse.getStationResponses().get(1).getId()).isEqualTo(2L);
            softly.assertThat(lineAndStationsResponse.getStationResponses().get(1).getName()).isEqualTo("디노");
        });
    }

}
