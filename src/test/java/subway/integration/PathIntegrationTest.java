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
import org.springframework.test.context.jdbc.Sql;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("경로 관련 기능")
public class PathIntegrationTest extends IntegrationTest {

    @Nested
    @DisplayName("최단 경로를 조회한다.")
    @Sql({"/truncate.sql", "/data.sql"})
    class findShortestPath {

        @Test
        @DisplayName("성공 - 10km 이내 기본 운임")
        void success_under_10km() {
            // given
            final long startStationId = 3L;
            final long endStationId = 5L;

            // when
            final ExtractableResponse<Response> response = RestAssured
                    .given()
                    .params(Map.of("start", startStationId, "end", endStationId))
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
                    () -> assertThat(documentContext.read("$.path.size()", Integer.class)).isEqualTo(3),
                    () -> assertThat(documentContext.read("$.path[0].name", String.class)).isEqualTo("광명사거리"),
                    () -> assertThat(documentContext.read("$.path[1].name", String.class)).isEqualTo("철산"),
                    () -> assertThat(documentContext.read("$.path[2].name", String.class)).isEqualTo("가산디지털단지")
            );
        }

        @Test
        @DisplayName("성공 - 10km ~ 50km 추가 운임")
        @Sql({"/truncate.sql", "/data.sql"})
        void success_10km_to_50km() {
            // given
            final long startStationId = 3L;
            final long endStationId = 12L;

            // when
            final ExtractableResponse<Response> response = RestAssured
                    .given()
                    .params(Map.of("start", startStationId, "end", endStationId))
                    .when().get("/path")
                    .then()
                    .extract();

            // then
            final Configuration conf = Configuration.defaultConfiguration();
            final DocumentContext documentContext = JsonPath.using(conf).parse(response.asString());

            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                    () -> assertThat(documentContext.read("$.distance", Integer.class)).isEqualTo(20),
                    () -> assertThat(documentContext.read("$.fee", Integer.class)).isEqualTo(1450),
                    () -> assertThat(documentContext.read("$.path.size()", Integer.class)).isEqualTo(6),
                    () -> assertThat(documentContext.read("$.path[0].name", String.class)).isEqualTo("광명사거리"),
                    () -> assertThat(documentContext.read("$.path[1].name", String.class)).isEqualTo("철산"),
                    () -> assertThat(documentContext.read("$.path[2].name", String.class)).isEqualTo("가산디지털단지"),
                    () -> assertThat(documentContext.read("$.path[3].name", String.class)).isEqualTo("남구로"),
                    () -> assertThat(documentContext.read("$.path[4].name", String.class)).isEqualTo("대림"),
                    () -> assertThat(documentContext.read("$.path[5].name", String.class)).isEqualTo("신도림")
            );
        }

        @Test
        @DisplayName("성공 - 50km 초과 추가 운임")
        @Sql({"/truncate.sql", "/data.sql"})
        void success_50km_up() {
            // given
            final long startStationId = 1L;
            final long endStationId = 14L;

            // when
            final ExtractableResponse<Response> response = RestAssured
                    .given()
                    .params(Map.of("start", startStationId, "end", endStationId))
                    .when().get("/path")
                    .then()
                    .extract();

            // then
            final Configuration conf = Configuration.defaultConfiguration();
            final DocumentContext documentContext = JsonPath.using(conf).parse(response.asString());

            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                    () -> assertThat(documentContext.read("$.distance", Integer.class)).isEqualTo(68),
                    () -> assertThat(documentContext.read("$.fee", Integer.class)).isEqualTo(2350),
                    () -> assertThat(documentContext.read("$.path.size()", Integer.class)).isEqualTo(9),
                    () -> assertThat(documentContext.read("$.path[0].name", String.class)).isEqualTo("온수"),
                    () -> assertThat(documentContext.read("$.path[1].name", String.class)).isEqualTo("천왕"),
                    () -> assertThat(documentContext.read("$.path[2].name", String.class)).isEqualTo("광명사거리"),
                    () -> assertThat(documentContext.read("$.path[3].name", String.class)).isEqualTo("철산"),
                    () -> assertThat(documentContext.read("$.path[4].name", String.class)).isEqualTo("가산디지털단지"),
                    () -> assertThat(documentContext.read("$.path[5].name", String.class)).isEqualTo("남구로"),
                    () -> assertThat(documentContext.read("$.path[6].name", String.class)).isEqualTo("대림"),
                    () -> assertThat(documentContext.read("$.path[7].name", String.class)).isEqualTo("구로디지털단지"),
                    () -> assertThat(documentContext.read("$.path[8].name", String.class)).isEqualTo("신대방")
            );
        }
    }
}
