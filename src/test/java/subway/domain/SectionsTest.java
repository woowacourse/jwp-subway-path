package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.exception.DuplicateException;
import subway.exception.InvalidException;

class SectionsTest {
    private Sections sections;

    @BeforeEach
    void setUp() {
        Section section1 = new Section(new Station(1L, "잠실역"), new Station(2L, "잠실새내역"), 10);
        Section section2 = new Section(new Station(2L, "잠실새내역"), new Station(3L, "봉천역"), 4);
        sections = Sections.from(List.of(section2, section1));
    }

    @DisplayName("Sections가 생성되면 자동으로 순서대로 정렬된다.")
    @Test
    void createFrom() {
        // given
        Section section1 = new Section(new Station(1L, "잠실역"), new Station(2L, "잠실새내역"), 10);
        Section section2 = new Section(new Station(2L, "잠실새내역"), new Station(3L, "봉천역"), 4);

        // when
        sections = Sections.from(List.of(section2, section1));

        // then
        assertAll(
                () -> assertThat(sections.getSections().get(0)).isEqualTo(section1),
                () -> assertThat(sections.getSections().get(1)).isEqualTo(section2)
        );
    }

    @DisplayName("구간 내의 모든 역을 반환한다.")
    @Test
    void getStations() {
        // when
        List<Station> stations = sections.getStations();

        // then
        assertThat(stations).contains(
                new Station(1L, "잠실역"),
                new Station(2L, "잠실새내역"),
                new Station(3L, "봉천역"));
    }

    @DisplayName("[구간 추가] A-B-C 구간이 연결되어 있을 때")
    @Nested
    class addSection {
        @DisplayName("새로운 B-D 구간이 B-C 사이에 추가된다.")
        @Test
        void addSectionInMiddle() {
            // given
            Station newStation = new Station(4L, "종합운동장역");
            Section newSection = new Section(newStation, new Station(3L, "봉천역"), 2);

            // when
            sections.addSection(newSection);

            // then
            assertAll(
                    () -> assertThat(sections.getStations()).hasSize(4),
                    () -> assertThat(sections.getSections().get(1)).isEqualTo(newSection)
            );
        }

        @DisplayName("연결되어 있는 A-C 구간이 입력될 경우 예외가 발생한다.")
        @Test
        void validateDuplicateSection() {
            // given
            Section newSection = new Section(new Station(1L, "잠실역"), new Station(3L, "봉천역"), 2);

            // then
            assertThatThrownBy(() -> sections.addSection(newSection))
                    .isInstanceOf(DuplicateException.class)
                    .hasMessage("연결되어 있는 구간입니다.");
        }

        @DisplayName("새로운 B-D 구간이 B-C 사이에 추가될 때 B-D 구간의 거리가 B-C 거리보다 크거나 같으면 예외가 발생한다.")
        @Test
        void validateDistance() {
            // given
            Station newStation = new Station(4L, "종합운동장역");
            Section newSection = new Section(new Station(2L, "잠실새내역"), newStation, 4);

            // then
            assertThatThrownBy(() -> sections.addSection(newSection))
                    .isInstanceOf(InvalidException.class)
                    .hasMessage("기존에 존재하는 역 사이의 거리보다 작아야 합니다.");
        }

        @DisplayName("새로운 D-A 구간이 A-B 앞에 추가된다.")
        @Test
        void addSectionFirstStation() {
            // given
            Station newStation = new Station(4L, "베로역");
            Section newSection = new Section(newStation, new Station(1L, "잠실역"), 10);

            // when
            sections.addSection(newSection);

            // then
            assertThat(sections.getSections().get(0)).isEqualTo(newSection);
        }

        @DisplayName("새로운 C-D 구간이 B-C 뒤에 추가된다.")
        @Test
        void addSectionLastStation() {
            // given
            Station newStation = new Station(4L, "베로역");
            Section newSection = new Section(new Station(3L, "봉천역"), newStation, 10);

            // when
            sections.addSection(newSection);

            // then
            assertThat(sections.getSections().get(2)).isEqualTo(newSection);
        }
    }

    @DisplayName("[구간 삭제]")
    @Nested
    class deleteSection {
        @DisplayName("A-B-C 구간이 연결되어 있을 때 B 역과 연결된 구간을 삭제하면 A-B, B-C 구간이 삭제되고 A-C 구간이 추가된다.")
        @Test
        void delete() {
            // when
            sections.deleteSection(new Station(2L, "잠실새내역"));

            // then
            assertAll(
                    () -> assertThat(sections.getStations()).hasSize(2),
                    () -> assertThat(sections.getSections()).hasSize(1)
            );
        }

        @DisplayName("A-B-C 구간이 연결되어 있을 때 A 역과 연결된 삭제하면 A-B 구간이 삭제된다.")
        @Test
        void deleteTerminal() {
            // when
            sections.deleteSection(new Station(1L, "잠실역"));

            // then
            assertAll(
                    () -> assertThat(sections.getStations()).hasSize(2),
                    () -> assertThat(sections.getSections()).hasSize(1)
            );
        }

        @DisplayName("A-B 구간 하나만 존재할 때 역을 삭제하면 모든 구간이 삭제된다.")
        @Test
        void deleteAll() {
            // given
            Section section1 = new Section(new Station(1L, "잠실역"), new Station(2L, "잠실새내역"), 10);
            sections = Sections.from(List.of(section1));

            // when
            sections.deleteSection(new Station(1L, "잠실역"));

            // then
            assertAll(
                    () -> assertThat(sections.getStations()).hasSize(0),
                    () -> assertThat(sections.getSections()).hasSize(0)
            );
        }
    }
}
