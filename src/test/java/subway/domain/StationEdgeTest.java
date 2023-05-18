package subway.domain;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.line.edge.StationEdge;
import subway.exception.IllegalStationEdgeException;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StationEdgeTest {

    @Test
    @DisplayName("두 역의 아이디를 통해 구간을 생성한다.")
    void construct_test() {
        // given
        final StationEdge stationEdge = new StationEdge(1L, 2L, 5);

        // when
        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(stationEdge.getUpStationId()).isEqualTo(1L);
            softly.assertThat(stationEdge.getDownStationId()).isEqualTo(2L);
            softly.assertThat(stationEdge.getDistance()).isEqualTo(5);
        });
    }

    @Test
    @DisplayName("상행과 하행역이 동일하면 예외를 발생시킨다.")
    void station_id_validation_test() {
        assertThatThrownBy(() -> new StationEdge(1L, 1L, 5))
                .isInstanceOf(IllegalStationEdgeException.class)
                .hasMessageContaining(StationEdge.SAME_STATION_ID_EXCEPTION_MESSAGE);
    }

    @Test
    @DisplayName("역간거리가 0이하이면 예외를 발생시킨다.")
    void distance_validation_test() {
        assertThatThrownBy(() -> new StationEdge(1L, 2L, 0))
                .isInstanceOf(IllegalStationEdgeException.class)
                .hasMessageContaining(StationEdge.INVALID_DISTANCE_EXCEPTION_MESSAGE);
    }

    @Test
    @DisplayName("구간을 나눠 새로운 2개의 구간으로 나눈다. - 상행기준")
    void split_from_up_test() {
        // given
        final StationEdge originalStation = new StationEdge(1L, 2L, 8);

        // when
        final Set<StationEdge> splitStationEdges = originalStation.splitFromUp(3L, 3);

        // then
        final int upEdgeDistance = splitStationEdges.stream()
                .filter(stationEdge -> stationEdge.getUpStationId() == 1L && stationEdge.getDownStationId() == 3L)
                .mapToInt(StationEdge::getDistance)
                .findFirst()
                .getAsInt();
        final int downEdgeDistance = splitStationEdges.stream()
                .filter(stationEdge -> stationEdge.getUpStationId() == 3L && stationEdge.getDownStationId() == 2L)
                .mapToInt(StationEdge::getDistance)
                .findFirst()
                .getAsInt();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(upEdgeDistance).isEqualTo(3);
            softly.assertThat(downEdgeDistance).isEqualTo(5);
        });
    }

    @Test
    @DisplayName("구간을 나눠 새로운 2개의 구간으로 나눈다. - 하행기준")
    void split_from_down_test() {
        // given
        final StationEdge originalStation = new StationEdge(1L, 2L, 8);

        // when
        final Set<StationEdge> splitStationEdges = originalStation.splitFromDown(3L, 3);

        // then
        final int upEdgeDistance = splitStationEdges.stream()
                .filter(stationEdge -> stationEdge.getUpStationId() == 1L && stationEdge.getDownStationId() == 3L)
                .mapToInt(StationEdge::getDistance)
                .findFirst()
                .getAsInt();
        final int downEdgeDistance = splitStationEdges.stream()
                .filter(stationEdge -> stationEdge.getUpStationId() == 3L && stationEdge.getDownStationId() == 2L)
                .mapToInt(StationEdge::getDistance)
                .findFirst()
                .getAsInt();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(upEdgeDistance).isEqualTo(5);
            softly.assertThat(downEdgeDistance).isEqualTo(3);
        });
    }
}
