package subway.integration;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.Station;
import subway.dto.LineResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate.sql")
public class SelectionRemovalIntegrationTest {
    @LocalServerPort
    int port;

    public static RequestSpecification given() {
        return RestAssured.given().log().all();
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("노선에 역이 2개 존재 시 둘 다 삭제한다")
    @Test
    void removeTwoWhenLineHasTwoStations() {
        //given
        createStation("이촌");
        createStation("용산");

        createLine("경의중앙선", "청록");

        List<LineResponse> lines = getLines();
        LineResponse line = getLine(lines.get(0).getId());
        Long lineId = line.getId();

        createLineStation(lineId, "1", "2", "10");

        //when
        deleteLineStation(lineId, 1L);

        //then
        given().
                when().
                get("/lines/" + lineId + "/stations").
                then().
                log().all().
                statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("상행 종점 역을 삭제한다")
    @Test
    void removeUpEndStation() {
        //given
        createStation("이촌");
        createStation("용산");
        createStation("서빙고");

        createLine("경의중앙선", "청록");

        List<LineResponse> lines = getLines();
        LineResponse line = getLine(lines.get(0).getId());
        Long lineId = line.getId();

        createLineStation(lineId, "2", "3", "10");
        createLineStation(lineId, "1", "2", "10");

        //when
        deleteLineStation(lineId, 1L);

        //then
        List<Station> stations = getLineStations(lineId);
        assertThat(stations.size()).isEqualTo(2);
        assertThat(stations.get(0).getName()).isEqualTo("용산");

    }

    @DisplayName("하행 종점 역을 삭제한다")
    @Test
    void removeDownEndStation() {
        //given
        createStation("이촌");
        createStation("용산");
        createStation("서빙고");

        createLine("경의중앙선", "청록");

        List<LineResponse> lines = getLines();
        LineResponse line = getLine(lines.get(0).getId());
        Long lineId = line.getId();

        createLineStation(lineId, "2", "3", "10");
        createLineStation(lineId, "1", "2", "10");

        //when
        deleteLineStation(lineId, 3L);

        //then
        List<Station> stations = getLineStations(lineId);
        assertThat(stations.size()).isEqualTo(2);
        assertThat(stations.get(1).getName()).isEqualTo("용산");
    }

    @DisplayName("중간 역을 삭제한다")
    @Test
    void removeInsideStation() {
        //given
        createStation("이촌");
        createStation("용산");
        createStation("서빙고");
        createStation("한남");

        createLine("경의중앙선", "청록");

        List<LineResponse> lines = getLines();
        LineResponse line = getLine(lines.get(0).getId());
        Long lineId = line.getId();

        createLineStation(lineId, "2", "3", "10");
        createLineStation(lineId, "1", "2", "10");
        createLineStation(lineId, "3", "4", "10");

        //when
        deleteLineStation(lineId, 2L);

        //then
        List<Station> stations = getLineStations(lineId);
        assertThat(stations.size()).isEqualTo(3);
        assertThat(stations.get(1).getName()).isEqualTo("서빙고");
    }

    private void createStation(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/stations").
                then().
                log().all().
                statusCode(HttpStatus.CREATED.value());
    }

    private void createLine(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/lines").
                then().
                log().all().
                statusCode(HttpStatus.CREATED.value());
    }


    private void createLineStation(Long id, String preStationId, String stationId, String distance) {
        Map<String, String> params = new HashMap<>();
        params.put("lineId", String.valueOf(id));
        params.put("preStationId", preStationId);
        params.put("stationId", stationId);
        params.put("distance", distance);

        given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/lines/" + id + "/stations").
                then().
                log().all().
                statusCode(HttpStatus.CREATED.value());
    }

    private void deleteLineStation(Long lineId, Long stationId) {
        given().
                when().
                delete("/lines/" + lineId + "/stations/" + stationId).
                then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value());
    }

    private List<LineResponse> getLines() {
        return
                given().
                        when().
                        get("/lines").
                        then().
                        log().all().
                        extract().
                        jsonPath().getList(".", LineResponse.class);
    }

    private LineResponse getLine(Long id) {
        return given().when().
                get("/lines/" + id).
                then().
                log().all().
                extract().as(LineResponse.class);
    }

    private List<Station> getLineStations(Long id) {
        return given().
                when().
                get("/lines/" + id + "/stations").
                then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                extract().
                jsonPath().getList("stations", Station.class);
    }
}
