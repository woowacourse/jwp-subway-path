package subway.integration.step;

import io.restassured.RestAssured;
import org.springframework.http.MediaType;
import subway.dto.SectionCreateRequest;

@SuppressWarnings("NonAsciiCharacters")
public class SectionStep {
    public static void 구간을_생성한다(SectionCreateRequest sectionCreateRequest) {
        RestAssured
                .given().log().all()
                .body(sectionCreateRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();
    }
}
