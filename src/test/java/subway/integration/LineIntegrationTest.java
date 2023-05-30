package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.ui.dto.request.CreationLineRequest;
import subway.ui.dto.request.CreationSectionRequest;
import subway.ui.dto.request.CreationStationRequest;
import subway.ui.dto.response.ReadLineResponse;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.fixtures.request.CreationLineRequestFixture.FIVE_LINE_REQUEST;
import static subway.fixtures.request.CreationStationRequestFixture.JAMSIL_REQUEST;
import static subway.fixtures.request.CreationStationRequestFixture.SAMSUNG_REQUEST;
import static subway.fixtures.request.CreationStationRequestFixture.SEOLLEUNG_REQUEST;

@DisplayName("지하철 노선 관련 기능")
@SuppressWarnings("NonAsciiCharacters")
public class LineIntegrationTest extends IntegrationTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    void 지하철_노선을_생성한다() {
        // given
        final CreationLineRequest lineRequest = CreationLineRequest.of("신분당선", "bg-red-600");

        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/lines")
                .then().log().all()
                .extract();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(response.header("Location")).isNotBlank();
        });
    }

    @Test
    void 기존에_존재하는_지하철_노선_이름으로_지하철_노선을_생성한다() {
        // given
        final CreationLineRequest lineRequest = CreationLineRequest.of("경의중앙선", "bg-red-600");

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/lines")
                .then().log().all()
                .extract();

        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/lines")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 지하철_노선_목록을_조회한다() {
        // given
        final CreationLineRequest lineRequest1 = CreationLineRequest.of("2호선", "bg-red-600");
        final CreationLineRequest lineRequest2 = CreationLineRequest.of("3호선", "bg-red-600");

        final ExtractableResponse<Response> createResponse1 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        final ExtractableResponse<Response> createResponse2 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest2)
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            final List<Long> expectedLineIds = Stream.of(createResponse1, createResponse2)
                    .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                    .collect(Collectors.toList());
            final List<Long> resultLineIds = response.jsonPath().getList(".", ReadLineResponse.class).stream()
                    .map(ReadLineResponse::getId)
                    .collect(Collectors.toList());
            assertThat(resultLineIds).containsAll(expectedLineIds);
        });
    }

    @Test
    void 지하철_노선을_조회한다() {
        // given
        final CreationLineRequest lineRequest = CreationLineRequest.of("4호선", "bg-red-600");

        final ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        final Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            ReadLineResponse resultResponse = response.as(ReadLineResponse.class);
            assertThat(resultResponse.getId()).isEqualTo(lineId);
        });
    }

    @Test
    void 지하철_노선을_제거한다() {
        // given
        final CreationLineRequest lineRequest = CreationLineRequest.of("5호선", "bg-red-600");
        final ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        final Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Nested
    class 지하철_노선_구간_추가_테스트 {

        Long lineId;
        Long stationOneId;
        Long stationTwoId;
        Long stationThreeId;

        @BeforeEach
        void 초기_노선_역_데이터_추가() {
            final ExtractableResponse<Response> createLineResponse = getLineResponseAfterPost(FIVE_LINE_REQUEST);
            final ExtractableResponse<Response> createStationResponseOne = getStationResponseAfterPost(JAMSIL_REQUEST);
            final ExtractableResponse<Response> createStationResponseTwo = getStationResponseAfterPost(SEOLLEUNG_REQUEST);
            final ExtractableResponse<Response> createStationResponseThree = getStationResponseAfterPost(SAMSUNG_REQUEST);

            lineId = Long.parseLong(createLineResponse.header("Location").split("/")[2]);
            stationOneId = Long.parseLong(createStationResponseOne.header("Location").split("/")[2]);
            stationTwoId = Long.parseLong(createStationResponseTwo.header("Location").split("/")[2]);
            stationThreeId = Long.parseLong(createStationResponseThree.header("Location").split("/")[2]);
        }

        @Test
        void 지하철_노선에_첫_구간을_추가하다() {
            // when
            final CreationSectionRequest sectionRequest = CreationSectionRequest.of(stationOneId, stationTwoId, 10);
            final ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .body(sectionRequest)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().post("/lines/{lineId}/sections", lineId)
                    .then().log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        }

        @Test
        void 지하철_노선에_새로운_상행_종점을_만드는_구간을_추가하다() {
            // given
            final CreationSectionRequest sectionRequest = CreationSectionRequest.of(stationOneId, stationTwoId, 10);
            RestAssured
                    .given().log().all()
                    .body(sectionRequest)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().post("/lines/{lineId}/sections", lineId)
                    .then().log().all()
                    .extract();

            // when
            final CreationSectionRequest sectionRequestTwo = CreationSectionRequest.of(stationThreeId, stationOneId, 10);
            final ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .body(sectionRequestTwo)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().post("/lines/{lineId}/sections", lineId)
                    .then().log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        }

        @Test
        void 지하철_노선에_새로운_하행_종점을_만드는_구간을_추가하다() {
            // given
            final CreationSectionRequest sectionRequest = CreationSectionRequest.of(stationOneId, stationTwoId, 10);
            RestAssured
                    .given().log().all()
                    .body(sectionRequest)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().post("/lines/{lineId}/sections", lineId)
                    .then().log().all()
                    .extract();

            // when
            final CreationSectionRequest sectionRequestTwo = CreationSectionRequest.of(stationTwoId, stationThreeId, 10);
            final ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .body(sectionRequestTwo)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().post("/lines/{lineId}/sections", lineId)
                    .then().log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        }

        @Test
        void 지하철_노선에_역과_역_사이에_구간을_추가하다() {
            // given
            final CreationSectionRequest sectionRequest = CreationSectionRequest.of(stationOneId, stationTwoId, 10);
            RestAssured
                    .given().log().all()
                    .body(sectionRequest)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().post("/lines/{lineId}/sections", lineId)
                    .then().log().all()
                    .extract();

            // when
            final CreationSectionRequest sectionRequestTwo = CreationSectionRequest.of(stationOneId, stationThreeId, 5);
            final ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .body(sectionRequestTwo)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().post("/lines/{lineId}/sections", lineId)
                    .then().log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        }
    }

    @Nested
    class 지하철_노선_구간_삭제_테스트 {

        Long lineId;
        Long stationOneId;
        Long stationTwoId;
        Long stationThreeId;

        @BeforeEach
        void 초기_노선_역_데이터_추가() {
            final ExtractableResponse<Response> createLineResponse = getLineResponseAfterPost(FIVE_LINE_REQUEST);
            final ExtractableResponse<Response> createStationResponseOne = getStationResponseAfterPost(JAMSIL_REQUEST);
            final ExtractableResponse<Response> createStationResponseTwo = getStationResponseAfterPost(SEOLLEUNG_REQUEST);
            final ExtractableResponse<Response> createStationResponseThree = getStationResponseAfterPost(SAMSUNG_REQUEST);

            lineId = Long.parseLong(createLineResponse.header("Location").split("/")[2]);
            stationOneId = Long.parseLong(createStationResponseOne.header("Location").split("/")[2]);
            stationTwoId = Long.parseLong(createStationResponseTwo.header("Location").split("/")[2]);
            stationThreeId = Long.parseLong(createStationResponseThree.header("Location").split("/")[2]);
        }

        @Test
        void 지하철_노선에_종점_지하철_역을_삭제하다() {
            // given
            final CreationSectionRequest sectionRequest = CreationSectionRequest.of(stationOneId, stationTwoId, 10);
            RestAssured
                    .given().log().all()
                    .body(sectionRequest)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().post("/lines/{lineId}/sections", lineId)
                    .then().log().all()
                    .extract();

            // when
            final ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .when().delete("/lines/{lineId}/stations/{stationId}", lineId, stationOneId)
                    .then().log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        }

        @Test
        void 지하철_노선에_중간_역을_삭제하다() {
            // given
            final CreationSectionRequest sectionRequestOne = CreationSectionRequest.of(stationOneId, stationTwoId, 10);
            RestAssured
                    .given().log().all()
                    .body(sectionRequestOne)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().post("/lines/{lineId}/sections", lineId)
                    .then().log().all()
                    .extract();

            final CreationSectionRequest sectionRequestTwo = CreationSectionRequest.of(stationTwoId, stationThreeId, 10);
            RestAssured
                    .given().log().all()
                    .body(sectionRequestTwo)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().post("/lines/{lineId}/sections", lineId)
                    .then().log().all()
                    .extract();


            // when
            final ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .when().delete("/lines/{lineId}/stations/{stationId}", lineId, stationTwoId)
                    .then().log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        }
    }

    private ExtractableResponse<Response> getLineResponseAfterPost(final CreationLineRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/lines")
                .then().log().all().
                extract();
    }

    private ExtractableResponse<Response> getStationResponseAfterPost(final CreationStationRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/stations")
                .then().log().all().
                extract();
    }
}
