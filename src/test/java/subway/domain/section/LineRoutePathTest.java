package subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import subway.domain.path.LineRoutePath;
import subway.domain.station.Station;
import subway.domain.station.StationDistance;

class LineRoutePathTest {

    @Test
    void sortedPath() {
        //given
        final List<Section> sections = List.of(
                new Section(new Station("B"), new Station("C"), new StationDistance(1)),
                new Section(new Station("C"), new Station("D"), new StationDistance(2)),
                new Section(new Station("A"), new Station("B"), new StationDistance(3))
        ); // A-B-C-D

        //when
        final LineRoutePath lineRoutePath = new LineRoutePath(new Sections(sections));

        //then
        final List<Station> sortedStations = lineRoutePath.getStations();
        assertThat(sortedStations).hasSize(4);
        assertThat(sortedStations).containsExactly(
                new Station("A"),
                new Station("B"),
                new Station("C"),
                new Station("D")
        );
    }
}
