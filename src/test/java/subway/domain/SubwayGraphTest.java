package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class SubwayGraphTest {

    @Test
    void 한_노선에서의_최단거리_경로를_찾는다() {
        // given
        final List<Section> sections = List.of(new Section("서초역", "교대역", 7), new Section("교대역", "강남역", 12));
        final Subway subway = new Subway(List.of(new Line("2호선", sections)));
        final SubwayGraph subwayGraph = SubwayGraph.from(subway);

        // when
        final List<Station> shortestPath = subwayGraph.findShortestPath(new Station("서초역"), new Station("강남역"));

        // then
        assertThat(shortestPath).map(Station::getName)
                .containsExactly("서초역", "교대역", "강남역");
    }

    @Test
    void 환승이_가능한_노선에서의_최단거리_경로를_찾는다() {
        // given
        final List<Section> sections1 = List.of(new Section("서초역", "교대역", 7), new Section("교대역", "강남역", 12));
        final Line line1 = new Line("2호선", sections1);
        final List<Section> sections2 = List.of(new Section("교대역", "고속터미널역", 3), new Section("고속터미널역", "강남역", 4));
        final Line line2 = new Line("2호선", sections2);
        final Subway subway = new Subway(List.of(line1, line2));
        final SubwayGraph subwayGraph = SubwayGraph.from(subway);

        // when
        final List<Station> shortestPath = subwayGraph.findShortestPath(new Station("서초역"), new Station("강남역"));

        // then
        assertThat(shortestPath).map(Station::getName)
                .containsExactly("서초역", "교대역", "고속터미널역", "강남역");
    }

    @Test
    void 최단거리를_계산한다() {
        // given
        final List<Section> sections = List.of(new Section("서초역", "교대역", 7), new Section("교대역", "강남역", 12));
        final Subway subway = new Subway(List.of(new Line("2호선", sections)));
        final SubwayGraph subwayGraph = SubwayGraph.from(subway);

        // when
        final int distance = subwayGraph.calculateShortestDistance(new Station("서초역"), new Station("강남역"));

        // then
        assertThat(distance).isEqualTo(19);
    }

    @Test
    void 환승이_가능한_노선에서의_최단거리를_계산한다() {
        // given
        final List<Section> sections1 = List.of(new Section("서초역", "교대역", 7), new Section("교대역", "강남역", 12));
        final Line line1 = new Line("2호선", sections1);
        final List<Section> sections2 = List.of(new Section("교대역", "고속터미널역", 3), new Section("고속터미널역", "강남역", 4));
        final Line line2 = new Line("2호선", sections2);
        final Subway subway = new Subway(List.of(line1, line2));
        final SubwayGraph subwayGraph = SubwayGraph.from(subway);

        // when
        final int distance = subwayGraph.calculateShortestDistance(new Station("서초역"), new Station("강남역"));

        // then
        assertThat(distance).isEqualTo(14);
    }
}
