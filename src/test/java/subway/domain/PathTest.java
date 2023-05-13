package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

    @DisplayName("상행역이 같으면 true")
    @Test
    void isUpStationEquals() {
        //given
        final Station station = new Station("서면");
        final Station station2 = new Station("해운대");
        final Station station3 = new Station("광안리");

        final Path path = new Path(station, station2, 3);
        final Path path2 = new Path(station, station3, 3);

        //when, then
        assertThat(path.isUpStationEquals(path2)).isTrue();
    }

    @DisplayName("상행역이 다르면 true")
    @Test
    void isUpStationEquals_false() {
        //given
        final Station station = new Station("서면");
        final Station station2 = new Station("해운대");
        final Station station3 = new Station("광안리");

        final Path path = new Path(station, station2, 3);
        final Path path2 = new Path(station2, station3, 3);

        //when, then
        assertThat(path.isUpStationEquals(path2)).isFalse();
    }

    @DisplayName("하행역이 같으면 true")
    @Test
    void isDownStationEquals() {
        //given
        final Station station = new Station("서면");
        final Station station2 = new Station("해운대");
        final Station station3 = new Station("광안리");

        final Path path = new Path(station, station2, 3);
        final Path path2 = new Path(station3, station2, 3);

        //when, then
        assertThat(path.isDownStationEquals(path2)).isTrue();
    }

    @DisplayName("하행역이 다르면 false")
    @Test
    void isDownStationEquals_false() {
        //given
        final Station station = new Station("서면");
        final Station station2 = new Station("해운대");
        final Station station3 = new Station("광안리");

        final Path path = new Path(station, station3, 3);
        final Path path2 = new Path(station3, station2, 3);

        //when, then
        assertThat(path.isDownStationEquals(path2)).isFalse();
    }

    @DisplayName("한 경로를 새 두 경로로 나눌 수 있다")
    @Test
    void divideBy() {
        //given
        final Station station1 = new Station("서면역");
        final Station station2 = new Station("부산역");
        final Path path = new Path(station1, station2, 10);

        final Station station = new Station("전포역");
        final Path newPath = new Path(station1, station, 5);

        //when
        final List<Path> divided = path.divideBy(newPath);
        final Path path1 = divided.get(0);
        final Path path2 = divided.get(1);

        //then
        assertAll(
                () -> assertThat(path1.getDistance() + path2.getDistance()).isEqualTo(path.getDistance()),
                () -> assertThat(path1.getUp()).isEqualTo(path.getUp()),
                () -> assertThat(path2.getDown()).isEqualTo(path.getDown()));
    }

    @DisplayName("한 경로 사이에 새 경로를 추가할 경우 기존 거리보다 크면 예외가 발생한다")
    @Test
    void divideBy_fail() {
        //given
        final Station station1 = new Station("서면역");
        final Station station2 = new Station("부산역");
        final Path path = new Path(station1, station2, 10);

        //when
        final Station station = new Station("전포역");
        final Path newPath = new Path(station1, station, 10);

        //then
        assertThatThrownBy(() -> path.divideBy(newPath))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("기존의 거리보다 길 수 없습니다.");
    }

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
}
