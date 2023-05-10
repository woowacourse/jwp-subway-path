package subway.domain;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StationEdgeTest {
    @Test
    @DisplayName("역 두 개와 거리로 생성한다.")
    void create() {
        //given
        int distance = 5;
        Long stationId = 1L;

        //when
        final var stationEdge = new StationEdge(stationId, distance);

        //then
        Assertions.assertThat(stationEdge)
                .isInstanceOf(StationEdge.class)
                .isNotNull();
    }

    @Test
    @DisplayName("거리와 역 아이디로 엣지를 쪼갠다.")
    void split() {
        //given
        StationEdge stationEdge = new StationEdge(1L, 5);
        //when
        List<StationEdge> split = stationEdge.split(2L, 3);
        //then
        assertSoftly(
                softly -> {
                    softly.assertThat(split.get(0).getDownStationId()).isEqualTo(2L);
                    softly.assertThat(split.get(0).getDistance()).isEqualTo(3);
                    softly.assertThat(split.get(1).getDownStationId()).isEqualTo(1L);
                    softly.assertThat(split.get(1).getDistance()).isEqualTo(2);
                }
        );
    }
}