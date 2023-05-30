package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.adapter.in.web.section.dto.AddStationToLineRequest;
import subway.application.port.in.line.dto.response.LineQueryResponse;
import subway.application.port.in.route.dto.response.RouteQueryResponse;
import subway.application.port.in.station.dto.response.StationQueryResponse;
import subway.fixture.LineFixture.이호선;
import subway.fixture.StationFixture.강남역;
import subway.fixture.StationFixture.역삼역;
import subway.fixture.StationFixture.잠실역;

@DisplayName("지하철역 관련 기능")
public class StationIntegrationTest extends IntegrationTest {

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");
        RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then()
                .log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void getStations() {
        /// given
        Map<String, String> params1 = new HashMap<>();
        params1.put("name", "강남역");
        ExtractableResponse<Response> createResponse1 = RestAssured.given().log().all()
                .body(params1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        Map<String, String> params2 = new HashMap<>();
        params2.put("name", "역삼역");
        ExtractableResponse<Response> createResponse2 = RestAssured.given().log().all()
                .body(params2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get("/stations")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Long> expectedStationIds = Stream.of(createResponse1, createResponse2)
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());
        List<Long> resultStationIds = response.jsonPath().getList(".", StationQueryResponse.class).stream()
                .map(StationQueryResponse::getId)
                .collect(Collectors.toList());
        assertThat(resultStationIds).containsAll(expectedStationIds);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStation() {
        /// given
        Map<String, String> params1 = new HashMap<>();
        params1.put("name", "강남역");
        ExtractableResponse<Response> createResponse = RestAssured.given().log().all()
                .body(params1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        // when
        Long stationId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get("/stations/{stationId}", stationId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        StationQueryResponse stationResponse = response.as(StationQueryResponse.class);
        assertThat(stationResponse.getId()).isEqualTo(stationId);
    }

    @DisplayName("지하철역을 수정한다.")
    @Test
    void updateStation() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");
        ExtractableResponse<Response> createResponse = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        // when
        Map<String, String> otherParams = new HashMap<>();
        otherParams.put("name", "삼성역");
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(otherParams)
                .when()
                .put(uri)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");
        ExtractableResponse<Response> createResponse = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("역 삭제시 구간도 제거한다")
    @Test
    void deleteStationWithSections() {
        // given
        // 역 등록
        StationQueryResponse station1 = addStation(강남역.REQUEST);
        StationQueryResponse station2 = addStation(역삼역.REQUEST);
        StationQueryResponse station3 = addStation(잠실역.REQUEST);

        // 노선 등록
        LineQueryResponse line1 = addLine(이호선.REQUEST);

        // 구간 등록
        addSection(new AddStationToLineRequest(station1.getId(), station2.getId(), 3), line1.getId());
        addSection(new AddStationToLineRequest(station2.getId(), station3.getId(), 5), line1.getId());

        // when
        // 역 삭제
        deleteStation(station2.getId());

        // then
        // 노선 조회
        LineQueryResponse line = findLine(line1.getId());
        RouteQueryResponse route = findRoute(station1.getId(), station3.getId());
        assertAll(
                () -> assertThat(line.getStations())
                        .extracting("name")
                        .containsExactly("강남역", "잠실역"),
                () -> assertThat(route.getRoute())
                        .extracting("name")
                        .containsExactly("강남역", "잠실역"),
                () -> assertThat(route.getDistance())
                        .isEqualTo(8)
        );
    }
}
