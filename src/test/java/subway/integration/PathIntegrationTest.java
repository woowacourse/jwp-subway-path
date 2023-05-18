package subway.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@Sql({"/data-initial.sql"})
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("지하철 노선 관련 기능")
public class PathIntegrationTest extends IntegrationTest {

    @Test
    void 전체_노선_조회_테스트() {
        // when
        final long startStationId = 1L;
        final long endStationId = 4L;
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .param("startStationId", startStationId)
                .param("endStationId", endStationId)
                .when()
                .get("/path")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final Integer distance = response.jsonPath().get("distance");
        assertThat(distance).isNotNull();
        assertThat(distance).isEqualTo(18);

        final Integer fare = response.jsonPath().get("fare");
        assertThat(fare).isNotNull();
        assertThat(fare).isEqualTo(1450);
    }
}
