package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.ui.dto.request.AddStationRequest;

@Sql({"/data-initial.sql"})
@SuppressWarnings("NonAsciiCharacters")
public class StationIntegrationTest extends IntegrationTest {

    private static final Long FRONT_STATION_ID = 1L;
    private static final Long SECOND_STATION_ID = 2L;
    private static final Long END_STATION_ID = 3L;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 역과_역_사이에_새로운_역_등록_API_테스트() throws JsonProcessingException {
        // given
        final AddStationRequest request = new AddStationRequest(
                FRONT_STATION_ID,
                SECOND_STATION_ID,
                "잠실",
                1
        );

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsBytes(request))
                .when().post("/lines/{lineId}/station", 1L)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header(HttpHeaders.CONTENT_LOCATION)).isNotBlank();
    }

    @Test
    void 상행_종점에_역_추가_API_테스트() throws JsonProcessingException {
        // given
        final AddStationRequest request = new AddStationRequest(
                null,
                FRONT_STATION_ID,
                "교대",
                5
        );

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsBytes(request))
                .when().post("/lines/{lineId}/station", 1L)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header(HttpHeaders.CONTENT_LOCATION)).isNotBlank();
    }

    @Test
    void 하행_종점에_역_추가_API_테스트() throws JsonProcessingException {
        // given
        final AddStationRequest request = new AddStationRequest(
                END_STATION_ID,
                null,
                "삼성",
                3
        );

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsBytes(request))
                .when().post("/lines/{lineId}/station", 1L)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header(HttpHeaders.CONTENT_LOCATION)).isNotBlank();
    }

    @Test
    void 상행_종점_역_삭제_API_테스트() {
        // given
        final Long removalStationId = 1L;

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/lines/{lineId}/stations/{stationId}", 1L, removalStationId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 하행_종점_역_삭제_API_테스트() {
        // given
        final Long removalStationId = 3L;

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/lines/{lineId}/stations/{stationId}", 1L, removalStationId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 역과_역_사이_역_삭제_API_테스트() {
        // given
        final Long removalStationId = 2L;

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/lines/{lineId}/stations/{stationId}", 1L, removalStationId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
