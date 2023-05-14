package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class LineIntegrationTest extends IntegrationTest {

    @Nested
    public class 노선을_추가할_때 {

        @Test
        void 정상적으로_추가된다() {
            // given
            ExtractableResponse<Response> response = 노선_생성_요청("2호선", "강남역", "역삼역", 10);

            // expect
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(response.header("Location")).isNotEmpty();
        }

        @Test
        void 이미_존재하는_노선의_이름을_추가하면_오류가_발생한다() {
            노선_생성_요청("2호선", "강남역", "역삼역", 10);

            ExtractableResponse<Response> response = 노선_생성_요청("2호선", "역삼역", "잠실역", 10);

            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }

    private static ExtractableResponse<Response> 노선_생성_요청(String lineName, String source, String target, int distance) {
        LineRequest request = new LineRequest(lineName, source, target, distance);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }
}
