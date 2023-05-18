package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static subway.common.fixture.WebFixture.후추_요청;
import static subway.common.step.StationStep.createStation;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class StationIntegrationTest extends IntegrationTest {

    @Test
    void 지하철역을_생성한다() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(후추_요청)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            softly.assertThat(response.header("Location")).isNotBlank();
        });
    }

    @Test
    void 동일한_이름의_역을_생성하면_예외를_던진다() {
        // given
        createStation(후추_요청);

        // when
        ExtractableResponse<Response> response = createStation(후추_요청);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            softly.assertThat(response.body().asString()).isEqualTo("이미 존재하는 역입니다.");
        });
    }
}
