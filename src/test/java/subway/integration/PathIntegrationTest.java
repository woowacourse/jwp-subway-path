package subway.integration;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.controller.dto.request.LineRequest;
import subway.controller.dto.request.ShortestPathRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("경로 관련 기능")
public class PathIntegrationTest extends IntegrationTest {

    @Nested
    @DisplayName("최단 경로를 조회한다.")
    class findShortestPath {

        @Test
        @DisplayName("성공 - 10km 이내 기본 운임")
        void success_under_10km() {
            // given
            final ShortestPathRequest request = new ShortestPathRequest("광명사거리", "가산디지털단지");

            // when
            final ExtractableResponse<Response> response = RestAssured
                    .given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().get("/path")
                    .then()
                    .extract();

            // then
            final Configuration conf = Configuration.defaultConfiguration();
            final DocumentContext documentContext = JsonPath.using(conf).parse(response.asString());

            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                    () -> assertThat(documentContext.read("$.distance", Integer.class)).isEqualTo(8),
                    () -> assertThat(documentContext.read("$.fee", Integer.class)).isEqualTo(1250),
                    () -> assertThat(documentContext.read("$.route.size()", Integer.class)).isEqualTo(3),
                    () -> assertThat(documentContext.read("$.route[0].name", String.class)).isEqualTo("광명사거리"),
                    () -> assertThat(documentContext.read("$.route[1].name", String.class)).isEqualTo("철산"),
                    () -> assertThat(documentContext.read("$.route[2].name", String.class)).isEqualTo("가산디지털단지")
            );
        }
    }
}
