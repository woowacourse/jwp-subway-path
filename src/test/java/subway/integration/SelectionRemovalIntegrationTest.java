package subway.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.domain.Station;
import subway.dto.LineResponse;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("구간 삭제 관련 기능")
public class SelectionRemovalIntegrationTest extends IntegrationTest {

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
}
