package subway.integration.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;

@SuppressWarnings("NonAsciiCharacters")
public class LineStep {
    public static Long 노선을_생성한다(LineRequest lineRequest) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        String location = response.header("Location");
        return Long.parseLong(location.substring(location.lastIndexOf("/") + 1));
    }
}
