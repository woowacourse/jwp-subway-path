package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.StationRequest;
import subway.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationIntegrationTest extends IntegrationTest {
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given
        StationRequest request = new StationRequest("해운대역");

        // when
        ExtractableResponse<Response> createResponse = 지하철_역_생성_요청(request);

        String stationId = parseUri(createResponse.header("Location"));
        ExtractableResponse<Response> response =
                지하철_역_정보_조회(stationId);

        // then
        StationResponse stationResponse = response.as(StationResponse.class);
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(createResponse.header("Location")).isNotBlank();
        assertThat(stationResponse.getName()).isEqualTo("해운대역");
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        // given
        StationRequest request = new StationRequest("강남역");
        ExtractableResponse<Response> response1 = 지하철_역_생성_요청(request);

        // when
        ExtractableResponse<Response> response2 = RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then()
                .log().all()
                .extract();

        // then
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void showStations() {
        /// given
        StationRequest request1 = new StationRequest("동대구역");
        StationRequest request2 = new StationRequest("신천역");
        ExtractableResponse<Response> createResponse1 = 지하철_역_생성_요청(request1);
        ExtractableResponse<Response> createResponse2 = 지하철_역_생성_요청(request2);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when()
                .get("/stations")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Long> expectedStationIds = Stream.of(createResponse1, createResponse2)
                .map(it -> Long.parseLong(parseUri(it.header("Location"))))
                .collect(Collectors.toList());
        List<Long> resultStationIds = response.jsonPath().getList(".", StationResponse.class).stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
        assertThat(resultStationIds).containsAll(expectedStationIds);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void showStation() {
        /// given
        StationRequest request = new StationRequest("동대구역");
        ExtractableResponse<Response> createResponse = 지하철_역_생성_요청(request);

        // when
        String stationId = parseUri(createResponse.header("Location"));
        ExtractableResponse<Response> response = 지하철_역_정보_조회(stationId);

        // then
        StationResponse stationResponse = response.as(StationResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(stationResponse.getName()).isEqualTo("동대구역");
        assertThat(stationResponse.getId()).isEqualTo(Long.parseLong(stationId));
    }

    @DisplayName("지하철역을 수정한다.")
    @Test
    void updateStation() {
        // given
        StationRequest request1 = new StationRequest("동대구역");
        ExtractableResponse<Response> createResponse = 지하철_역_생성_요청(request1);

        // when
        StationRequest request2 = new StationRequest("신천역");
        String stationId = parseUri(createResponse.header("Location"));
        ExtractableResponse<Response> updateResponse =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(request2)
                        .when()
                        .put("/stations/{id}", stationId)
                        .then().log().all()
                        .extract();

        ExtractableResponse<Response> response = 지하철_역_정보_조회(stationId);

        // then
        StationResponse stationResponse = response.as(StationResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(stationResponse.getName()).isEqualTo("신천역");
        assertThat(stationResponse.getId()).isEqualTo(Long.parseLong(stationId));
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        StationRequest request = new StationRequest("동대구역");
        ExtractableResponse<Response> createResponse = 지하철_역_생성_요청(request);

        // when
        String stationId = parseUri(createResponse.header("Location"));
        ExtractableResponse<Response> deleteResponse =
                RestAssured.given().log().all()
                        .when()
                        .delete("/stations/{id}", stationId)
                        .then().log().all()
                        .extract();

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    protected static ExtractableResponse<Response> 지하철_역_생성_요청(final StationRequest stationRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRequest)
                .when().post("/stations")
                .then().extract();
    }

    private ExtractableResponse<Response> 지하철_역_정보_조회(final String stationId) {
        return RestAssured.given()
                .when()
                .get("/stations/{id}", stationId)
                .then()
                .log().all()
                .extract();
    }

    private String parseUri(String uri) {
        String[] parts = uri.split("/");
        return parts[parts.length - 1];
    }
}
