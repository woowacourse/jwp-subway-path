package subway.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.StationRequest;
import subway.dto.StationResponse;

@DisplayName("지하철역 관련 기능")
class StationControllerTest extends ControllerTest {

    private StationRequest stationRequest1;
    private StationRequest stationRequest2;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        stationRequest1 = new StationRequest("강남역");
        stationRequest2 = new StationRequest("역삼역");
    }

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        final ExtractableResponse<Response> response = createStation(stationRequest1);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        // given
        createStation(stationRequest1);

        // when
        final ExtractableResponse<Response> response = createStation(stationRequest1);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void getStations() {
        // given
        final Long stationId1 = Long.parseLong(
                createStation(stationRequest1).header("Location").split("/")[2]
        );

        final Long stationId2 = Long.parseLong(
                createStation(stationRequest2).header("Location").split("/")[2]
        );

        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final List<Long> resultStationIds = response.jsonPath().getList("id", Long.class);
        assertThat(resultStationIds).containsExactly(stationId1, stationId2);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStation() {
        /// given
        final ExtractableResponse<Response> createResponse = createStation(stationRequest1);

        // when
        final Long stationId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/stations/{stationId}", stationId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final StationResponse result = response.as(StationResponse.class);
        assertThat(result.getId()).isEqualTo(stationId);
    }

    @DisplayName("지하철역을 수정한다.")
    @Test
    void updateStation() {
        // given
        final ExtractableResponse<Response> createResponse = createStation(stationRequest1);

        // when
        final String uri = createResponse.header("Location");
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRequest2)
                .when().put(uri)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        final ExtractableResponse<Response> createResponse = createStation(stationRequest1);

        // when
        final String uri = createResponse.header("Location");
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete(uri)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> createStation(final StationRequest stationRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRequest)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }
}
