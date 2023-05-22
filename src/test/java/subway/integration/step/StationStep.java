package subway.integration.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.springframework.http.MediaType;
import subway.dto.StationCreateRequest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class StationStep {
    public static ExtractableResponse<Response> 역_생성_요청(final String 역_이름) {
        StationCreateRequest 요청 = new StationCreateRequest(역_이름);
        return RestAssured.given().log().all()
                .body(요청)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 역_조회_요청(final Long 역_ID) {
        return RestAssured.given().log().all()
                .when().get("/stations/" + 역_ID)
                .then().log().all()
                .extract();
    }
}
