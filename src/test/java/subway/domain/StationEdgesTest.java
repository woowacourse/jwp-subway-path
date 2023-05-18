package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StationEdgesTest {
    private static Long upStationId = 2L;
    private static Long downStationId = 1L;
    private static int distance = 5;

    @Test
    @DisplayName("상행 방향으로 역을 추가한다.")
    void insertUpStation() {
        //given
        StationEdge upStationEdge = new StationEdge(upStationId, 0);
        StationEdge adjacentStationEdge = new StationEdge(downStationId, distance);
        List<StationEdge> beforeEdges = new ArrayList<>(new ArrayList<>(Arrays.asList(upStationEdge, adjacentStationEdge)));
        StationEdges stationEdges = StationEdges.from(beforeEdges);

        //when
        Long newStationId = 3L;
        System.out.println(stationEdges.getStationEdges());
        stationEdges.addStationUpperFrom(newStationId, downStationId, Distance.from(2));

        List<StationEdge> afterEdges = stationEdges.getStationEdges();

        System.out.println(stationEdges.getStationEdges());
        Optional<StationEdge> addedEdge = afterEdges.stream()
                .filter(stationEdge -> stationEdge.getDownStationId().equals(newStationId))
                .findFirst();

        //then
        assertSoftly(softly -> {
            softly.assertThat(addedEdge).isNotEmpty();
            softly.assertThat(afterEdges.indexOf(addedEdge.get()))
                    .isEqualTo(1);
        });
    }

    @Test
    @DisplayName("하행 방향으로 역을 추가한다.")
    void insertDownStation() {
        //given
        StationEdge upStationEdge = new StationEdge(upStationId, 0);
        StationEdge adjacentStationEdge = new StationEdge(downStationId, distance);
        StationEdges stationEdges = StationEdges.from(new ArrayList<>(Arrays.asList(upStationEdge, adjacentStationEdge)));

        //when
        Long newStationId = 3L;
        stationEdges.addStationDownFrom(newStationId, upStationId, Distance.from(2)); // 상행 종점 거리 2 아래에 역을 추가한다.


        //then
        List<StationEdge> afterEdges = stationEdges.getStationEdges();

        Optional<StationEdge> insertedMiddleEdge = afterEdges.stream()
                .filter(stationEdge -> stationEdge.getDownStationId().equals(newStationId))
                .findFirst();

        assertSoftly(softly -> {
            softly.assertThat(insertedMiddleEdge).isNotEmpty();
            softly.assertThat(insertedMiddleEdge.get().getDistance()).isEqualTo(Distance.from(2));
            softly.assertThat(afterEdges.indexOf(insertedMiddleEdge.get()))
                    .isEqualTo(1);
        });
    }

    @Test
    @DisplayName("하행 종점을 추가한다.")
    void insertDownEndStation() {
        //given
        StationEdge upStationEdge = new StationEdge(upStationId, 0);
        StationEdge adjacentStationEdge = new StationEdge(downStationId, distance);
        StationEdges stationEdges = StationEdges.from(new ArrayList<>(Arrays.asList(upStationEdge, adjacentStationEdge)));

        //when
        Long newStationId = 3L;
        stationEdges.addStationDownFrom(newStationId, downStationId, Distance.from(2));

        //then
        List<StationEdge> edgesAfterAddition = stationEdges.getStationEdges();
        assertSoftly(softly -> {
            softly.assertThat(edgesAfterAddition.get(2).getDistance()).isEqualTo(Distance.from(2));
            softly.assertThat(edgesAfterAddition).hasSize(3);
        });
    }

    @Test
    @DisplayName("노선에서 역을 제거한다.")
    void deleteStation() {
        //given
        StationEdge upStationEdge = new StationEdge(upStationId, 0);
        StationEdge adjacentStationEdge = new StationEdge(downStationId, distance);
        StationEdges stationEdges = StationEdges.from(new ArrayList<>(Arrays.asList(upStationEdge, adjacentStationEdge)));

        stationEdges.addStationDownFrom(3L, upStationId, Distance.from(2));

        //when
        StationEdge changedStationEdge = stationEdges.deleteStation(3L);

        //then
        assertSoftly(softly -> {
            softly.assertThat(changedStationEdge.getDownStationId()).isEqualTo(downStationId);
            softly.assertThat(changedStationEdge.getDistance().getValue()).isEqualTo(Distance.from(distance).getValue());
        });
    }

    @Test
    @DisplayName("노선에 역이 존재하는지 확인한다.")
    void contains() {
        //given
        StationEdge upStationEdge = new StationEdge(upStationId, 0);
        StationEdge adjacentStationEdge = new StationEdge(downStationId, distance);
        StationEdges stationEdges = StationEdges.from(new ArrayList<>(Arrays.asList(upStationEdge, adjacentStationEdge)));
        //when
        boolean contains = stationEdges.contains(upStationId);
        //then
        assertThat(contains).isTrue();
    }

    @Test
    @DisplayName("역이 2개 이상이면 삭제 가능하다.")
    void canDeleteStation() {
        //given
        StationEdge upStationEdge = new StationEdge(upStationId, 0);
        StationEdge adjacentStationEdge = new StationEdge(downStationId, distance);
        StationEdges stationEdges = StationEdges.from(new ArrayList<>(Arrays.asList(upStationEdge, adjacentStationEdge)));

        stationEdges.addStationDownFrom(3L, upStationId, Distance.from(2));

        //when
        boolean canDeleteStation = stationEdges.canDeleteStation();

        //then
        assertThat(canDeleteStation).isTrue();
    }

    @Test
    @DisplayName("역이 2개이면 삭제가 불가능하다.")
    void cannotDeleteStation() {
        //given
        StationEdge upStationEdge = new StationEdge(upStationId, 0);
        StationEdge adjacentStationEdge = new StationEdge(downStationId, distance);
        StationEdges stationEdges = StationEdges.from(new ArrayList<>(Arrays.asList(upStationEdge, adjacentStationEdge)));

        //when
        boolean canDeleteStation = stationEdges.canDeleteStation();

        //then
        assertThat(canDeleteStation).isFalse();
    }
}