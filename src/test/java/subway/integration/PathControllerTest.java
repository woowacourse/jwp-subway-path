package subway.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.request.PathRequest;
import subway.dto.response.PathResponse;
import subway.dto.response.StationResponse;

@Sql("/integration-data.sql")
class PathControllerTest extends IntegrationTest {

    @Test
    void 출발역과_도착역을_받아_총_경로와_총_거리를_구한다() {
        final PathRequest pathRequest = new PathRequest(4L, 3L);

        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pathRequest)
                .when().post("/path")
                .then().log().all().extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getObject(".", PathResponse.class))
                        .usingRecursiveComparison()
                        .isEqualTo(new PathResponse(
                                List.of(
                                        new StationResponse(4L, "강남"),
                                        new StationResponse(2L, "잠실새내"),
                                        new StationResponse(3L, "잠실")
                                ), 1250, 10
                        ))
        );
    }

}
