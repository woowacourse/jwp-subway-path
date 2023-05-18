package subway.domain;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathSegmentTest {

    @Test
    @DisplayName("전체 거리를 계산한다.")
    void calculateDistance() {
        //given
        List<StationEdge> stationEdges = List.of(new StationEdge(2L, Distance.from(5)),
                new StationEdge(3L, Distance.from(2)));
        PathSegment pathSegment = new PathSegment(1L, stationEdges);
        //when
        Distance totalDistance = pathSegment.calculateDistance();
        //then
        Assertions.assertThat(totalDistance).isEqualTo(Distance.from(7));
    }

}