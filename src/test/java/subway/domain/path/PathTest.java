package subway.domain.path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class PathTest {

    @DisplayName("거리가 0 이하이면 예외가 발생한다")
    @Test
    void notPositiveDistance() {
        //given
        final Station station = new Station("서면역");

        //when, then
        assertThatThrownBy(() -> new Path(station, station, 0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("경로가 겹치는 지 테스트")
    @Nested
    class Overlapped {
        @DisplayName("상행역이 같으면 true")
        @Test
        void overlapped() {
            //given
            final Station station = new Station("서면");
            final Station station2 = new Station("해운대");
            final Station station3 = new Station("광안리");

            final Path path = new Path(station, station2, 3);
            final Path path2 = new Path(station, station3, 3);

            //when, then
            assertThat(path.isOverlapped(path2)).isTrue();
        }

        @DisplayName("하행역이 같으면 true")
        @Test
        void overlapped2() {
            //given
            final Station station = new Station("서면");
            final Station station2 = new Station("해운대");
            final Station station3 = new Station("광안리");

            final Path path = new Path(station, station2, 3);
            final Path path2 = new Path(station3, station2, 3);

            //when, then
            assertThat(path.isOverlapped(path2)).isTrue();
        }

        @DisplayName("상행역과 하행역이 모두 다르면 false")
        @Test
        void overlapped_false() {
            //given
            final Station station = new Station("서면");
            final Station station2 = new Station("해운대");
            final Station station3 = new Station("광안리");

            final Path path = new Path(station, station2, 3);
            final Path path2 = new Path(station2, station3, 3);

            //when, then
            assertThat(path.isOverlapped(path2)).isFalse();
        }
    }

    @DisplayName("겹치는 경로 나누기 테스트")
    @Nested
    class Divide {
        @DisplayName("한 경로를 새 두 경로로 나눌 수 있다 - 상행역이 겹칠 때")
        @Test
        void divide_up() {
            //given
            final Station station1 = new Station("서면역");
            final Station station2 = new Station("부산역");
            final Path path = new Path(station1, station2, 10);

            final Station station = new Station("전포역");
            final Path newPath = new Path(station1, station, 5);

            //when
            final List<Path> divided = path.divide(newPath);
            final Path path1 = divided.get(0);
            final Path path2 = divided.get(1);

            //then
            assertAll(
                    () -> assertThat(path1.getDistance() + path2.getDistance()).isEqualTo(path.getDistance()),
                    () -> assertThat(path1.getUp()).isEqualTo(path.getUp()),
                    () -> assertThat(path2.getDown()).isEqualTo(path.getDown()),
                    () -> assertThat(path1.getDown()).isEqualTo(path2.getUp()));
        }

        @DisplayName("한 경로를 새 두 경로로 나눌 수 있다 - 하행역이 겹칠 때")
        @Test
        void divide_down() {
            //given
            final Station station1 = new Station("서면역");
            final Station station2 = new Station("부산역");
            final Path path = new Path(station1, station2, 10);

            final Station station = new Station("전포역");
            final Path newPath = new Path(station, station2, 5);

            //when
            final List<Path> divided = path.divide(newPath);
            final Path path1 = divided.get(0);
            final Path path2 = divided.get(1);

            //then
            assertAll(
                    () -> assertThat(path1.getDistance() + path2.getDistance()).isEqualTo(path.getDistance()),
                    () -> assertThat(path1.getUp()).isEqualTo(path.getUp()),
                    () -> assertThat(path2.getDown()).isEqualTo(path.getDown()),
                    () -> assertThat(path1.getDown()).isEqualTo(path2.getUp()));
        }

        @DisplayName("한 경로 사이에 새 경로를 추가할 경우 기존 거리보다 크면 예외가 발생한다")
        @Test
        void divide_fail() {
            //given
            final Station station1 = new Station("서면역");
            final Station station2 = new Station("부산역");
            final Path path = new Path(station1, station2, 10);

            //when
            final Station station = new Station("전포역");
            final Path newPath = new Path(station1, station, 10);

            //then
            assertThatThrownBy(() -> path.divide(newPath))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("기존의 거리보다 길 수 없습니다.");
        }

        @DisplayName("경로를 나눌 때 겹치는 경로가 아니면 예외가 발생한다")
        @Test
        void divide_fail2() {
            //given
            final Station station = new Station("서면");
            final Station station2 = new Station("해운대");
            final Station station3 = new Station("광안리");

            final Path path = new Path(station, station2, 3);
            final Path path2 = new Path(station2, station3, 1);

            //when, then
            assertThatThrownBy(() -> path.divide(path2))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("두 경로가 겹치지 않습니다.");
        }
    }

    @DisplayName("경로 합치기 테스트")
    @Nested
    class Merge {
        @DisplayName("이어진 두 경로를 합칠 수 있다")
        @Test
        void merge() {
            //given
            final Station station1 = new Station("서면역");
            final Station station2 = new Station("부산역");
            final Station station3 = new Station("센텀역");
            final Path path = new Path(station1, station2, 10);
            final Path path2 = new Path(station2, station3, 10);

            //when
            final Path merged = path.merge(path2);

            //then
            assertThat(merged.getDistance()).isEqualTo(path.getDistance() + path2.getDistance());
        }

        @DisplayName("이어지지 않은 경로를 합치면 예외가 발생한다")
        @Test
        void merge_fail() {
            //given
            final Station station1 = new Station("서면역");
            final Station station2 = new Station("부산역");
            final Station station3 = new Station("센텀역");
            final Station station4 = new Station("해운대역");
            final Path path = new Path(station1, station2, 10);
            final Path path2 = new Path(station4, station3, 10);

            //when, then
            assertThatThrownBy(() -> path.merge(path2))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("두 경로를 합칠 수 없습니다.");
        }
    }
}
