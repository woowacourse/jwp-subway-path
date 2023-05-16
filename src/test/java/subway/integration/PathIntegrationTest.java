package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static subway.integration.IntegrationFixture.OBJECT_MAPPER;
import static subway.integration.IntegrationFixture.SAMSUNG;
import static subway.integration.IntegrationFixture.SEONGLENUG;
import static subway.integration.IntegrationFixture.jsonSerialize;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.service.dto.PathRequest;
import subway.service.dto.PathResponse;

@DisplayName("경로 조회 기능 테스트")
public class PathIntegrationTest extends IntegrationTest {

    @DisplayName("출발점과 도착점으로 경로와 총 거리를 구한다.")
    @Test
    void findPath() throws JsonProcessingException {
        final PathRequest pathRequest = new PathRequest(SAMSUNG.getName(), SEONGLENUG.getName());

        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonSerialize(pathRequest))
                .when().post("/path")
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final PathResponse pathResponse = OBJECT_MAPPER.readValue(response.asString(), PathResponse.class);
        assertThat(pathResponse)
                .extracting(PathResponse::getTotalDistance, PathResponse::getTotalFare)
                .containsExactly(tuple(10, 1250));
    }
}
