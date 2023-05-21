package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import subway.domain.line.Line;
import subway.domain.route.EdgeSection;
import subway.domain.route.Route;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.domain.station.Stations;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


class RouteTest {

    /**
     * 11   5
     * F -- G -- H
     * 4 |         | 4
     * |         |
     * A -- B -- C -- D
     * 1   2    3
     */

    @ParameterizedTest
    @MethodSource("findShortestRoute")
    @DisplayName("findShortestRoute() : 시작점과 도착점이 주어지면 최단 경로를 구할 수 있다.")
    void test_findShortestRoute(
            final String start,
            final String end,
            final List<String> routes
    ) throws Exception {
        //given
        final List<Line> lines = createDefaultLines();
        final Route route = new Route(lines, new Station(start), new Station(end));

        //when
        final List<String> shortestRoute = route.findShortestRoute();

        //then
        assertThat(shortestRoute).containsExactlyElementsOf(routes);
    }

    static Stream<Arguments> findShortestRoute() {

        final String start1 = "A";
        final String end1 = "G";
        final List<String> route1 = List.of("A", "B", "C", "D", "H", "G");

        final String start2 = "A";
        final String end2 = "H";
        final List<String> route2 = List.of("A", "B", "C", "D", "H");

        final String start3 = "G";
        final String end3 = "C";
        final List<String> route3 = List.of("G", "H", "D", "C");

        final String start4 = "F";
        final String end4 = "H";
        final List<String> route4 = List.of("F", "B", "C", "D", "H");

        return Stream.of(
                Arguments.of(start1, end1, route1),
                Arguments.of(start2, end2, route2),
                Arguments.of(start3, end3, route3),
                Arguments.of(start4, end4, route4)
        );
    }

    /**
     * 11   5
     * F -- G -- H
     * 4 |         | 4
     * |         |
     * A -- B -- C -- D
     * 1   2    3
     */
    @ParameterizedTest
    @MethodSource("findShortestRouteDistance")
    @DisplayName("findShortestRouteDistance() : 시작점과 도착점이 주어지면 최단 거리를 구할 수 있다.")
    void test_findShortestRoutePrice(
            final Station start,
            final Station end,
            final double distance
    ) throws Exception {

        //given
        final List<Line> lines = createDefaultLines();
        final Route route = new Route(lines, start, end);

        //when
        final Distance shortestRouteDistance = route.findShortestRouteDistance();

        //then
        assertEquals(distance, shortestRouteDistance.getValue());
    }

    static Stream<Arguments> findShortestRouteDistance() {

        final Station start1 = new Station("A");
        final Station end1 = new Station("G");
        final double distance1 = 15.0;

        final Station start2 = new Station("A");
        final Station end2 = new Station("H");
        final double distance2 = 10.0;

        final Station start3 = new Station("G");
        final Station end3 = new Station("C");
        final double distance3 = 12.0;

        final Station start4 = new Station("F");
        final Station end4 = new Station("H");
        final double distance4 = 13.0;

        return Stream.of(
                Arguments.of(start1, end1, distance1),
                Arguments.of(start2, end2, distance2),
                Arguments.of(start3, end3, distance3),
                Arguments.of(start4, end4, distance4)
        );
    }

    @Test
    @DisplayName("findShortestRoute() : 도착점이나 출발점이 하나라도 그래프에 없다면 IllegalArgumentException이 발생한다.")
    void test_findShortestRoute_IllegalArgumentException_notContainStartOrEnd() throws Exception {
        //given
        final List<Line> lines = createDefaultLines();
        final Station from = new Station("A");
        final Station notExistTo = new Station("K");

        final Route route = new Route(lines, from, notExistTo);

        //when & then
        assertThatThrownBy(route::findShortestRoute)
                .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * 11   5
     * F -- G -- H
     * 4 |         | 4
     * |         |
     * A -- B -- C -- D
     * 1   2    3
     */

    @Test
    @DisplayName("findShortestRouteSections() : 최단 경로에 속한 섹션들을 구할 수 있다.")
    void test_findShortestRouteSections() throws Exception {
        //given
        final List<Line> lines = createDefaultLines();
        final Station from = new Station("A");
        final Station to = new Station("G");

        final Route route = new Route(lines, from, to);

        //when
        final List<EdgeSection> shortestRouteSections = route.findShortestRouteSections();

        //then
        final List<String> startStations =
                shortestRouteSections.stream()
                                     .map(EdgeSection::getStartStation)
                                     .collect(Collectors.toList());

        final List<String> endStations =
                shortestRouteSections.stream()
                                     .map(EdgeSection::getEndStation)
                                     .collect(Collectors.toList());

        assertAll(
                () -> assertEquals(5, shortestRouteSections.size()),
                () -> assertThat(startStations).containsAnyElementsOf(
                        List.of("A", "B", "C", "D", "H")),
                () -> assertThat(endStations).containsAnyElementsOf(
                        List.of("B", "C", "D", "H", "G"))
        );
    }

    private List<Line> createDefaultLines() {
        final Stations stations1 = new Stations(new Station("A"), new Station("B"), 1);
        final Stations stations2 = new Stations(new Station("B"), new Station("C"), 2);
        final Stations stations3 = new Stations(new Station("C"), new Station("D"), 3);

        final Section section1 = new Section(stations1);
        final Section section2 = new Section(stations2);
        final Section section3 = new Section(stations3);

        final Line line1 = new Line(1L, "1호선", List.of(section1, section2, section3));

        final Stations stations4 = new Stations(new Station("B"), new Station("F"), 4);
        final Stations stations5 = new Stations(new Station("F"), new Station("G"), 11);
        final Stations stations6 = new Stations(new Station("G"), new Station("H"), 5);
        final Stations stations7 = new Stations(new Station("H"), new Station("D"), 4);

        final Section section4 = new Section(stations4);
        final Section section5 = new Section(stations5);
        final Section section6 = new Section(stations6);
        final Section section7 = new Section(stations7);

        final Line line2 = new Line(2L, "2호선",
                                    List.of(section4, section5, section6, section7));

        return List.of(line1, line2);
    }
}
