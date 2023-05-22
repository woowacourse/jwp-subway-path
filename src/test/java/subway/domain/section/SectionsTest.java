package subway.domain.section;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.line.Direction;
import subway.domain.station.Station;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SectionsTest {

    @DisplayName("생성할 때 갈림길은 생성될 수 없다.")
    @Test
    void validate_fork_road_construct() {
        //given
        Station centerStation = new Station(1L, "center");
        Station main = new Station(2L, "main");
        Station branch = new Station(3L, "branch");

        //when
        Section section1 = new Section(centerStation, main, 10);
        Section section2 = new Section(centerStation, branch, 20);

        //then
        assertThatThrownBy(() -> new Sections(List.of(section1, section2)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("추가할 때 갈림길은 생성될 수 없다.")
    @Test
    void validate_fork_road_add_section() {
        //given
        Station centerStation = new Station(1L, "center");
        Station main = new Station(2L, "main");
        Station branch = new Station(3L, "branch");

        //when
        Section section1 = new Section(centerStation, main, 10);
        Sections sections = new Sections(List.of(section1));
        Section section2 = new Section(centerStation, branch, 20);

        //then
        assertThatThrownBy(() -> sections.addSection(section2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("영역을 분할하면 하나가 늘어난다.")
    @Test
    void split_section() {
        //given
        Station leftStation = new Station(1L, "left");
        Station innerStation = new Station(2L, "inner");
        Station rightStation = new Station(3L, "right");
        Section section1 = new Section(leftStation, rightStation, 10);
        Sections sections = new Sections(List.of(section1));

        //when
        int beforeSize = sections.size();
        sections.split(innerStation, leftStation, Direction.RIGHT, 5);
        int afterSize = sections.size();

        //then
        assertEquals(beforeSize + 1, afterSize);
    }

    @DisplayName("분할하는 영역이 더 크면 실패한다.")
    @Test
    void fail_split_section_over_inner_distance() {
        //given
        Station leftStation = new Station(1L, "left");
        Station innerStation = new Station(2L, "inner");
        Station rightStation = new Station(3L, "right");
        Section section1 = new Section(leftStation, rightStation, 10);
        Sections sections = new Sections(List.of(section1));

        //when + then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> sections.split(innerStation, leftStation, Direction.RIGHT, 20));
        assertEquals("사이에 들어갈 역의 거리는 기존 거리보다 작아야 합니다.", exception.getMessage());
    }

    @DisplayName("왼쪽 역을 기준으로 영역을 가져온다.")
    @Test
    void get_section_by_left() {
        //given
        Station leftStation = new Station(1L, "left");
        Station innerStation = new Station(2L, "inner");
        Station rightStation = new Station(3L, "right");
        Section section1 = new Section(1L, leftStation, innerStation, 10);
        Section section2 = new Section(2L, innerStation, rightStation, 10);
        Sections sections = new Sections(List.of(section1, section2));

        //when + then
        assertEquals(section1, sections.getSectionByLeftStation(leftStation));
        assertEquals(section2, sections.getSectionByLeftStation(innerStation));
    }

    @DisplayName("왼쪽 역을 기준으로 하는 영역이 없는 경우 조회를 실패한다.")
    @Test
    void fail_get_not_exist_section_by_left() {
        //given
        Station leftStation = new Station(1L, "left");
        Sections sections = new Sections(List.of());

        //when + then
        assertThrows(IllegalArgumentException.class, () -> sections.getSectionByLeftStation(leftStation));
    }

    @DisplayName("오른쪽 역을 기준으로 영역을 가져온다.")
    @Test
    void get_section_by_right() {
        //given
        Station leftStation = new Station(1L, "left");
        Station innerStation = new Station(2L, "inner");
        Station rightStation = new Station(3L, "right");
        Section section1 = new Section(1L, leftStation, innerStation, 10);
        Section section2 = new Section(2L, innerStation, rightStation, 10);
        Sections sections = new Sections(List.of(section1, section2));

        //when + then
        assertEquals(section1, sections.getSectionByRightStation(innerStation));
        assertEquals(section2, sections.getSectionByRightStation(rightStation));
    }

    @DisplayName("오른쪽 역을 기준으로 하는 영역이 없는 경우 조회를 실패한다.")
    @Test
    void fail_get_not_exist_section_by_right() {
        //given
        Station rightStation = new Station(1L, "right");
        Sections sections = new Sections(List.of());

        //when + then
        assertThrows(IllegalArgumentException.class, () -> sections.getSectionByLeftStation(rightStation));
    }

    @DisplayName("가장자리 역을 제거해 영역을 제거한다.")
    @Test
    void delete_edge_section() {
        //given
        Station leftStation = new Station(1L, "left");
        Station innerStation = new Station(2L, "inner");
        Station rightStation = new Station(3L, "right");
        Section section1 = new Section(1L, leftStation, innerStation, 10);
        Section section2 = new Section(2L, innerStation, rightStation, 10);
        Sections sections = new Sections(List.of(section1, section2));

        //when
        int beforeSize = sections.size();
        sections.delete(section1);
        int afterSize = sections.size();

        //then
        assertEquals(beforeSize - 1, afterSize);
    }

    @DisplayName("가운데 역을 제거해 영역을 하나 합친다.")
    @Test
    void delete_inner_section() {
        //given
        Station leftStation = new Station(1L, "left");
        Station innerStation = new Station(2L, "inner");
        Station rightStation = new Station(3L, "right");
        Section section1 = new Section(1L, leftStation, innerStation, 10);
        Section section2 = new Section(2L, innerStation, rightStation, 10);
        Sections sections = new Sections(List.of(section1, section2));

        //when
        int beforeSize = sections.size();
        sections.mixSection(section1, section2);
        int afterSize = sections.size();

        //then
        assertEquals(beforeSize - 1, afterSize);
    }
}
