package subway.integration;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.controller.dto.request.LineRequest;
import subway.controller.dto.request.StationRegisterInLineRequest;
import subway.controller.dto.request.StationUnregisterInLineRequest;
import subway.controller.dto.request.SubwayDirection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 노선 관련 기능")
public class LineIntegrationTest extends IntegrationTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    @DisplayName("지하철 노선을 생성한다.")
    @Sql("/station_test_data.sql")
    void createLine() {
        // given
        final LineRequest lineRequest = new LineRequest("11호선", "bg-pink-600", 10, "잠실", "석촌");

        // when
        final ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/lines")
                .then().log().all()
                .extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank()
        );
    }

    @Test
    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Sql("/station_test_data.sql")
    void createLineWithDuplicateName() {
        // given
        final LineRequest lineRequest = new LineRequest("2호선", "bg-brown-600", 4, "석촌", "송파");
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/lines")
                .then()
                .extract();

        // when
        final ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/lines")
                .then()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Sql({"/line_test_data.sql", "/section_test_data.sql", "/station_test_data.sql"})
    void getLines() {
        // given, when
        final ExtractableResponse<Response> response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then()
                .extract();

        // then
        final Configuration conf = Configuration.defaultConfiguration();
        final DocumentContext documentContext = JsonPath.using(conf).parse(response.asString());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(documentContext.read("$.size()", Integer.class)).isEqualTo(2),
                () -> assertThat(documentContext.read("$[0]['name']", String.class)).isEqualTo("2호선"),
                () -> assertThat(documentContext.read("$[0]['color']", String.class)).isEqualTo("bg-green-600"),
                () -> assertThat(documentContext.read("$[0]['stations'].size()", Integer.class)).isEqualTo(3),
                () -> assertThat(documentContext.read("$[0]['stations'][0]['name']", String.class)).isEqualTo("잠실"),
                () -> assertThat(documentContext.read("$[0]['stations'][1]['name']", String.class)).isEqualTo("잠실새내")
        );
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    @Sql("/station_test_data.sql")
    void getLine() {
        // given
        final LineRequest lineRequest = new LineRequest("5호선", "bg-purple-600", 2, "잠실새내", "송파");
        final ExtractableResponse<Response> createResponse = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/lines")
                .then()
                .extract();

        // when
        final Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        final ExtractableResponse<Response> response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{id}", lineId)
                .then()
                .extract();

        // then
        final Configuration conf = Configuration.defaultConfiguration();
        final DocumentContext documentContext = JsonPath.using(conf).parse(response.asString());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(documentContext.read("$.id", Long.class)).isEqualTo(lineId),
                () -> assertThat(documentContext.read("$.name", String.class)).isEqualTo(lineRequest.getName()),
                () -> assertThat(documentContext.read("$.color", String.class)).isEqualTo(lineRequest.getColor()),
                () -> assertThat(documentContext.read("$.stations.size()", Integer.class)).isEqualTo(2),
                () -> assertThat(documentContext.read("$.stations[0].name", String.class))
                        .isEqualTo(lineRequest.getFirstStation()),
                () -> assertThat(documentContext.read("$.stations[1].name", String.class))
                        .isEqualTo(lineRequest.getSecondStation())
        );
    }

    @Test
    @DisplayName("노선에 역을 추가한다.")
    @Sql({"/line_test_data.sql", "/station_test_data.sql", "/section_test_data.sql"})
    void registerStation() {
        // given
        final long lineId = 1L;
        final StationRegisterInLineRequest request = new StationRegisterInLineRequest(SubwayDirection.UP, "잠실", "석촌", 1);

        // when
        final ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request).log().all()
                .when().patch("/lines/{id}/register", lineId)
                .then().log().all()
                .extract();

        // then
        final Configuration conf = Configuration.defaultConfiguration();
        final DocumentContext documentContext = JsonPath.using(conf).parse(response.asString());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(documentContext.read("$.id", Long.class)).isEqualTo(lineId),
                () -> assertThat(documentContext.read("$.stations.size()", Integer.class)).isEqualTo(4),
                () -> assertThat(documentContext.read("$.stations[0].name", String.class))
                        .isEqualTo(request.getStandardStationName()),
                () -> assertThat(documentContext.read("$.stations[1].name", String.class))
                        .isEqualTo(request.getNewStationName())
        );
    }

    @Test
    @DisplayName("노선에 역을 삭제한다.")
    @Sql({"/line_test_data.sql", "/station_test_data.sql", "/section_test_data.sql"})
    void unregisterSection() {
        // given
        final long lineId = 1L;
        final StationUnregisterInLineRequest request = new StationUnregisterInLineRequest("잠실");

        // when
        final ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().patch("/lines/{id}/unregister", lineId)
                .then()
                .extract();

        // then
        final Configuration conf = Configuration.defaultConfiguration();
        final DocumentContext documentContext = JsonPath.using(conf).parse(response.asString());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(documentContext.read("$.id", Long.class)).isEqualTo(lineId),
                () -> assertThat(documentContext.read("$.stations.size()", Integer.class)).isEqualTo(2),
                () -> assertThat(documentContext.read("$.stations[0].name", String.class)).isEqualTo("잠실새내"),
                () -> assertThat(documentContext.read("$.stations[1].name", String.class)).isEqualTo("종합운동장")
        );
    }

}
