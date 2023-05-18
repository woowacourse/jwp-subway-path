package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.line.Line;
import subway.domain.line.edge.StationEdge;
import subway.domain.station.Station;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayTest {

    @Test
    @DisplayName("노선에 역을 추가한다.")
    void insert_station_to_line_test() {
        // given
        final Station upStation = new Station(1L, "up");
        final Station downStation = new Station(2L, "down");
        final Line line = Line.of(1L, "2호선", "green", Set.of(new StationEdge(upStation.getId(), downStation.getId(), 7)));
        final Subway subway = Subway.of(
                List.of(line),
                List.of(upStation, downStation)
        );
        final Station middleStation = new Station(3L, "middle");
        subway.addStation(middleStation);

        // when
        subway.insertStationToLine(line.getId(), middleStation.getId(), upStation.getId(), LineDirection.DOWN, 2);

        // then
        List<Station> stations = subway.getStationsIn(line.getId());
        assertThat(stations).containsExactly(upStation, middleStation, downStation);
    }

    @Test
    @DisplayName("노선에서 역을 제거한다.")
    void remove_station_from_line_test() {
        // given
        final Station upStation = new Station(1L, "up");
        final Station downStation = new Station(2L, "down");
        final Line line = Line.of(1L, "2호선", "green", Set.of(new StationEdge(upStation.getId(), downStation.getId(), 7)));
        final Subway subway = Subway.of(
                List.of(line),
                List.of(upStation, downStation)
        );
        final Station middleStation = new Station(3L, "middle");
        subway.addStation(middleStation);
        subway.insertStationToLine(line.getId(), middleStation.getId(), upStation.getId(), LineDirection.DOWN, 2);

        // when
        subway.removeStationFromLine(line.getId(), middleStation.getId());

        // then
        final List<Station> stations = subway.getStationsIn(line.getId());
        assertThat(stations).containsExactly(upStation, downStation);
    }

}
