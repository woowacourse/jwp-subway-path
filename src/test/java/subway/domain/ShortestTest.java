package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.domain.path.Path;
import subway.domain.path.PathEdgeProxy;
import subway.domain.path.Paths;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ShortestTest {

    @DisplayName("Line들을 받아서 그래프를 만들 수 있다.")
    @Test
    void construct() {
        assertDoesNotThrow(() -> Shortest.from(List.of(new Line("1호선", "blue"))));
    }

    @Nested
    @DisplayName("최단 경로 찾기 테스트")
    class FindShortest {

        /**
         * 광안 <-5-> 전포 <-5-> 노포
         * 광안 <-2-> 부산 <-1-> 해운대 <-1-> 노포
         * expect: 4
         */
        @DisplayName("여러 경로 중 최단 경로를 구할 수 있다")
        @Test
        void findShortest() {
            //given
            final Station source = new Station("광안");
            final Station target = new Station("노포");

            final Station station1 = new Station("전포");
            final Station station2 = new Station("부산");
            final Station station3 = new Station("해운대");

            final Path path1 = new Path(source, station1, 5);
            final Path path2 = new Path(station1, target, 5);

            final Path path3 = new Path(source, station2, 2);
            final Path path4 = new Path(station2, station3, 1);
            final Path path5 = new Path(station3, target, 1);

            final Paths paths1 = new Paths(List.of(path1, path2));
            final Paths paths2 = new Paths(List.of(path3, path4, path5));

            final Line line1 = new Line("1호선", "blue", paths1);
            final Line line2 = new Line("2호선", "red", paths2);
            final Shortest shortest = Shortest.from(List.of(line1, line2));

            //when
            final List<PathEdgeProxy> found = shortest.findShortest(source, target);
            final long distance = found.stream().mapToLong(PathEdgeProxy::getDistance).sum();

            //then
            assertAll(
                    () -> assertThat(distance).isEqualTo(4),
                    () -> assertThat(found).hasSize(3));
        }

        /**
         * 광안 <-5-> 전포 <-5-> 노포
         * 광안 <-2-> 부산 <-1-> 해운대 <-1-> 노포
         * |
         * 1
         * |
         * 다대포 <-1-> 노포
         * expect: 2
         */
        @DisplayName("여러 경로 중 최단 경로를 구할 수 있다")
        @Test
        void findShortest2() {
            //given
            final Station source = new Station("광안");
            final Station target = new Station("노포");

            final Station station1 = new Station("전포");
            final Station station2 = new Station("부산");
            final Station station3 = new Station("해운대");
            final Station station4 = new Station("다대포");

            final Path path1 = new Path(source, station1, 5);
            final Path path2 = new Path(station1, target, 5);

            final Path path3 = new Path(source, station2, 2);
            final Path path4 = new Path(station2, station3, 1);
            final Path path5 = new Path(station3, target, 1);

            final Path path6 = new Path(source, station4, 1);
            final Path path7 = new Path(station4, target, 1);

            final Paths paths1 = new Paths(List.of(path1, path2));
            final Paths paths2 = new Paths(List.of(path3, path4, path5));
            final Paths paths3 = new Paths(List.of(path6, path7));

            final Line line1 = new Line("1호선", "blue", paths1);
            final Line line2 = new Line("2호선", "red", paths2);
            final Line line3 = new Line("2호선", "yellow", paths3);
            final Shortest shortest = Shortest.from(List.of(line1, line2, line3));

            //when
            final List<PathEdgeProxy> found = shortest.findShortest(source, target);
            final long distance = found.stream().mapToLong(PathEdgeProxy::getDistance).sum();

            //then
            assertAll(
                    () -> assertThat(distance).isEqualTo(2),
                    () -> assertThat(found).hasSize(2));
        }

        @DisplayName("존재하지 않는 경로로 최단 경로를 조회하면 빈 경로가 반환된다")
        @Test
        void findShortest_fail() {
            //given
            final Station source = new Station("광안");
            final Station target = new Station("노포");

            final Station station1 = new Station("전포");
            final Station station2 = new Station("부산");

            final Path path1 = new Path(source, station1, 5);
            final Path path2 = new Path(target, station2, 5);

            final Paths paths1 = new Paths(List.of(path1));
            final Paths paths2 = new Paths(List.of(path2));

            final Line line1 = new Line("1호선", "blue", paths1);
            final Line line2 = new Line("2호선", "resd", paths2);
            final Shortest shortest = Shortest.from(List.of(line1, line2));

            //when
            final List<PathEdgeProxy> found = shortest.findShortest(source, target);
            final long distance = found.stream().mapToLong(PathEdgeProxy::getDistance).sum();

            //then
            assertAll(
                    () -> assertThat(distance).isZero(),
                    () -> assertThat(found).isEmpty());
        }
    }
}
