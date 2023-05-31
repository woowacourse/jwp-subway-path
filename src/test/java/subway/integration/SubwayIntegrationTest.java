package subway.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import subway.dto.PathRequest;

import static org.hamcrest.Matchers.*;

@ActiveProfiles("test")
@DisplayName("지하철 경로 관련 기능")
class SubwayIntegrationTest extends IntegrationTest {

    @DisplayName("서울역과 종로 3가 사이의 최단 경로와 거리, 요금을 찾는다")
    @Test
    void findPathResponseFromSeoulyeokToJongrosamga() {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new PathRequest(1L, 5L))
                .when().get("/subway/path")
                .then().log().all()
                .statusCode(200)
                .body("sections", hasSize(8))
                .body("sections.line", hasItems(1, 2, 3, 4))
                .body("sections.from", hasItems("서울역", "신도림", "강변", "구의", "약수", "옥수", "이수", "종각"))
                .body("sections.to", hasItems("신도림", "강변", "구의", "약수", "옥수", "이수", "종각", "종로3가"))
                .body("sections.distance", hasItems(30, 5))
                .body("wholeDistance", equalTo(90))
                .body("fare", equalTo(2550));
    }

    @DisplayName("존재하지 않는 역에 대한 경로 요청 시 예외를 반환한다")
    @Test
    void throwExceptionWhenFindPathToNotExistStation() {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new PathRequest(1L, 17L))
                .when().get("/subway/path")
                .then().log().all()
                .statusCode(400);
    }
}
