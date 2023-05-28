package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class RouteMapTest {

    @Test
    @DisplayName("Route 객체를 정상적으로 생성한다")
    void createRouteMap() {

        //given
        final Stations stations1 = new Stations(new Station("A"), new Station("B"), 5);
        final Stations stations2 = new Stations(new Station("B"), new Station("C"), 4);
        final Stations stations3 = new Stations(new Station("C"), new Station("D"), 3);
        final Stations stations4 = new Stations(new Station("D"), new Station("E"), 3);

        Section section1 = new Section(stations1);

        final Line line = new Line("2호선");

        line.add(section1);

        final Section section2 = new Section(stations2);
        line.add(section2);

        final Section section3 = new Section(stations3);
        line.add(section3);

        final Section section4 = new Section(stations4);
        line.add(section4);


        //when & then
        assertDoesNotThrow(() -> RouteMap.generateRouteMap(List.of(line)));
    }


    /**
     * 노선(2호선): A -> B -> C -> D -> E
     */
    @Test
    @DisplayName("노선에 포함된 역을 바탕으로 그래프의 정점을 정상적으로 등록한다")
    void AddGraphVertex() {

        //given
        final Stations stations1 = new Stations(new Station("A"), new Station("B"), 5);
        final Stations stations2 = new Stations(new Station("B"), new Station("C"), 4);
        final Stations stations3 = new Stations(new Station("C"), new Station("D"), 3);
        final Stations stations4 = new Stations(new Station("D"), new Station("E"), 3);

        Section section1 = new Section(stations1);

        final Line line = new Line("2호선");

        line.add(section1);

        final Section section2 = new Section(stations2);
        line.add(section2);

        final Section section3 = new Section(stations3);
        line.add(section3);

        final Section section4 = new Section(stations4);
        line.add(section4);

        RouteMap routeMap = RouteMap.generateRouteMap(List.of(line));

        //when
        WeightedMultigraph<String, DefaultWeightedEdge> graph = routeMap.getGraph();

        //then
        assertAll(
                () -> assertThat(graph.containsVertex("A")).isTrue(),
                () -> assertThat(graph.containsVertex("B")).isTrue(),
                () -> assertThat(graph.containsVertex("C")).isTrue(),
                () -> assertThat(graph.containsVertex("D")).isTrue(),
                () -> assertThat(graph.containsVertex("E")).isTrue()
        );
    }

    /**
     * 노선(2호선): A -> B -> C -> D -> E
     */
    @Test
    @DisplayName("노선에 포함된 구간을 바탕으로 그래프의 간선을 정상적으로 등록한다")
    void AddGraphEdge() {

        //given
        final Stations stations1 = new Stations(new Station("A"), new Station("B"), 5);
        final Stations stations2 = new Stations(new Station("B"), new Station("C"), 4);
        final Stations stations3 = new Stations(new Station("C"), new Station("D"), 3);
        final Stations stations4 = new Stations(new Station("D"), new Station("E"), 3);

        Section section1 = new Section(stations1);

        final Line line = new Line("2호선");

        line.add(section1);

        final Section section2 = new Section(stations2);
        line.add(section2);

        final Section section3 = new Section(stations3);
        line.add(section3);

        final Section section4 = new Section(stations4);
        line.add(section4);

        RouteMap routeMap = RouteMap.generateRouteMap(List.of(line));

        //when
        WeightedMultigraph<String, DefaultWeightedEdge> graph = routeMap.getGraph();

        //then
        assertAll(
                () -> assertThat(graph.containsEdge("A", "B")).isTrue(),
                () -> assertThat(graph.containsEdge("B", "C")).isTrue(),
                () -> assertThat(graph.containsEdge("C", "D")).isTrue(),
                () -> assertThat(graph.containsEdge("D", "E")).isTrue()
        );
    }

    /**
     * 노선(2호선): A -> B -> C -> D -> E
     */
    @Test
    @DisplayName("노선에 포함된 구간을 바탕으로 그래프의 가중치를 정상적으로 등록한다")
    void AddGraphWeight() {

        //given
        final Stations stations1 = new Stations(new Station("A"), new Station("B"), 4);
        final Stations stations2 = new Stations(new Station("B"), new Station("C"), 3);
        final Stations stations3 = new Stations(new Station("C"), new Station("D"), 2);
        final Stations stations4 = new Stations(new Station("D"), new Station("E"), 1);

        Section section1 = new Section(stations1);

        final Line line = new Line("2호선");

        line.add(section1);

        final Section section2 = new Section(stations2);
        line.add(section2);

        final Section section3 = new Section(stations3);
        line.add(section3);

        final Section section4 = new Section(stations4);
        line.add(section4);

        RouteMap routeMap = RouteMap.generateRouteMap(List.of(line));

        //when
        WeightedMultigraph<String, DefaultWeightedEdge> graph = routeMap.getGraph();

        //then
        assertAll(
                () -> assertThat(graph.getEdgeWeight(graph.getEdge("A", "B"))).isEqualTo(4),
                () -> assertThat(graph.getEdgeWeight(graph.getEdge("B", "C"))).isEqualTo(3),
                () -> assertThat(graph.getEdgeWeight(graph.getEdge("C", "D"))).isEqualTo(2),
                () -> assertThat(graph.getEdgeWeight(graph.getEdge("D", "E"))).isEqualTo(1)
        );
    }

    /**
     * 노선(2호선): A -> B -> C -> D -> E
     * 탐색 경로: from B -> To D
     * 결과 : B -> C -> D
     */
    @Test
    @DisplayName("단일 노선일 경우 최단 경로를 구한다")
    void findShortestRoute() {

        //given
        final Stations stations1 = new Stations(new Station("A"), new Station("B"), 4);
        final Stations stations2 = new Stations(new Station("B"), new Station("C"), 3);
        final Stations stations3 = new Stations(new Station("C"), new Station("D"), 2);
        final Stations stations4 = new Stations(new Station("D"), new Station("E"), 1);

        Section section1 = new Section(stations1);

        final Line line = new Line("2호선");

        line.add(section1);

        final Section section2 = new Section(stations2);
        line.add(section2);

        final Section section3 = new Section(stations3);
        line.add(section3);

        final Section section4 = new Section(stations4);
        line.add(section4);

        RouteMap routeMap = RouteMap.generateRouteMap(List.of(line));

        //when
        List<String> shortestPath = routeMap.findShortestPath("B", "D");

        //then
        assertThat(shortestPath).contains("B", "C", "D");
    }

    /**
     * 노선(2호선): A -> B -> C -> D -> E
     * 탐색 경로: from B -> To D
     * 예상 거리: 5km
     */
    @Test
    @DisplayName("단일 노선일 경우 최단 경로의 거리를 구한다")
    void findShortestRouteDistance() {

        //given
        final Stations stations1 = new Stations(new Station("A"), new Station("B"), 4);
        final Stations stations2 = new Stations(new Station("B"), new Station("C"), 3);
        final Stations stations3 = new Stations(new Station("C"), new Station("D"), 2);
        final Stations stations4 = new Stations(new Station("D"), new Station("E"), 1);

        Section section1 = new Section(stations1);

        final Line line = new Line("2호선");

        line.add(section1);

        final Section section2 = new Section(stations2);
        line.add(section2);

        final Section section3 = new Section(stations3);
        line.add(section3);

        final Section section4 = new Section(stations4);
        line.add(section4);

        RouteMap routeMap = RouteMap.generateRouteMap(List.of(line));

        //when
        double distance = routeMap.findShortestDistance("B", "D");

        //then
        assertThat(distance).isEqualTo(5);
    }

    /**
     * 노선(2호선): A -> B -> C -> D
     * 노선(8호선): X -> C -> Y -> Z
     * 탐색 경로: from B -> To Y
     * 결과 : B -> C -> Y
     */
    @Test
    @DisplayName("복수 노선일 경우 최단 경로를 구한다")
    void findShortestRouteWithMultiLines() {

        //given
        final Stations stations1 = new Stations(new Station("A"), new Station("B"), 4);
        final Stations stations2 = new Stations(new Station("B"), new Station("C"), 3);
        final Stations stations3 = new Stations(new Station("C"), new Station("D"), 2);

        final Stations stations4 = new Stations(new Station("X"), new Station("C"), 7);
        final Stations stations5 = new Stations(new Station("C"), new Station("Y"), 6);
        final Stations stations6 = new Stations(new Station("Y"), new Station("Z"), 5);

        final Line line1 = new Line("2호선");

        Section section1 = new Section(stations1);
        line1.add(section1);

        final Section section2 = new Section(stations2);
        line1.add(section2);

        final Section section3 = new Section(stations3);
        line1.add(section3);


        final Line line2 = new Line("8호선");

        final Section section4 = new Section(stations4);
        line2.add(section4);

        final Section section5 = new Section(stations5);
        line2.add(section5);

        final Section section6 = new Section(stations6);
        line2.add(section6);

        RouteMap routeMap = RouteMap.generateRouteMap(List.of(line1, line2));

        //when
        List<String> shortestPath = routeMap.findShortestPath("B", "Y");

        //then
        assertThat(shortestPath).contains("B", "C", "Y");
    }

    /**
     * 노선(2호선): A -> B -> C -> D
     * 노선(8호선): X -> C -> Y -> Z
     * 탐색 경로: from B -> To Y
     * 결과 : 9km (B -> C -> Y)
     */
    @Test
    @DisplayName("복수 노선일 경우 최단 경로의 이동 거리를 구한다")
    void findShortestRouteDistanceWithMultiLines() {

        //given
        final Stations stations1 = new Stations(new Station("A"), new Station("B"), 4);
        final Stations stations2 = new Stations(new Station("B"), new Station("C"), 3);
        final Stations stations3 = new Stations(new Station("C"), new Station("D"), 2);

        final Stations stations4 = new Stations(new Station("X"), new Station("C"), 7);
        final Stations stations5 = new Stations(new Station("C"), new Station("Y"), 6);
        final Stations stations6 = new Stations(new Station("Y"), new Station("Z"), 5);

        final Line line1 = new Line("2호선");

        Section section1 = new Section(stations1);
        line1.add(section1);

        final Section section2 = new Section(stations2);
        line1.add(section2);

        final Section section3 = new Section(stations3);
        line1.add(section3);


        final Line line2 = new Line("8호선");

        final Section section4 = new Section(stations4);
        line2.add(section4);

        final Section section5 = new Section(stations5);
        line2.add(section5);

        final Section section6 = new Section(stations6);
        line2.add(section6);

        RouteMap routeMap = RouteMap.generateRouteMap(List.of(line1, line2));

        //when
        double shortestDistance = routeMap.findShortestDistance("B", "Y");

        //then
        assertThat(shortestDistance).isEqualTo(9);
    }

    /**
     * 노선(2호선): A -> B -> C -> D
     * 노선(8호선): X -> C -> Y -> Z
     * 탐색 경로: from B -> To Y
     * 결과 : B -> C -> Y / 9km
     */
    @Test
    @DisplayName("이동 거리가 10km 미만일 경우 기본 요금을 계산한다")
    void calculateFareShorterThan10Km() {

        //given
        final Stations stations1 = new Stations(new Station("A"), new Station("B"), 4);
        final Stations stations2 = new Stations(new Station("B"), new Station("C"), 3);
        final Stations stations3 = new Stations(new Station("C"), new Station("D"), 2);

        final Stations stations4 = new Stations(new Station("X"), new Station("C"), 7);
        final Stations stations5 = new Stations(new Station("C"), new Station("Y"), 6);
        final Stations stations6 = new Stations(new Station("Y"), new Station("Z"), 5);

        final Line line1 = new Line("2호선");

        Section section1 = new Section(stations1);
        line1.add(section1);

        final Section section2 = new Section(stations2);
        line1.add(section2);

        final Section section3 = new Section(stations3);
        line1.add(section3);


        final Line line2 = new Line("8호선");

        final Section section4 = new Section(stations4);
        line2.add(section4);

        final Section section5 = new Section(stations5);
        line2.add(section5);

        final Section section6 = new Section(stations6);
        line2.add(section6);

        RouteMap routeMap = RouteMap.generateRouteMap(List.of(line1, line2));

        //when
        double shortestDistance = routeMap.findShortestDistance("B", "Y");

        int fare = routeMap.calculateFare(shortestDistance);

        //then
        assertThat(fare).isEqualTo(1250);
    }

    /**
     * 노선(2호선): A -> B -> C -> D
     * 노선(8호선): X -> C -> Y -> Z
     * 탐색 경로: from B -> To Y
     * 결과 : B -> C -> Y / 16km
     */
    @Test
    @DisplayName("이동 거리가 10km 초과 50km 이하일 경우 기본 요금 + 추가 요금을 계산한다.")
    void calculateFareLongerThan10KmShorterThan50Km() {

        //given
        final Stations stations1 = new Stations(new Station("A"), new Station("B"), 4);
        final Stations stations2 = new Stations(new Station("B"), new Station("C"), 9);
        final Stations stations3 = new Stations(new Station("C"), new Station("D"), 2);

        final Stations stations4 = new Stations(new Station("X"), new Station("C"), 3);
        final Stations stations5 = new Stations(new Station("C"), new Station("Y"), 7);
        final Stations stations6 = new Stations(new Station("Y"), new Station("Z"), 5);

        final Line line1 = new Line("2호선");

        Section section1 = new Section(stations1);
        line1.add(section1);

        final Section section2 = new Section(stations2);
        line1.add(section2);

        final Section section3 = new Section(stations3);
        line1.add(section3);


        final Line line2 = new Line("8호선");

        final Section section4 = new Section(stations4);
        line2.add(section4);

        final Section section5 = new Section(stations5);
        line2.add(section5);

        final Section section6 = new Section(stations6);
        line2.add(section6);

        RouteMap routeMap = RouteMap.generateRouteMap(List.of(line1, line2));

        //when
        double shortestDistance = routeMap.findShortestDistance("B", "Y");

        int fare = routeMap.calculateFare(shortestDistance);

        //then
        assertThat(fare).isEqualTo(1450);
    }

    /**
     * 노선(2호선): A -> B -> C -> D
     * 노선(8호선): X -> C -> Y -> Z
     * 탐색 경로: from B -> To Y
     * 결과 : B -> C -> Y / 58km
     */
    @Test
    @DisplayName("이동 거리가 50km를 초과할 경우 기본 요금 + 추가 요금을 계산한다.")
    void calculateFareLongerThan50Km() {

        //given
        final Stations stations1 = new Stations(new Station("A"), new Station("B"), 4);
        final Stations stations2 = new Stations(new Station("B"), new Station("C"), 30);
        final Stations stations3 = new Stations(new Station("C"), new Station("D"), 2);

        final Stations stations4 = new Stations(new Station("X"), new Station("C"), 7);
        final Stations stations5 = new Stations(new Station("C"), new Station("Y"), 28);
        final Stations stations6 = new Stations(new Station("Y"), new Station("Z"), 5);

        final Line line1 = new Line("2호선");

        Section section1 = new Section(stations1);
        line1.add(section1);

        final Section section2 = new Section(stations2);
        line1.add(section2);

        final Section section3 = new Section(stations3);
        line1.add(section3);


        final Line line2 = new Line("8호선");

        final Section section4 = new Section(stations4);
        line2.add(section4);

        final Section section5 = new Section(stations5);
        line2.add(section5);

        final Section section6 = new Section(stations6);
        line2.add(section6);

        RouteMap routeMap = RouteMap.generateRouteMap(List.of(line1, line2));

        //when
        double shortestDistance = routeMap.findShortestDistance("B", "Y");

        int fare = routeMap.calculateFare(shortestDistance);

        //then
        assertThat(fare).isEqualTo(2150);
    }

}