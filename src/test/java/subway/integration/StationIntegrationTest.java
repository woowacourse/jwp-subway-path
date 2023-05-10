package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.CreationStationRequest;
import subway.dto.ReadStationResponse;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("지하철역 관련 기능")
public class StationIntegrationTest extends IntegrationTest {

    @Test
    void 지하철역을_생성한다() {
        // given
        final CreationStationRequest requestDto =  CreationStationRequest.from("강남역");

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(requestDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            softAssertions.assertThat(response.header("Location")).isNotBlank();
        });
    }

    @Test
    void 기존에_존재하는_지하철역_이름으로_지하철역을_생성하면_상태코드_400을_반환한다() {
        // given
        final CreationStationRequest requestDto =  CreationStationRequest.from("양재역");

        RestAssured.given().log().all()
                .body(requestDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(requestDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then()
                .log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 지하철역을_조회한다() {
        /// given
        final CreationStationRequest requestDto =  CreationStationRequest.from("선릉역");
        final ExtractableResponse<Response> createResponse = RestAssured.given().log().all()
                .body(requestDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        // when
        final Long stationId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get("/stations/{stationId}", stationId)
                .then().log().all()
                .extract();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            final ReadStationResponse readStationResponse = response.as(ReadStationResponse.class);
            softAssertions.assertThat(readStationResponse.getId()).isEqualTo(stationId);
        });
    }

    @Test
    void 지하철역을_제거한다() {
        // given
        final CreationStationRequest requestDto =  CreationStationRequest.from("잠실역");
        final ExtractableResponse<Response> createResponse = RestAssured.given().log().all()
                .body(requestDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        // when
        final String uri = createResponse.header("Location");
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
