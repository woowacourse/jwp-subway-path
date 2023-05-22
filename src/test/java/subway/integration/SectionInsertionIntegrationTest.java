package subway.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.domain.Station;
import subway.dto.LineResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("구간 추가 관련 기능")
public class SectionInsertionIntegrationTest extends IntegrationTest {

    @DisplayName("지하철 노선에 지하철 역이 없을 때 둘 다 추가")
    @Test
    void addStationFirst() {
        //given
        createStation("이촌");
        createStation("용산");

        createLine("경의중앙선", "청록");

        List<LineResponse> lines = getLines();
        LineResponse line = getLine(lines.get(0).getId());
        Long lineId = line.getId();

        //when
        createLineStation(lineId, "1", "2", "10");

        //then
        List<Station> stations = getLineStations(lineId);
        assertThat(stations.size()).isEqualTo(2);
        assertThat(stations.get(0).getName()).isEqualTo("이촌");
    }

    @DisplayName("기존 역들 중간에 역을 추가한다")
    @Test
    void addStationInside() {
        //given
        createStation("이촌");
        createStation("용산");
        createStation("서빙고");

        createLine("경의중앙선", "청록");

        List<LineResponse> lines = getLines();
        LineResponse line = getLine(lines.get(0).getId());
        Long lineId = line.getId();

        createLineStation(lineId, "1", "3", "10");

        //when
        createLineStation(lineId, "1", "2", "5");

        //then
        List<Station> stations = getLineStations(lineId);
        assertThat(stations.size()).isEqualTo(3);
        assertThat(stations.get(1).getName()).isEqualTo("용산");
    }

    @DisplayName("상행 종점에 역을 추가한다")
    @Test
    void addStationUpEnd() {
        //given
        createStation("이촌");
        createStation("용산");
        createStation("서빙고");

        createLine("경의중앙선", "청록");

        List<LineResponse> lines = getLines();
        LineResponse line = getLine(lines.get(0).getId());
        Long lineId = line.getId();

        createLineStation(lineId, "2", "3", "10");

        //when
        createLineStation(lineId, "1", "2", "5");

        //then
        List<Station> stations = getLineStations(lineId);
        assertThat(stations.size()).isEqualTo(3);
        assertThat(stations.get(1).getName()).isEqualTo("용산");
    }

    @DisplayName("하행 종점에 역을 추가한다")
    @Test
    void addStationDownEnd() {
        //given
        createStation("이촌");
        createStation("용산");
        createStation("서빙고");

        createLine("경의중앙선", "청록");

        List<LineResponse> lines = getLines();
        LineResponse line = getLine(lines.get(0).getId());
        Long lineId = line.getId();

        createLineStation(lineId, "1", "2", "10");

        //when
        createLineStation(lineId, "2", "3", "5");

        //then
        List<Station> stations = getLineStations(lineId);
        assertThat(stations.size()).isEqualTo(3);
        assertThat(stations.get(2).getName()).isEqualTo("서빙고");
    }

    @DisplayName("거리 조건이 맞지 않을 경우 예외를 던진다")
    @Test
    void addStationWithInvalidDistance() {
        //given
        createStation("이촌");
        createStation("용산");
        createStation("서빙고");

        createLine("경의중앙선", "청록");

        List<LineResponse> lines = getLines();
        LineResponse line = getLine(lines.get(0).getId());
        Long lineId = line.getId();

        createLineStation(lineId, "1", "3", "10");

        Map<String, String> params = new HashMap<>();
        String id = String.valueOf(lineId);
        params.put("lineId", String.valueOf(lineId));
        params.put("preStationId", "1");
        params.put("stationId", "2");
        params.put("distance", "15");

        given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/lines/" + id + "/stations").
                then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
