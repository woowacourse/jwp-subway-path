package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.ShortPathRequest;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Sql("classpath:test_schema.sql")
@Sql("classpath:test_data.sql")
class PathIntegrationTest {

    @Test
    void 두_역_사이의_최단경로와_요금을_반환한다() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ShortPathRequest("후추", "로운"))
                .when().get("/path")
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

}
