package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dto.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static subway.fixture.StationFixture.EXPRESS_BUS_TERMINAL_STATION;
import static subway.fixture.StationFixture.SAPYEONG_STATION;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LineIntegrationTest {
    public static final LineCreateRequest LINE_NINE_CREATE_REQUEST = new LineCreateRequest("9호선", "BROWN");
    public static final StationCreateRequest EXPRESS_BUS_TERMINAL_REQUEST = new StationCreateRequest(EXPRESS_BUS_TERMINAL_STATION.getName());
    public static final StationCreateRequest SAPYEONG_STATION_REQUEST = new StationCreateRequest(SAPYEONG_STATION.getName());
    public static final StationCreateRequest NEW_STATION_REQUEST = new StationCreateRequest("새 역");

    @LocalServerPort
    int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("TRUNCATE TABLE station");
        jdbcTemplate.update("TRUNCATE TABLE line");
        jdbcTemplate.update("TRUNCATE TABLE section");
    }

    @Test
    void createNewLineTest() {
        final LineCreateRequest lineCreateRequest = LINE_NINE_CREATE_REQUEST;
        final ExtractableResponse<Response> response = createNewLine(lineCreateRequest);

        assertAll(
                () -> assertThat(response.statusCode())
                        .isEqualTo(CREATED.value()),
                () -> assertThat(response.header("Location"))
                        .isNotBlank()
        );
    }

    @Test
    void createInitialSectionTest() {
        final StationResponse stationResponse1 = createNewStation(EXPRESS_BUS_TERMINAL_REQUEST);
        final StationResponse stationResponse2 = createNewStation(SAPYEONG_STATION_REQUEST);

        final LineResponse lineResponse = createNewLine(LINE_NINE_CREATE_REQUEST).as(LineResponse.class);

        final InitialSectionCreateRequest initialSectionCreateRequest = new InitialSectionCreateRequest(
                lineResponse.getId(), stationResponse1.getId(), stationResponse2.getId(), 5
        );

        final ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(APPLICATION_JSON_VALUE)
                .body(initialSectionCreateRequest)

                .when()
                .post("/lines/" + lineResponse.getId() + "/stations/initial")

                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(CREATED.value());
    }
    @Test
    void createStationInLineTest() {
        final StationResponse stationResponse1 = createNewStation(EXPRESS_BUS_TERMINAL_REQUEST);
        final StationResponse stationResponse2 = createNewStation(SAPYEONG_STATION_REQUEST);
        final StationResponse newStationResponse = createNewStation(NEW_STATION_REQUEST);

        final LineResponse lineResponse = createNewLine(LINE_NINE_CREATE_REQUEST).as(LineResponse.class);

        final InitialSectionCreateRequest initialSectionCreateRequest = new InitialSectionCreateRequest(
                lineResponse.getId(), stationResponse1.getId(), stationResponse2.getId(), 5
        );

        createNewSection(lineResponse, initialSectionCreateRequest);

        final SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(
                stationResponse1.getId(),
                newStationResponse.getId(),
                3);

        final ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(APPLICATION_JSON_VALUE)
                .body(sectionCreateRequest)

                .when()
                .post("/lines/" + lineResponse.getId() + "/stations")

                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(CREATED.value());
    }

    private void createNewSection(final LineResponse lineResponse, final InitialSectionCreateRequest initialSectionCreateRequest) {
        RestAssured
                .given()
                .contentType(APPLICATION_JSON_VALUE)
                .body(initialSectionCreateRequest)

                .when()
                .post("/lines/" + lineResponse.getId() + "/stations/initial")

                .then()
                .extract();
    }

    private ExtractableResponse<Response> createSection(final LineResponse lineResponse, final SectionCreateRequest sectionCreateRequest) {
        return RestAssured
                .given()
                .contentType(APPLICATION_JSON_VALUE)
                .body(sectionCreateRequest)

                .when()
                .post("/lines/" + lineResponse.getId() + "/stations")

                .then()
                .extract();
    }

    private StationResponse createNewStation(final StationCreateRequest stationCreateRequest) {
        return RestAssured
                .given()
                .contentType(APPLICATION_JSON_VALUE)
                .body(stationCreateRequest)

                .when()
                .post("/stations")

                .then()
                .extract()
                .as(StationResponse.class);
    }

    private ExtractableResponse<Response> createNewLine(final LineCreateRequest lineCreateRequest) {
        return RestAssured
                .given()
                .contentType(APPLICATION_JSON_VALUE)
                .body(lineCreateRequest)

                .when()
                .post("/lines")

                .then()
                .extract();
    }
}
