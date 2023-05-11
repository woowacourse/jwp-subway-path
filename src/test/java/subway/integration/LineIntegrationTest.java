package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.line.dto.LineRequest;
import subway.line.dto.LineSearchResponse;
import subway.station.domain.Station;

@DisplayName("지하철 노선 관련 기능")
class LineIntegrationTest extends IntegrationTest {
    
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        lineRequest1 = new LineRequest("신분당선", "bg-red-600");
        lineRequest2 = new LineRequest("구신분당선", "bg-red-600");
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    @Sql({"classpath:line.sql", "classpath:station.sql", "classpath:section.sql"})
    void getLines() {
        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final List<LineSearchResponse> result = response.jsonPath().getList(".", LineSearchResponse.class);
        assertAll(
                () -> assertThat(result.get(0).getId()).isPositive(),
                () -> assertThat(result.get(0).getName()).isEqualTo("1호선"),
                () -> assertThat(result.get(0).getColor()).isEqualTo("파란색"),
                () -> assertThat(result.get(0).getStations()).containsExactly(
                        new Station(1L, "1L"),
                        new Station(2L, "2L"),
                        new Station(3L, "3L"),
                        new Station(4L, "4L"),
                        new Station(5L, "5L"),
                        new Station(6L, "6L"),
                        new Station(7L, "7L")
                ),
                () -> assertThat(result.get(1).getId()).isPositive(),
                () -> assertThat(result.get(1).getName()).isEqualTo("2호선"),
                () -> assertThat(result.get(1).getColor()).isEqualTo("초록색"),
                () -> assertThat(result.get(1).getStations()).containsExactly(
                        new Station(8L, "8L"),
                        new Station(9L, "9L"),
                        new Station(4L, "4L"),
                        new Station(10L, "10L"),
                        new Station(11L, "11L")
                )
        );
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    @Sql({"classpath:line.sql", "classpath:station.sql", "classpath:section.sql"})
    void getLine() {
        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", 1)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final LineSearchResponse lineSearchResponse = response.as(LineSearchResponse.class);
        assertThat(lineSearchResponse.getId()).isEqualTo(1);
        assertThat(lineSearchResponse.getName()).isEqualTo("1호선");
        assertThat(lineSearchResponse.getColor()).isEqualTo("파란색");
        assertThat(lineSearchResponse.getStations()).containsExactly(
                new Station(1L, "1L"),
                new Station(2L, "2L"),
                new Station(3L, "3L"),
                new Station(4L, "4L"),
                new Station(5L, "5L"),
                new Station(6L, "6L"),
                new Station(7L, "7L")
        );
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        final ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        final Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest2)
                .when().put("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        final ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        final Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
