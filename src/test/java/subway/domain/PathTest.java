package subway.domain;

import static org.assertj.core.api.Assertions.*;

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
        Line line = Line.of("2호선", "초록색", upStationId, downStationId, distance);
        LineSegment lineSegment = new LineSegment(1L, line.getStationEdges());
        Path path = new Path(List.of(lineSegment));
        //when
        Distance totalDistance = path.calculateTotalDistance();
        //then
        assertThat(totalDistance).isEqualTo(Distance.from(2));
    }

}