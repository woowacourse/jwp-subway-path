package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathsTest {

    private final Station station = new Station("서면");
    private final Station station2 = new Station("해운대");
    private final Station station3 = new Station("광안리");
    private final Station station4 = new Station("전포");

    @DisplayName("경로가 하나도 없을 때 경로를 추가할 수 있다")
    @Test
    void addPath() {
        //given
        Paths paths = new Paths();

        //when, then
        paths = paths.addPath(new Path(station, station2, 3));

        //then
        assertThat(paths.getPaths()).hasSize(1);
    }

    @DisplayName("다른 경로가 있을 때 이어져있지 않은 경로를 추가하면 예외가 발생한다")
    @Test
    void addPath_fail() {
        //given
        final Path path = new Path(station, station2, 3);
        final Paths paths = new Paths(List.of(path));

        //when, then
        final Path path2 = new Path(station3, station4, 3);
        assertThatThrownBy(() -> paths.addPath(path2))
                .hasMessage("기존의 역과 이어져야 합니다.");
    }

    @DisplayName("다른 경로가 있을 때 존재하는 두 역의 새 경로를 추가하면 예외가 발생한다")
    @Test
    void addPath_fail2() {
        //given
        final Path path = new Path(station, station2, 3);
        final Paths paths = new Paths(List.of(path));

        //when, then
        final Path path2 = new Path(station, station2, 5);
        assertThatThrownBy(() -> paths.addPath(path2))
                .hasMessage("이미 존재하는 경로입니다.");
    }

    @DisplayName("다른 경로가 있을 때 사이에 경로를 추가할 수 있다")
    @Test
    void addPath_between() {
        //given
        final Path path = new Path(station, station2, 3);
        Paths paths = new Paths(List.of(path));

        //when, then
        final Path path2 = new Path(station, station3, 1);
        paths = paths.addPath(path2);

        //then
        assertThat(paths.getPaths()).hasSize(2);
    }
}
