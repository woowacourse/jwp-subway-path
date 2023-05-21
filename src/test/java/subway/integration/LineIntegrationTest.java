package subway.integration;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.dto.DistanceResponse;
import subway.dto.FareResponse;
import subway.dto.LineAndSectionsResponse;
import subway.dto.LineAndStationsResponse;
import subway.dto.PathRequest;
import subway.dto.PathResponse;
import subway.dto.StationAddRequest;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static subway.common.fixture.DomainFixture.디노;
import static subway.common.fixture.DomainFixture.로운;
import static subway.common.fixture.DomainFixture.조앤;
import static subway.common.fixture.DomainFixture.후추;
import static subway.common.fixture.WebFixture.디노_요청;
import static subway.common.fixture.WebFixture.로운_요청;
import static subway.common.fixture.WebFixture.이호선_초록색_요청;
import static subway.common.fixture.WebFixture.일호선_남색_요청;
import static subway.common.fixture.WebFixture.조앤_요청;
import static subway.common.fixture.WebFixture.후추_요청;
import static subway.common.step.LineStep.addStationToLine;
import static subway.common.step.LineStep.createLine;
import static subway.common.step.LineStep.createLineAndGetId;
import static subway.common.step.LineStep.findStationsByLineId;
import static subway.common.step.StationStep.createStationAndGetId;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class LineIntegrationTest extends IntegrationTest {

    @Test
    void 지하철_노선을_생성한다() {
        // when
        final ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(일호선_남색_요청)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            softly.assertThat(response.header("Location")).isNotBlank();
        });
    }

    @Test
    void 동일한_이름의_노선을_생성하면_예외를_던진다() {
        // given
        createLine(일호선_남색_요청);

        // when
        final ExtractableResponse<Response> response = createLine(일호선_남색_요청);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            softly.assertThat(response.body().asString()).isEqualTo("이미 존재하는 호선입니다.");
        });
    }


    @Test
    void 호선의_역을_조회한다() {
        //given
        final Long lineId = createLineAndGetId(일호선_남색_요청);
        final Long fromStationId = createStationAndGetId(후추_요청);
        final Long toStationId = createStationAndGetId(디노_요청);
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
    void 모든_호선의_역을_조회한다() {
        //given
        final Long lineId = createLineAndGetId(일호선_남색_요청);
        final Long fromStationId = createStationAndGetId(후추_요청);
        final Long toStationId = createStationAndGetId(디노_요청);
        addStationToLine(new StationAddRequest(fromStationId, toStationId, 7), lineId);

        //when
        final ExtractableResponse<Response> response = given().log().all()
                .when()
                .get("/lines/stations")
                .then().log().all()
                .extract();

        //then
        final List<LineAndStationsResponse> lineAndStationsResponses = response.as(new TypeRef<>() {
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
    void 호선의_역을_제거한다() {
        //given
        final Long lineId = createLineAndGetId(일호선_남색_요청);
        final Long 후추_id = createStationAndGetId(후추_요청);
        final Long 디노_id = createStationAndGetId(디노_요청);
        final Long 조앤_id = createStationAndGetId(조앤_요청);
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
        final Long lineId = createLineAndGetId(일호선_남색_요청);
        final Long 후추_id = createStationAndGetId(후추_요청);
        final Long 디노_id = createStationAndGetId(디노_요청);

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

    @Test
    void 최단_경로를_반환한다() {
        //given
        final Long 일호선_id = createLineAndGetId(일호선_남색_요청);
        final Long 이호선_id = createLineAndGetId(이호선_초록색_요청);

        final Long 후추_id = createStationAndGetId(후추_요청);
        final Long 디노_id = createStationAndGetId(디노_요청);
        final Long 조앤_id = createStationAndGetId(조앤_요청);
        final Long 로운_id = createStationAndGetId(로운_요청);

        addStationToLine(new StationAddRequest(후추_id, 디노_id, 7), 일호선_id);
        addStationToLine(new StationAddRequest(디노_id, 조앤_id, 5), 일호선_id);
        addStationToLine(new StationAddRequest(디노_id, 로운_id, 3), 이호선_id);

        final PathRequest pathRequest = new PathRequest(후추_id, 로운_id);

        //when
        final ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pathRequest)
                .when()
                .get("/routes/shortest")
                .then().log().all()
                .extract();

        //then
        final PathResponse pathResponse = response.as(PathResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(pathResponse).usingRecursiveComparison()
                    .isEqualTo(new PathResponse(
                            List.of(
                                    LineAndSectionsResponse.of(
                                            new Line(일호선_id, "일호선", "남색", List.of(new Section(1L, 후추, 디노, 7), new Section(2L, 디노, 조앤, 5))),
                                            List.of(new Section(1L, 후추, 디노, 7))
                                    ),
                                    LineAndSectionsResponse.of(
                                            new Line(이호선_id, "이호선", "초록색", List.of(new Section(3L, 디노, 로운, 3))),
                                            List.of(new Section(3L, 디노, 로운, 3))
                                    )
                            ),
                            new DistanceResponse(10),
                            new FareResponse(1250)
                    ));
        });
    }
}
