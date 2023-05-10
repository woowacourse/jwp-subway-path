package subway.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StationEdgeTest {
    @Test
    @DisplayName("역 두 개와 거리로 생성한다.")
    void create() {
        //given
        Station station1 = new Station("잠실");
        Station station2 = new Station("잠실나루");
        int distance = 5;

        //when
        final var stationEdge = new StationEdge(station1, station2, distance);

        //then
        Assertions.assertThat(stationEdge)
                .isInstanceOf(StationEdge.class)
                .isNotNull();
    }
}