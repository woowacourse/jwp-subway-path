package subway.infrastructure.graph;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.StationFixture;
import subway.application.core.domain.Distance;
import subway.application.core.domain.RouteMap;
import subway.application.core.domain.Section;
import subway.application.core.domain.Station;
import subway.application.core.service.dto.out.PathFindResult;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JgraphtPathFinderTest {

    private static JgraphtPathFinder jgraphtPathFinder;

    @BeforeAll
    static void setup() {
        jgraphtPathFinder = new JgraphtPathFinder();
    }

    @Test
    @DisplayName("그래프가 일직선으로 이어진 경우, 최단경로를 및 거리를 찾을 수 있다")
    void findShortestPath_oneLine() {
        // given
        List<RouteMap> routeMaps = List.of(
                routeMapOf(List.of(
                        StationFixture.of(1L, "A"),
                        StationFixture.of(2L, "B"),
                        StationFixture.of(3L, "C"),
                        StationFixture.of(4L, "D")
                ))
        );
        Station departure = new Station(1L, "A");
        Station terminal = new Station(4L, "D");

        // when
        PathFindResult result = jgraphtPathFinder.findShortestPath(routeMaps, departure, terminal);

        // then
        assertThat(result.getShortestPath()).containsExactly(
                StationFixture.of(1L, "A"), StationFixture.of(2L, "B"),
                StationFixture.of(3L, "C"), StationFixture.of(4L, "D")
        );
        assertThat(result.getDistance()).isEqualTo(3);
    }

    @Test
    @DisplayName("환승할 수 있는 경우, 최단경로를 및 거리를 찾을 수 있다")
    void findShortestPath_transfer() {
        // given
        List<RouteMap> routeMaps = List.of(
                routeMapOf(List.of(
                        StationFixture.of(1L, "A"),
                        StationFixture.of(2L, "B"),
                        StationFixture.of(3L, "C"),
                        StationFixture.of(4L, "D")
                )),
                routeMapOf(List.of(
                        StationFixture.of(2L, "B"),
                        StationFixture.of(5L, "E"),
                        StationFixture.of(6L, "F")
                ))
        );
        Station departure = new Station(1L, "A");
        Station terminal = new Station(6L, "F");

        // when
        PathFindResult result = jgraphtPathFinder.findShortestPath(routeMaps, departure, terminal);

        // then
        assertThat(result.getShortestPath()).containsExactly(
                StationFixture.of(1L, "A"), StationFixture.of(2L, "B"),
                StationFixture.of(5L, "E"), StationFixture.of(6L, "F")
        );
        assertThat(result.getDistance()).isEqualTo(3);
    }

    @Test
    @DisplayName("해당 목적지로 가는 여러 경로가 존재하는 경우, 최단경로를 및 거리를 찾을 수 있다")
    void findShortestPath_selectRoute() {
        // given
        List<RouteMap> routeMaps = List.of(
                routeMapOf(List.of(
                        StationFixture.of(1L, "A"),
                        StationFixture.of(2L, "B"),
                        StationFixture.of(3L, "C")
                )),
                routeMapOf(List.of(
                        StationFixture.of(1L, "A"),
                        StationFixture.of(4L, "D")
                )),
                routeMapOf(List.of(
                        StationFixture.of(3L, "C"),
                        StationFixture.of(5L, "E"),
                        StationFixture.of(4L, "D")
                ))
        );
        Station departure = new Station(1L, "A");
        Station terminal = new Station(5L, "E");

        // when
        PathFindResult result = jgraphtPathFinder.findShortestPath(routeMaps, departure, terminal);

        // then
        assertThat(result.getShortestPath()).containsExactly(
                StationFixture.of(1L, "A"), StationFixture.of(4L, "D"),
                StationFixture.of(5L, "E")
        );
        assertThat(result.getDistance()).isEqualTo(2);
    }

    RouteMap routeMapOf(List<Station> stations) {
        List<Section> sections = new ArrayList<>();
        for (int i = 0; i < stations.size() - 1; i++) {
            sections.add(new Section(
                    stations.get(i),
                    stations.get(i + 1),
                    new Distance(1)
            ));
        }
        return new RouteMap(sections);
    }
}
