package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.Distance;
import subway.domain.Route;
import subway.domain.Section;
import subway.domain.Station;

class RouteTest {

    @DisplayName("최단 거리 테스트")
    @Test
    void getShortestPath() {
        // given
        Station station1 = new Station("1역");
        Station station2 = new Station("2역");
        Station station3 = new Station("3역");
        Station station4 = new Station("4역");
        Station station5 = new Station("5역");

        Distance 거리3 = new Distance(3);

        Section section1 = new Section(station1, station2, 거리3);
        Section section2 = new Section(station2, station3, 거리3);
        Section section3 = new Section(station3, station4, 거리3);
        Section section4 = new Section(station4, station5, 거리3);

        Route route = new Route(List.of(section1, section2, section3, section4));

        // then
        List<Station> path = route.getPath(station1, station4);
        assertThat(path).hasSize(4);
        assertThat(path).containsExactly(station1, station2, station3, station4);

        assertThat(route.getPathWeight(station1, station4)).isEqualTo(9);
    }
}