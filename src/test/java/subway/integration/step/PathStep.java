package subway.integration.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.springframework.http.MediaType;
import subway.dto.ShortestPathRequest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class PathStep {
    public static ExtractableResponse<Response> 경로_조회_요청(final ShortestPathRequest 요청) {
        return RestAssured.given().log().all()
                .body(요청)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/path")
                .then().log().all()
                .extract();
    }

    public static ShortestPathRequest 경로_조회_요청을_생성한다(final Long 출발역_ID, final Long 도착역_ID) {
        return new ShortestPathRequest(출발역_ID, 도착역_ID);
    }
}
