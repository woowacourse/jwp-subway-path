package subway.domain;

import static org.assertj.core.api.Assertions.*;
import static subway.domain.Line.UP_END_EDGE_DISTANCE;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathTest {

    @Test
    @DisplayName("전체 거리를 구할 수 있다.")
    void calculateFare() {
        //given
        Long downStationId = 1L;
        int distance = 2;
        Long upStationId = 2L;
        StationEdges stationEdges = StationEdges.from(
                List.of(new StationEdge(upStationId, UP_END_EDGE_DISTANCE), new StationEdge(downStationId, distance))
        );
        Line line = new Line("2호선", "초록색", stationEdges);
        PathSegment pathSegment = new PathSegment(line.getId(), line.getStationEdges());
        Path path = new Path(List.of(pathSegment));
        //when
        Distance totalDistance = path.calculateTotalDistance();
        //then
        assertThat(totalDistance).isEqualTo(Distance.from(2));
    }

}