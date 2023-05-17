package subway.integration.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.springframework.http.MediaType;
import subway.dto.SectionCreateRequest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class SectionStep {
    public static ExtractableResponse<Response> 구간_생성_요청(final SectionCreateRequest 구간_생성_요청, final Long 라인_ID) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(구간_생성_요청)
                .when()
                .post("/lines/" + 라인_ID + "/sections")
                .then().log().all()
                .extract();
    }

    public static SectionCreateRequest 구간_생성_요청을_생성한다(
            final Long 시작역_ID,
            final Long 도착역_ID,
            final int 거리
    ) {
        return new SectionCreateRequest(시작역_ID, 도착역_ID, 거리);
    }
}
