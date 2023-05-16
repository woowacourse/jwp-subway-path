package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;

@DisplayName("지하철 노선 관련 기능")
public class LineIntegrationTest extends IntegrationTest {
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

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


    @DisplayName("지하철 노선과 해당하는 역을 조회한다.")
    @Test
    void findStationsByLineId() {
        RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", 1)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("lineResponse.id", equalTo(1))
                .body("lineResponse.name", equalTo("2호선"))
                .body("lineResponse.color", equalTo("Green"))
                .body("stationResponses.size()", equalTo(4))
                .body("stationResponses[0].id", equalTo(1))
                .body("stationResponses[0].name", equalTo("후추"))
                .body("stationResponses[1].id", equalTo(2))
                .body("stationResponses[1].name", equalTo("디노"))
                .body("stationResponses[2].id", equalTo(3))
                .body("stationResponses[2].name", equalTo("조앤"))
                .body("stationResponses[3].id", equalTo(4))
                .body("stationResponses[3].name", equalTo("로운"));
    }

    @DisplayName("모든 지하철 노선과 해당하는 역을 차례로 조회한다.")
    @Test
    void findLines() {
        RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("$", hasSize(2))
                .body("[0].lineResponse.id", equalTo(1))
                .body("[0].lineResponse.name", equalTo("2호선"))
                .body("[0].lineResponse.color", equalTo("Green"))
                .body("[0].stationResponses", hasSize(4))
                .body("[0].stationResponses[0].id", equalTo(1))
                .body("[0].stationResponses[0].name", equalTo("후추"))
                .body("[0].stationResponses[1].id", equalTo(2))
                .body("[0].stationResponses[1].name", equalTo("디노"))
                .body("[0].stationResponses[2].id", equalTo(3))
                .body("[0].stationResponses[2].name", equalTo("조앤"))
                .body("[0].stationResponses[3].id", equalTo(4))
                .body("[0].stationResponses[3].name", equalTo("로운"))
                .body("[1].lineResponse.id", equalTo(2))
                .body("[1].lineResponse.name", equalTo("8호선"))
                .body("[1].lineResponse.color", equalTo("pink"))
                .body("[1].stationResponses", hasSize(3))
                .body("[1].stationResponses[0].id", equalTo(3))
                .body("[1].stationResponses[0].name", equalTo("조앤"))
                .body("[1].stationResponses[1].id", equalTo(5))
                .body("[1].stationResponses[1].name", equalTo("포비"))
                .body("[1].stationResponses[2].id", equalTo(4))
                .body("[1].stationResponses[2].name", equalTo("로운"));
    }


    @DisplayName("지하철 노선을 수정한다.")
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

        // when
        Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest2)
                .when().put("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

}
