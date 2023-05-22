package subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.domain.station.Station;

class SectionsTest {

    @Test
    @DisplayName("새로운 구간을 추가한다.")
    void add() {
        final Sections sections = new Sections(new ArrayList<>());
        final Section section = new Section(new Station("잠실역"), new Station("잠실새내역"), 10);

        sections.add(section);

        assertThat(sections.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("특정 위치에 새로운 구간을 추가한다.")
    void addAtPosition() {
        final Sections sections = new Sections(new ArrayList<>());
        final Section oldSection = new Section(new Station("잠실역"), new Station("잠실새내역"), 10);
        sections.add(oldSection);
        final Section newSection = new Section(new Station("강남역"), new Station("잠실역"), 10);

        sections.add(0, newSection);

        assertAll(
                () -> assertThat(sections.size()).isEqualTo(2),
                () -> assertThat(sections.findSectionByPosition(0)).isEqualTo(newSection),
                () -> assertThat(sections.findSectionByPosition(1)).isEqualTo(oldSection)
        );
    }

    @Test
    @DisplayName("특정 위치의 구간을 조회한다.")
    void findSectionByPosition() {
        final Sections sections = new Sections(new ArrayList<>());
        final Section section = new Section(new Station("잠실역"), new Station("잠실새내역"), 10);
        sections.add(0, section);

        final Section findSection = sections.findSectionByPosition(0);

        assertThat(findSection).isEqualTo(section);
    }

    @Test
    @DisplayName("특정 위치의 구간을 삭제한다.")
    void deleteByPosition() {
        final Sections sections = new Sections(new ArrayList<>());
        final Section section = new Section(new Station("잠실역"), new Station("잠실새내역"), 10);
        sections.add(0, section);

        sections.deleteByPosition(0);

        assertThat(sections.size()).isEqualTo(0);
    }

    @Nested
    @DisplayName("findPosition 메서드는 ")
    class FindPosition {

        @Test
        @DisplayName("역이 상행역으로 존재하는 구간 위치를 찾는다.")
        void findPosition() {
            final Sections sections = new Sections(new ArrayList<>());
            final Station upward = new Station(1L, "잠실역");
            final Station downward = new Station(2L, "잠실새내역");
            final Section section = new Section(upward, downward, 10);
            sections.add(0, section);

            final int position = sections.findPosition(upward);

            assertThat(position).isEqualTo(0);
        }

        @Test
        @DisplayName("역이 상행역으로 존재하는 구간이 존재하지 않으면 -1을 반환한다.")
        void findPositionWithNotExistStation() {
            final Sections sections = new Sections(new ArrayList<>());
            final Station upward = new Station(1L, "잠실역");
            final Station downward = new Station(2L, "잠실새내역");
            final Section section = new Section(upward, downward, 10);
            sections.add(0, section);

            final int position = sections.findPosition(new Station(3L, "강남역"));

            assertThat(position).isEqualTo(-1);
        }
    }

    @Nested
    @DisplayName("isEmpty 메서드는 ")
    class IsEmpty {

        @Test
        @DisplayName("구간 정보가 존재하지 않으면 true 반환한다.")
        void isEmptyTrue() {
            final Sections sections = new Sections(new ArrayList<>());

            final boolean result = sections.isEmpty();

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("구간 정보가 존재하면 false 반환한다.")
        void isEmptyFalse() {
            final Sections sections = new Sections(new ArrayList<>());
            final Section section = new Section(new Station(1L, "잠실역"), new Station(2L, "잠실새내역"), 10);
            sections.add(section);

            final boolean result = sections.isEmpty();

            assertThat(result).isFalse();
        }
    }

    @Test
    @DisplayName("모든 구간 목록 정보를 삭제한다.")
    void clear() {
        final Sections sections = new Sections(new ArrayList<>());
        final Section section = new Section(new Station(1L, "잠실역"), new Station(2L, "잠실새내역"), 10);
        sections.add(section);

        sections.clear();

        assertThat(sections.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("상행 역 목록을 조회한다.")
    void getUpwards() {
        final Sections sections = new Sections(new ArrayList<>());
        final Station jamsil = new Station(1L, "잠실역");
        final Station jamsilsaenae = new Station(2L, "잠실새내역");
        final Station geondae = new Station(3L, "건대역");
        final Section firstSection = new Section(jamsil, jamsilsaenae, 10);
        final Section secondSection = new Section(jamsilsaenae, geondae, 10);
        sections.add(firstSection);
        sections.add(secondSection);

        final List<Station> result = sections.getUpwards();

        assertThat(result).isEqualTo(List.of(jamsil, jamsilsaenae));
    }
}
