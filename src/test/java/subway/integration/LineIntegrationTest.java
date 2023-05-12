package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.Station;
import subway.dto.request.LineRequest;
import subway.dto.response.LineResponse;
import subway.dto.response.StationsInLineResponse;
import subway.dto.response.StationsResponse;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("노선 생성/조회/수정/삭제 기능")
public class LineIntegrationTest extends IntegrationTest {
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        lineRequest1 = new LineRequest("신분당선", "bg-red-600");
        lineRequest2 = new LineRequest("구신분당선", "bg-red-600");
    }

    @DisplayName("[노선 생성][정상] 새로운 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = RestAssured
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

    @DisplayName("[노선 생성][비정상] 이미 등록되어있는 노선 이름으로 노선을 생성하려하면 상태코드가 BAD_REQUEST 이다.")
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
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Sql("classpath:/testData.sql")
    @DisplayName("[노선 조회][정상] 모든 노선의 역들을 조회한다.")
    @Test
    void getLines() {
        // given
        List<StationsInLineResponse> expectedBodyResponse = List.of(
                new StationsInLineResponse(
                        new LineResponse(1L, "2호선", "green"),
                        new StationsResponse(
                                List.of(
                                        new Station(1L, "잠실"),
                                        new Station(2L, "잠실새내"),
                                        new Station(3L, "종합운동장")
                                )
                        )
                ),
                new StationsInLineResponse(
                        new LineResponse(2L, "8호선", "red"),
                        new StationsResponse(
                                List.of(
                                        new Station(5L, "천호"),
                                        new Station(1L, "잠실")
                                )
                        )
                ),
                new StationsInLineResponse(
                        new LineResponse(3L, "1호선", "blue"),
                        new StationsResponse(
                                Collections.emptyList()
                        )
                )
        );

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();

        List<StationsInLineResponse> bodyResponse = response.jsonPath().getList(".", StationsInLineResponse.class);


        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        for (int i = 0; i < bodyResponse.size(); i++) {
            assertThat(bodyResponse.get(i)).extracting("lineResponse")
                    .extracting("name")
                    .isEqualTo(expectedBodyResponse.get(i).getLineResponse().getName());

            assertThat(bodyResponse.get(i)).extracting("stationsResponse")
                    .extracting("stations")
                    .asList()
                    .containsAll(expectedBodyResponse.get(i).getStationsResponse().getStations());
        }
    }

    @Sql("classpath:/testData.sql")
    @DisplayName("[노선 조회][정상] 특정 노선을 노선 id로 조회한다.")
    @Test
    void getLine() {
        // given
        Long lineId = 1L;

        StationsResponse expectedBodyResponse = new StationsResponse(List.of(
                new Station(1L, "잠실"),
                new Station(2L, "잠실새내"),
                new Station(3L, "종합운동장")
        ));

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{line_id}", lineId)
                .then().log().all()
                .extract();

        StationsResponse bodyResponse = response.as(StationsResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(bodyResponse.getStations().size()).isEqualTo(expectedBodyResponse.getStations().size());
        assertThat(bodyResponse.getStations()).containsAll(expectedBodyResponse.getStations());
    }

    @DisplayName("[노선 수정][정상] 노선을 노선 id로 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest2)
                .when().put("/lines/{line_id}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("[노선 수정][비정상] 등록되지 않은 노선을 노선 id로 수정하려하면 상태코드가 BAD_REQUEST 이다.")
    @Test
    void updateLineWithNotExistingId() {
        // given
        Long lineId = 2L;
        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest2)
                .when().put("/lines/{line_id}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("[노선 삭제][정상] 노선을 노선 id로 삭제한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/lines/{line_id}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("[노선 제거][비정상] 등록되지 않은 노선을 노선 id로 삭제하려하면 상태코드가 BAD_REQUEST 이다.")
    @Test
    void deleteLineWithNotExistingId() {
        // given
        Long lineId = 2L;

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/lines/{line_id}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
