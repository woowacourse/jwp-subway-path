package subway.support;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DijkstraTest {

    @Test
    void 최단_경로를_계산한다() {
        //given
        final Dijkstra<String> dijkstra = new Dijkstra<>();
        List<String> from = List.of("1", "2", "3");
        List<String> to = List.of("3", "4", "4");
        List<Integer> distance = List.of(1, 2, 3);

        //when
        final double weight = dijkstra.shortestPath(from, to, distance, "1", "4").getWeight();

        //then
        assertThat(weight).isEqualTo(4.0);
    }

}
