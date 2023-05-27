package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PathFinderTest {

    private PathFinder pathFinder;

    @BeforeEach
    void setUp() {
        Station station1 = new Station("강남역");
        Station station2 = new Station("잠실역");
        Station station3 = new Station("강변역");
        Station station4 = new Station("건대입구역");
        Section section1 = new Section(station1, station2, new Distance(5));
        Section section2 = new Section(station2, station3, new Distance(3));
        Section section3 = new Section(station3, station4, new Distance(6));
        LinkedList<Section> sections1 = new LinkedList<>(List.of(section1, section2, section3));

        List<LinkedList<Section>> allSections = List.of(sections1);
        pathFinder = new PathFinder(allSections);
    }

    @DisplayName("역과 역 사이의 거리를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"강남역:잠실역:5", "잠실역:강변역:3", "강남역:강변역:8", "잠실역:건대입구역:9", "강남역:건대입구역:14"}, delimiter = ':')
    void getPathDistance(String stationName1, String stationName2, int distance) {
        // given
        Station station1 = new Station(stationName1);
        Station station2 = new Station(stationName2);

        // when
        int pathDistance = pathFinder.getPathDistance(station1, station2);

        // then
        assertThat(pathDistance).isEqualTo(distance);
    }

    @DisplayName("역과 역 사이에 있는 모든 역을 반환한다.")
    @Test
    void getStations() {
        // given
        Station station1 = new Station("강남역");
        Station station2 = new Station("건대입구역");

        // when
        List<Station> stations = pathFinder.getStations(station1, station2);
        List<String> stationNames = stations.stream()
                .map(Station::getName)
                .collect(Collectors.toList());

        // then
        assertThat(stationNames).containsExactly("강남역", "잠실역", "강변역", "건대입구역");
    }
}
