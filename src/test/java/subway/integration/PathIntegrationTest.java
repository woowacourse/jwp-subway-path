package subway.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Transactional
@DisplayName("경로 관련 기능")
public class PathIntegrationTest extends IntegrationTest {

    ObjectMapper objectMapper = new ObjectMapper();
    
    @DisplayName("출발역과 도착역의 최단 거리 경로와 거리 정보를 응답한다")
    @Test
    void 출발역_도착역_최단_거리_정보_조회() throws JsonProcessingException {
        PathRequest pathRequest = new PathRequest(1L, 5L);
        List<SectionResponse> paths = List.of(
                new SectionResponse(new StationResponse(1L, "잠실새내"), new StationResponse(2L, "잠실"), new LineResponse(1L, "2호선", "초록"), 10),
                new SectionResponse(new StationResponse(2L, "잠실"), new StationResponse(3L, "잠실나루"), new LineResponse(1L, "2호선", "초록"), 15),
                new SectionResponse(new StationResponse(3L, "잠실나루"), new StationResponse(5L, "석촌"), new LineResponse(2L, "8호선", "파랑"), 15)
        );
        PathResponse pathResponse = new PathResponse(40, 1850, paths);


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
