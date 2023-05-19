package subway.domain;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.line.Line;
import subway.domain.line.edge.StationEdge;
import subway.domain.navigation.JgraphtPathNavigation;
import subway.domain.path.LinePath;
import subway.domain.path.SubwayPath;
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

    @Test
    @DisplayName("지하철에서 경로를 검색한다.")
    void search_path_test() {
        // given
        final Station st1 = new Station(1L, "st1");
        final Station st2 = new Station(2L, "st2");
        final Station st3 = new Station(3L, "st3");
        final Station st4 = new Station(4L, "st4");
        final Station st5 = new Station(5L, "st5");
        final Station st6 = new Station(6L, "st6");
        final Station st7 = new Station(7L, "st7");
        final Line line1 = Line.of(1L, "line1", "red", Set.of(new StationEdge(st1.getId(), st3.getId(), 1)));
        final Line line2 = Line.of(2L, "line2", "blue", Set.of(new StationEdge(st7.getId(), st3.getId(), 2)));
        final Subway subway = Subway.of(
                List.of(line1, line2),
                List.of(st1, st2, st3, st4, st5, st6, st7)
        );
        subway.insertStationToLine(line1.getId(), st4.getId(), st3.getId(), LineDirection.DOWN, 2);
        subway.insertStationToLine(line1.getId(), st5.getId(), st4.getId(), LineDirection.DOWN, 2);
        subway.insertStationToLine(line1.getId(), st6.getId(), st5.getId(), LineDirection.DOWN, 2);
        subway.insertStationToLine(line1.getId(), st2.getId(), st6.getId(), LineDirection.DOWN, 1);
        subway.insertStationToLine(line2.getId(), st5.getId(), st3.getId(), LineDirection.DOWN, 3);

        // when
        final SubwayPath path = subway.findPath(st1.getId(), st2.getId(), new JgraphtPathNavigation());
        final List<LinePath> linePaths = path.getLinePaths();

        // then
        // expected : 1(st1 -> st3) 2(st3 -> st5) 1(t5 -> st6 -> st2)
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(path.getTotalDistance()).isEqualTo(7);
            softly.assertThat(linePaths).hasSize(3);
            softly.assertThat(linePaths.get(0).getLineId()).isEqualTo(line1.getId());
            softly.assertThat(linePaths.get(1).getLineId()).isEqualTo(line2.getId());
            softly.assertThat(linePaths.get(2).getLineId()).isEqualTo(line1.getId());
            softly.assertThat(linePaths.get(0).getStationIds()).containsExactly(st1.getId(), st3.getId());
            softly.assertThat(linePaths.get(1).getStationIds()).containsExactly(st3.getId(), st5.getId());
            softly.assertThat(linePaths.get(2).getStationIds()).containsExactly(st5.getId(), st6.getId(), st2.getId());
        });
    }

    @Test
    @DisplayName("노선에 등록된 역순으로도 경로를 얻는다.")
    void find_path_in_opposite_direction_test() {
        // given
        final Station st1 = new Station(1L, "st1");
        final Station st2 = new Station(2L, "st2");
        final Line line1 = Line.of(1L, "line1", "red", Set.of(new StationEdge(st1.getId(), st2.getId(), 1)));
        final Subway subway = Subway.of(
                List.of(line1),
                List.of(st1, st2)
        );

        // when
        final SubwayPath path = subway.findPath(st2.getId(), st1.getId(), new JgraphtPathNavigation());

        // then
        final List<Long> stationIds = path.getLinePaths().get(0).getStationIds();
        assertThat(stationIds).containsExactly(2L, 1L);
    }
}
