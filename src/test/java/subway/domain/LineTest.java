package subway.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.domain.Direction.DOWN;
import static subway.domain.Direction.UP;

class LineTest {

    @DisplayName("역을 순서대로 정렬한다.")
    @Test
    void sortTest() {
        final Line line = new Line(1L, "1호선", "파랑", Map.of(
                new Station(1L, "성남"),
                new Path(new Station(3L, "성대"), 5)
                , new Station(3L, "성대"),
                new Path(new Station(2L, "강남"), 10)));

        final List<Station> stations = line.sortStations();

        assertThat(stations).map(Station::getName).containsExactly("성남", "성대", "강남");
    }

    @DisplayName("경로가 없을 때 정렬하면 빈 리스트를 반환한다.")
    @Test
    void sortEmptyTest() {
        final Line line = new Line(1L, "1호선", "blue", Map.of());

        final List<Station> stations = line.sortStations();

        assertThat(stations).isEmpty();
    }

    @Nested
    class 역을_추가한다 {

        final Station stationA = new Station(1L, "A");
        final Station stationB = new Station(3L, "B");
        final Station stationC = new Station(2L, "C");
        final Station stationD = new Station(4L, "D");
        final Line line = new Line(1L, "1호선", "파랑",
                new HashMap<>(Map.of(
                        stationA, new Path(stationB, 5)
                        , stationB, new Path(stationC, 10)
                ))
        );

        @Test
        void A_뒤에_역을_추가한다() {

            //when
            line.addPath(stationA, stationD, 3, DOWN);
            final Path pathAfterStationA = line.getPaths().get(stationA);
            final Path pathAfterNewStation = line.getPaths().get(stationD);

            //then
            assertAll(
                    () -> assertThat(pathAfterStationA.getNext()).isEqualTo(stationD),
                    () -> assertThat(pathAfterStationA.getDistance()).isEqualTo(3),
                    () -> assertThat(pathAfterNewStation.getDistance()).isEqualTo(2)
            );
        }

        @Test
        void 맨_앞에_역을_추가한다() {

            //when
            line.addPath(stationA, stationD, 3, UP);
            final Path path = line.getPaths().get(stationD);

            //then
            assertAll(
                    () -> assertThat(path.getNext()).isEqualTo(stationA),
                    () -> assertThat(path.getDistance()).isEqualTo(3),
                    () -> assertThat(line.getPaths().get(stationA).getNext()).isEqualTo(stationB)
            );
        }

        @Test
        void 맨_뒤에_역을_추가한다() {

            //when
            line.addPath(stationC, stationD, 4, DOWN);
            final Path path = line.getPaths().get(stationC);

            //then
            assertAll(
                    () -> assertThat(path.getNext()).isEqualTo(stationD),
                    () -> assertThat(path.getDistance()).isEqualTo(4)
            );
        }

        @Test
        void 역을_추가할_때_거리를_초과하면_예외를_던진다() {

            //when,then
            assertThatThrownBy(() -> line.addPath(stationA, stationD, 6, DOWN))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 역을_삭제한다 {

        final Station stationA = new Station(1L, "A");
        final Station stationB = new Station(3L, "B");
        final Station stationC = new Station(2L, "C");

        final Line line = new Line(1L, "1호선", "파랑",
                new HashMap<>(Map.of(
                        stationA, new Path(stationB, 5)
                        , stationB, new Path(stationC, 10)
                ))
        );

        @Test
        void 중간_역을_삭제한다() {
            //when
            line.removeStation(stationB);

            //then
            Assertions.assertAll(
                    () -> assertThat(line.getPaths().get(stationA).getDistance()).isEqualTo(15),
                    () -> assertThat(line.getPaths().get(stationA).getNext()).isEqualTo(stationC)
            );
        }

        @Test
        void 마지막_역을_삭제한다() {
            //when
            line.removeStation(stationC);

            //then
            Assertions.assertAll(
                    () -> assertThat(line.getPaths()).hasSize(1),
                    () -> assertThat(line.getPaths().get(stationB)).isNull()
            );
        }

        @Test
        void 처음_역을_삭제한다() {
            //when
            line.removeStation(stationA);

            //then
            Assertions.assertAll(
                    () -> assertThat(line.getPaths()).hasSize(1),
                    () -> assertThat(line.getPaths().get(stationA)).isNull()
            );
        }
    }
}
