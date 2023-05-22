package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {

    private Sections sections = new Sections(List.of(
        new Section(new Station("강남역"), new Station("교대역"), Distance.from(10)),
        new Section(new Station("교대역"), new Station("잠실역"), Distance.from(5))));

    @Test
    @DisplayName("노선이 비어있음다면 true가 반환된다.")
    void isEmpty() {
        Line line = new Line(1L, "2호선", "green", new Sections(Collections.emptyList()));
        assertThat(line.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("노선에 역이 존재한다면 true가 반환된다.")
    void hasStationInLine_true() {
        Line line = new Line(1L, "2호선", "green", sections);
        assertThat(line.hasStationInLine(new Station("강남역"))).isTrue();
    }

    @Test
    @DisplayName("노선에 찾는 역이 없다면 falsae가 반환된다.")
    void hasStationInLine_false() {
        Line line = new Line(1L, "2호선", "green", sections);
        assertThat(line.hasStationInLine(new Station("역삼역"))).isFalse();
    }

    @Test
    @DisplayName("노선에 해당 역을 하행역으로 가지는 구간을 찾는다.")
    void findSectionWithEndStation() {
        Line line = new Line(1L, "2호선", "green", sections);
        assertThat(line.findSectionWithEndStation(new Station("교대역")))
            .isPresent()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(new Section(new Station("강남역"), new Station("교대역"), Distance.from(10)));
    }

    @Test
    @DisplayName("노선에 해당 역을 상행역으로 가지는 구간을 찾는다.")
    void findSectionWithStartStation() {
        Line line = new Line(1L, "2호선", "green", sections);
        assertThat(line.findSectionWithStartStation(new Station("교대역")))
            .isPresent()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(new Section(new Station("교대역"), new Station("잠실역"), Distance.from(5)));
    }
}