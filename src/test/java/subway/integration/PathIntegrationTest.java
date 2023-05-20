package subway.integration;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.dto.PathFindingRequest;

@Sql("/InitializeTable.sql")
class PathIntegrationTest extends IntegrationTest {
    @Test
    @DisplayName("최단 경로를 조회한다.")
    void findPathTest() {
        PathFindingRequest pathFindingRequest = new PathFindingRequest(1L, 3L);

        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(pathFindingRequest)
            .when().get("/paths")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
