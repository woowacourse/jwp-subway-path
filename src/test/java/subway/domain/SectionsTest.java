package subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class SectionsTest {

    @Test
    @DisplayName("Sections를 생성할 수 있다.")
    void create() {
        // given
        final String upStationName = "용산역";
        final String downStationName = "죽전역";
        final int distance = 10;
        final Section section = new Section(new Station(upStationName), new Station(downStationName), distance);
        // when
        final Sections sections = new Sections(List.of(section));
        // then
        assertEquals(1, sections.getSections().size());
        assertEquals(upStationName, sections.getSections().get(0).getUpStation().getName());
        assertEquals(downStationName, sections.getSections().get(0).getDownStation().getName());
        assertEquals(distance, sections.getSections().get(0).getDistance());
    }

    @Nested
    @DisplayName("section 추가 테스트")
    class AddSection {

        @Test
        @DisplayName("등록된 section이 없는 경우, 입력받은 section을 그대로 추가할 수 있다.")
        void addSectionWhenInitial() {
            // given
            final String upStationName = "용산역";
            final String downStationName = "죽전역";
            final int distance = 10;
            final Section section = new Section(new Station(upStationName), new Station(downStationName), distance);
            final Sections sections = new Sections(new ArrayList<>());
            // when
            sections.addSection(section);
            // then
            assertEquals(1, sections.getSections().size());
            assertEquals(upStationName, sections.getSections().get(0).getUpStation().getName());
            assertEquals(downStationName, sections.getSections().get(0).getDownStation().getName());
            assertEquals(distance, sections.getSections().get(0).getDistance());
        }

        @Test
        @DisplayName("중간에 section을 추가하는 경우, 기존 section을 삭제하고 입력받은 section을 추가 후 연결할 수 있다.")
        void addSectionWhenMiddle() {
            // given
            final Section section = new Section(new Station(1L, "용산역"), new Station(2L, "감삼역"), 15);
            final Sections sections = new Sections(new ArrayList<>());
            sections.addSection(section);
            final Section newSection = new Section(new Station(1L, "용산역"), new Station(3L, "죽전역"), 10);
            // when
            sections.addSection(newSection);
            // then
            assertEquals(2, sections.getSections().size());
            final Section upSection = sections.getSections().stream()
                    .filter(it -> it.getUpStation().getName().equals("용산역"))
                    .findAny().orElseThrow(IllegalStateException::new);
            assertEquals("죽전역", upSection.getDownStation().getName());
            assertEquals(10, upSection.getDistance());
            final Section downSection = sections.getSections().stream()
                    .filter(it -> it.getUpStation().getName().equals("죽전역"))
                    .findAny().orElseThrow(IllegalStateException::new);
            assertEquals("감삼역", downSection.getDownStation().getName());
            assertEquals(5, downSection.getDistance());
        }

        @Test
        @DisplayName("종점에 section을 추가하는 경우, 입력받은 section을 그대로 추가할 수 있다.")
        void addSectionWhenTerminal() {
            // given
            final Section section = new Section(new Station(1L, "청라언덕역"), new Station(2L, "반월당역"), 10);
            final Sections sections = new Sections(new ArrayList<>());
            sections.addSection(section);
            final Section newSection = new Section(new Station(2L, "반월당역"), new Station(3L, "경대병원역"), 20);
            // when
            sections.addSection(newSection);
            // then
            assertEquals(2, sections.getSections().size());
            final Section upSection = sections.getSections().stream()
                    .filter(it -> it.getUpStation().getName().equals("청라언덕역"))
                    .findAny().orElseThrow(IllegalStateException::new);
            assertEquals("반월당역", upSection.getDownStation().getName());
            assertEquals(10, upSection.getDistance());
            final Section downSection = sections.getSections().stream()
                    .filter(it -> it.getUpStation().getName().equals("반월당역"))
                    .findAny().orElseThrow(IllegalStateException::new);
            assertEquals("경대병원역", downSection.getDownStation().getName());
            assertEquals(20, downSection.getDistance());
        }

        @Test
        @DisplayName("이미 등록된 section을 추가하는 경우, 예외가 발생한다.")
        void exceptionWhenAlreadyExist() {
            // given
            final Section section = new Section(new Station(1L, "청라언덕역"), new Station(2L, "반월당역"), 10);
            final Sections sections = new Sections(new ArrayList<>());
            sections.addSection(section);
            // when & then
            assertThatThrownBy(() -> sections.addSection(section))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("section 삭제 테스트")
    class RemoveSection {

        @Test
        @DisplayName("해당 station으로 등록된 section이 없는 경우, 삭제할 수 없다.")
        void exceptionWhenEmpty() {
            // given
            final Sections sections = new Sections(new ArrayList<>());
            // when & then
            assertThatThrownBy(() -> sections.removeSectionByStation(new Station(1L, "용산역")))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("삭제할 station이 종점인 경우, 해당 station으로 등록된 section을 삭제할 수 있다.")
        void removeSectionWhenTerminal() {
            // given
            final Station upStation = new Station(1L, "청라언덕역");
            final Station downStation = new Station(2L, "반월당역");
            final Section section = new Section(upStation, downStation, 10);
            final Sections sections = new Sections(new ArrayList<>());
            sections.addSection(section);
            // when
            sections.removeSectionByStation(downStation);
            // then
            assertEquals(0, sections.getSections().size());
        }

        @Test
        @DisplayName("삭제할 station이 중간인 경우, 해당 station으로 등록된 section들을 삭제하고 연결할 수 있다.")
        void removeSectionWhenMiddle() {
            // given
            final Sections sections = new Sections(new ArrayList<>());
            final Station upStation = new Station(1L, "용산역");
            final Station midStation = new Station(2L, "감삼역");
            final Section section = new Section(upStation, midStation, 15);
            sections.addSection(section);
            final Station downStation = new Station(3L, "죽전역");
            final Section newSection = new Section(midStation, downStation, 10);
            sections.addSection(newSection);
            // when
            sections.removeSectionByStation(midStation);
            // then
            assertEquals(1, sections.getSections().size());
            final Section upSection = sections.getSections().stream()
                    .filter(it -> it.getUpStation().getName().equals("용산역"))
                    .findAny().orElseThrow(IllegalStateException::new);
            assertEquals("죽전역", upSection.getDownStation().getName());
            assertEquals(25, upSection.getDistance());
        }
    }

    @Nested
    @DisplayName("station 정렬 테스트")
    class SortStations {

        @Test
        @DisplayName("sections가 비어있는 경우, 빈 리스트를 반환한다.")
        void sortWhenEmptySections() {
            // given
            final Sections sections = new Sections(new ArrayList<>());
            // when
            final List<Station> stations = sections.sortStations();
            // then
            assertEquals(0, stations.size());
        }

        @Test
        @DisplayName("sections에 등록된 station들을 정렬할 수 있다.")
        void sort() {
            // given
            final Sections sections = new Sections(new ArrayList<>());
            final Station upStation = new Station(1L, "용산역");
            final Station downStation = new Station(2L, "감삼역");
            final Section section = new Section(upStation, downStation, 15);
            sections.addSection(section);
            final Station midStation = new Station(3L, "죽전역");
            final Section newSection = new Section(midStation, downStation, 10);
            sections.addSection(newSection);
            // when
            final List<Station> stations = sections.sortStations();
            // then
            assertEquals(3, stations.size());
            assertEquals("용산역", stations.get(0).getName());
            assertEquals("죽전역", stations.get(1).getName());
            assertEquals("감삼역", stations.get(2).getName());
        }
    }
}
