package subway.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.PathRequest;
import subway.dto.PathResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@DisplayName("경로 관련 기능")
public class PathIntegrationTest extends IntegrationTest {
    
    @DisplayName("출발역과 도착역의 최단 거리 경로와 거리 정보를 응답한다")
    @Test
    void 출발역_도착역_최단_거리_정보_조회() {
        PathRequest pathRequest = new PathRequest(1L, 5L);

        given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pathRequest)

        .when()
                .get("/paths")

        .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("distance", equalTo(40))
                .body("charge", equalTo(1850))
                .body("paths[0].upStation.id", equalTo(1))
                .body("paths[0].upStation.name", equalTo("잠실새내"))
                .body("paths[0].downStation.id", equalTo(2))
                .body("paths[0].downStation.name", equalTo("잠실"))
                .body("paths[0].line.id", equalTo(1))
                .body("paths[0].line.name", equalTo("2호선"))
                .body("paths[0].line.color", equalTo("초록"))
                .body("paths[0].distance", equalTo(10))
                .body("paths[1].upStation.id", equalTo(2))
                .body("paths[1].upStation.name", equalTo("잠실"))
                .body("paths[1].downStation.id", equalTo(3))
                .body("paths[1].downStation.name", equalTo("잠실나루"))
                .body("paths[1].line.id", equalTo(1))
                .body("paths[1].line.name", equalTo("2호선"))
                .body("paths[1].line.color", equalTo("초록"))
                .body("paths[1].distance", equalTo(15))
                .body("paths[2].upStation.id", equalTo(3))
                .body("paths[2].upStation.name", equalTo("잠실나루"))
                .body("paths[2].downStation.id", equalTo(5))
                .body("paths[2].downStation.name", equalTo("석촌"))
                .body("paths[2].line.id", equalTo(2))
                .body("paths[2].line.name", equalTo("8호선"))
                .body("paths[2].line.color", equalTo("파랑"))
                .body("paths[2].distance", equalTo(15))
                .log().all();
    }
}
