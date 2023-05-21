package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.application.dto.PathsResponse;
import subway.application.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class PathIntegrationTest extends IntegrationTest{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("최단 경로를 찾을 수 있다.")
    @Test
    void findShortestPaths() {
        jdbcTemplate.update(
                "insert into SECTIONS (line_id, left_station_id, right_station_id, distance) VALUES (1L, 1L, 2L, 2)");
        jdbcTemplate.update(
                "insert into SECTIONS (line_id, left_station_id, right_station_id, distance) VALUES (1L, 2L, 3L, 7)");
        jdbcTemplate.update(
                "insert into SECTIONS (line_id, left_station_id, right_station_id, distance) VALUES (1L, 3L, 4L, 3)");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get("/paths/1/4")
                .then().log().all()
                .extract();

        PathsResponse expected = PathsResponse.of(List.of(new StationResponse(1L, "종각역"), new StationResponse(2L, "서울역"),
                new StationResponse(3L, "아현역"), new StationResponse(4L, "시청역")), 12, 1350);

        PathsResponse pathsResponse = response.as(PathsResponse.class);
        System.out.println(pathsResponse);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(pathsResponse).isEqualTo(expected)
        );
    }
}
