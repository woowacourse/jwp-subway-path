package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.line.edge.StationEdge;
import subway.domain.line.edge.StationEdges;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class StationEdgesTest {

    @Test
    @DisplayName("상행 종점 역 추가한다")
    void insert_up_end_station_test() {
        // given
        final StationEdge firstEdge = new StationEdge(1L, 2L, 5);
        final StationEdge secondEdge = new StationEdge(2L, 3L, 7);
        final StationEdges stationEdges = new StationEdges(Set.of(firstEdge, secondEdge));

        // when
        stationEdges.addStation(4L, 1L, LineDirection.UP, 3);

        // then
        final Long actualUpEndStationId = stationEdges.findUpEndStationId();
        assertThat(actualUpEndStationId).isEqualTo(4L);
    }

    @Test
    @DisplayName("하행 종점 역 추가한다")
    void insert_down_end_station_test() {
        // given
        final StationEdge firstEdge = new StationEdge(1L, 2L, 5);
        final StationEdge secondEdge = new StationEdge(2L, 3L, 7);
        final StationEdges stationEdges = new StationEdges(Set.of(firstEdge, secondEdge));

        // when
        stationEdges.addStation(4L, 3L, LineDirection.DOWN, 3);

        // then
        final Long actualDownEndStationId = stationEdges.findDownEndStationId();
        assertThat(actualDownEndStationId).isEqualTo(4L);
    }

    @Test
    @DisplayName("중간 역 추가한다")
    void insert_middle_station_test() {
        // given
        final StationEdge firstEdge = new StationEdge(1L, 2L, 5);
        final StationEdge secondEdge = new StationEdge(2L, 3L, 7);
        final StationEdges stationEdges = new StationEdges(Set.of(firstEdge, secondEdge));

        // when
        stationEdges.addStation(4L, 2L, LineDirection.DOWN, 3);

        // then
        final Set<StationEdge> edges = stationEdges.toSet();
        final StationEdge insertedUpEdge = edges.stream()
                .filter(edge -> edge.isUpStationId(2L) && edge.isDownStationId(4L))
                .findFirst()
                .get();
        final StationEdge insertedDownEdge = edges.stream()
                .filter(edge -> edge.isUpStationId(4L) && edge.isDownStationId(3L))
                .findFirst()
                .get();

        assertSoftly(softly -> {
            softly.assertThat(edges).hasSize(3);
            softly.assertThat(insertedUpEdge.getDistance()).isEqualTo(3);
            softly.assertThat(insertedDownEdge.getDistance()).isEqualTo(4);
        });
    }

    @Test
    @DisplayName("중간역을 제거한다")
    void remove_station_test() {
        // given
        final StationEdge firstEdge = new StationEdge(1L, 2L, 5);
        final StationEdge secondEdge = new StationEdge(2L, 3L, 7);
        final StationEdge thirdEdge = new StationEdge(3L, 4L, 3);
        final StationEdges stationEdges = new StationEdges(Set.of(firstEdge, secondEdge, thirdEdge));

        // when
        stationEdges.removeStation(3L);

        // then
        final Set<StationEdge> edges = stationEdges.toSet();
        final StationEdge mergedEdge = edges.stream()
                .filter(edge -> edge.isUpStationId(2L) && edge.isDownStationId(4L))
                .findFirst()
                .get();
        assertSoftly(softly -> {
            softly.assertThat(edges).hasSize(2);
            softly.assertThat(mergedEdge.getDistance()).isEqualTo(10);
        });
    }

    @Test
    @DisplayName("상행종점역을 제거한다.")
    void remove_up_end_station_test() {
        // given
        final StationEdge firstEdge = new StationEdge(1L, 2L, 5);
        final StationEdge secondEdge = new StationEdge(2L, 3L, 7);
        final StationEdge thirdEdge = new StationEdge(3L, 4L, 3);
        final StationEdges stationEdges = new StationEdges(Set.of(firstEdge, secondEdge, thirdEdge));

        // when
        stationEdges.removeStation(1L);

        // then
        final Set<StationEdge> edges = stationEdges.toSet();
        final Long actualUpEndStationId = stationEdges.findUpEndStationId();
        assertSoftly(softly -> {
            softly.assertThat(edges).hasSize(2);
            softly.assertThat(actualUpEndStationId).isEqualTo(2L);
        });
    }

    @Test
    @DisplayName("하행종점역을 제거한다.")
    void remove_down_end_station_test() {
        // given

        final StationEdge firstEdge = new StationEdge(1L, 2L, 5);
        final StationEdge secondEdge = new StationEdge(2L, 3L, 7);
        final StationEdge thirdEdge = new StationEdge(3L, 4L, 3);
        final StationEdges stationEdges = new StationEdges(Set.of(firstEdge, secondEdge, thirdEdge));

        // when
        stationEdges.removeStation(4L);

        // then
        final Set<StationEdge> edges = stationEdges.toSet();
        final Long actualDownEndStationId = stationEdges.findDownEndStationId();
        assertSoftly(softly -> {
            softly.assertThat(edges).hasSize(2);
            softly.assertThat(actualDownEndStationId).isEqualTo(3L);
        });
    }

    @Test
    @DisplayName("등록된 역의 id를 순서대로 반환한다")
    void get_station_ids_in_order_test() {
        // given
        final StationEdge firstEdge = new StationEdge(1L, 3L, 5);
        final StationEdge secondEdge = new StationEdge(3L, 2L, 7);
        final StationEdge thirdEdge = new StationEdge(2L, 4L, 7);
        final StationEdges stationEdges = new StationEdges(Set.of(firstEdge, secondEdge, thirdEdge));

        // when
        final List<Long> stationIds = stationEdges.getStationIdsInOrder();

        // then
        assertThat(stationIds).containsExactly(1L, 3L, 2L, 4L);
    }
}
