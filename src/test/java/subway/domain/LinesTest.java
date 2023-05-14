package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.edge.Edge;
import subway.domain.line.Line;
import subway.domain.line.Lines;
import subway.domain.station.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LinesTest {
    @Test
    @DisplayName("해당 노선에 존재하는 모든 역을 가져온다.")
    void blah() {
        Station a = new Station(1L, "A");
        Station b = new Station(2L, "B");
        Station c = new Station(3L, "C");
        Station d = new Station(4L, "D");

        List<Edge> edges = List.of(new Edge(c, d, 5), new Edge(a, b, 3), new Edge(b, c, 4));
        Line line = new Line(1L, "3호선", edges);

        Lines lines = new Lines();
        List<Station> allStation = lines.findAllStation(line);
        assertThat(allStation).containsExactly(a, b, c, d);
    }
}
