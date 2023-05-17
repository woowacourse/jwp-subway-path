package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@Sql("/InitializeTable.sql")
@DisplayName("최단 경로 찾기 인수테스트")
public class PathIntegrationTest extends IntegrationTest {

    @Test
    @DisplayName("최단 경로를 검색한다.")
    void findShortestPath() {
        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/path/?source=1&target=3")
                .then().log().all()
                .extract();

        //then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.body().jsonPath().getInt("distance")).isEqualTo(20),
                () -> assertThat(response.body().jsonPath().getInt("price")).isEqualTo(1450),
                () -> assertThat(response.body().jsonPath().getList("path")).hasSize(2)
        );
    }

    @Test
    @DisplayName("최단 경로검색에 실패한 경우 400을 던진다..")
    void findShortestPathFail() {
        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/path/?source=1&target=4")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
