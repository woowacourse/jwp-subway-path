package subway.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.PathRequest;
import subway.dto.PathResponse;

import java.util.List;

import static io.restassured.RestAssured.given;
import static subway.fixture.SectionFixture.*;

@Transactional
@DisplayName("경로 관련 기능")
public class PathIntegrationTest extends IntegrationTest {

    ObjectMapper objectMapper = new ObjectMapper();
    
    @DisplayName("출발역과 도착역의 최단 거리 경로와 거리 정보를 응답한다")
    @Test
    void 출발역_도착역_최단_거리_정보_조회() throws JsonProcessingException {
        PathRequest pathRequest = new PathRequest(1L, 5L);
        PathResponse pathResponse = new PathResponse(40, 1850, List.of(잠실새내_잠실_응답, 잠실_잠실나루_응답, 잠실나루_석촌_응답));


        Response response = given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pathRequest)

                .when()
                .get("/paths")

                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .extract().response();

        String jsonResponse = response.getBody().asString();
        PathResponse actualResponse = objectMapper.readValue(jsonResponse, PathResponse.class);

        Assertions.assertThat(actualResponse).usingRecursiveComparison().isEqualTo(pathResponse);
    }
}
