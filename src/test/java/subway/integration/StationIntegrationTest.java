package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.controller.dto.response.StationResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철역 관련 기능")
public class StationIntegrationTest extends IntegrationTest {

    @Test
    @DisplayName("지하철역을 생성한다.")
    void createStation() {
        // given
        final Map<String, String> params = Map.of("name", "강남역");

        // when
        final ExtractableResponse<Response> response = RestAssured.given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then()
                .extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank()
        );
    }

    @Test
    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    void createStationWithDuplicateName() {
        // given
        final Map<String, String> params = Map.of("name", "강남역");
        RestAssured.given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then()
                .extract();

        // when
        final ExtractableResponse<Response> response = RestAssured.given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("지하철역 목록을 조회한다.")
    void getStations() {
        /// given
        final Map<String, String> params1 = Map.of("name", "강남역");
        final ExtractableResponse<Response> createResponse1 = RestAssured.given()
                .body(params1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then()
                .extract();

        final Map<String, String> params2 = Map.of("name", "역삼역");
        final ExtractableResponse<Response> createResponse2 = RestAssured.given()
                .body(params2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then()
                .extract();

        // when
        final ExtractableResponse<Response> response = RestAssured.given()
                .when()
                .get("/stations")
                .then()
                .extract();

        // then
        final List<Long> expectedStationIds = Stream.of(createResponse1, createResponse2)
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());
        final List<Long> resultStationIds = response.jsonPath().getList(".", StationResponse.class).stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
        final List<String> resultStationNames = response.jsonPath().getList(".", StationResponse.class).stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(resultStationIds).containsAll(expectedStationIds),
                () -> assertThat(resultStationNames).containsAll(List.of("강남역", "역삼역"))
        );
    }

    @Test
    @DisplayName("지하철역을 조회한다.")
    void getStation() {
        /// given
        final Map<String, String> params = Map.of("name", "강남역");
        final ExtractableResponse<Response> createResponse = RestAssured.given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then()
                .extract();

        // when
        final long stationId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        final ExtractableResponse<Response> response = RestAssured.given()
                .when()
                .get("/stations/{stationId}", stationId)
                .then()
                .extract();

        // then
        final StationResponse stationResponse = response.as(StationResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(stationResponse.getId()).isEqualTo(stationId),
                () -> assertThat(stationResponse.getName()).isEqualTo("강남역")
        );
    }

    @Test
    @DisplayName("존재하지 않는 id의 지하철역을 조회한다.")
    void getStationWithInvalidId() {
        /// given
        final long stationId = 100L;

        // when
        final ExtractableResponse<Response> response = RestAssured.given()
                .when()
                .get("/stations/{stationId}", stationId)
                .then()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("지하철역을 수정한다.")
    void updateStation() {
        // given
        final Map<String, String> params = Map.of("name", "강남역");
        final ExtractableResponse<Response> createResponse = RestAssured.given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then()
                .extract();

        // when
        final Map<String, String> otherParams = Map.of("name", "삼성역");
        final String uri = createResponse.header("Location");
        final ExtractableResponse<Response> response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(otherParams)
                .when()
                .put(uri)
                .then()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("존재하지 않는 id의 지하철역을 수정한다.")
    void updateStationWithInvalidId() {
        /// given
        final long stationId = 100L;
        final Map<String, String> params = Map.of("name", "강남역");

        // when
        final ExtractableResponse<Response> response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when()
                .put("/stations/{stationId}", stationId)
                .then()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("지하철역을 제거한다.")
    void deleteStation() {
        // given
        final Map<String, String> params = Map.of("name", "강남역");
        final ExtractableResponse<Response> createResponse = RestAssured.given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then()
                .extract();

        // when
        final String uri = createResponse.header("Location");
        final ExtractableResponse<Response> response = RestAssured.given()
                .when()
                .delete(uri)
                .then()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("존재하지 않는 id의 지하철역을 삭제한다.")
    void deleteStationWithInvalidId() {
        /// given
        final long stationId = 100L;
        final Map<String, String> params = Map.of("name", "강남역");

        // when
        final ExtractableResponse<Response> response = RestAssured.given()
                .when()
                .delete("stations/{stationId}", stationId)
                .then()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
