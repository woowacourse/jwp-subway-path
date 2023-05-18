package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.GlobalException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.domain.PathTest.Data.*;

class PathTest {
    @DisplayName("최단 경로와 이동 거리를 찾을 수 있다.")
    @Test
    void findShortestPath() {
        //given
        List<Station> stations = List.of(잠실, 건대입구, 왕십리, 청량리);
        List<Section> sections = List.of(잠실_건대입구, 건대입구_왕십리, 왕십리_청량리);
        Path path = new Path(stations, sections);

        //when
        List<Station> shortestPath = path.getShortestPath(건대입구, 청량리);
        Distance shortestDistance = path.getShortestDistance(건대입구, 청량리);

        //then
        assertThat(shortestPath).containsExactly(건대입구, 왕십리, 청량리);
        assertThat(shortestDistance.getDistance()).isEqualTo(16);
    }

    @DisplayName("연결되어 있지 않은 경로로는 이동할 수 없다.")
    @Test
    void findPathFailTest() {
        List<Station> stations = List.of(잠실, 건대입구, 왕십리, 청량리);
        List<Section> sections = List.of(잠실_건대입구, 왕십리_청량리);
        Path path = new Path(stations, sections);

        assertThatThrownBy(() -> path.getShortestPath(잠실, 청량리))
                .isInstanceOf(GlobalException.class)
                .hasMessage("이동할 수 없는 경로입니다.");
    }

    static class Data {
        static Station 잠실 = new Station("잠실");
        static Station 건대입구 = new Station("건대입구");
        static Station 왕십리 = new Station("왕십리");
        static Station 청량리 = new Station("청량리");

        static Section 잠실_건대입구 = new Section(잠실, 건대입구, new Distance(5));
        static Section 건대입구_왕십리 = new Section(건대입구, 왕십리, new Distance(8));
        static Section 왕십리_청량리 = new Section(왕십리, 청량리, new Distance(8));


    }

}
