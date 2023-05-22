package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.exception.*;

class SectionsTest {

    @Nested
    @DisplayName("역을 추가한다.")
    class addStationBySection {
        @DisplayName("중복된 Section이 있는 리스트는 Sections를 만들 수 없다")
        @Test
        void createSectionsFailTestByDuplication() {
            Station 잠실 = new Station("잠실");
            Station 잠실새내 = new Station("잠실새내");
            Section section1 = new Section(잠실, 잠실새내, new Distance(10));
            Section section2 = new Section(잠실, 잠실새내, new Distance(10));
            List<Section> sections = List.of(section1, section2);

            assertThatThrownBy(() -> new Sections(sections))
                    .isInstanceOf(DuplicateSectionException.class);
        }

        @DisplayName("중복된 Section이 없는 리스트는 Sections를 만들 수 있다")
        @Test
        void createSectionsSuccessTest() {
            Station 잠실 = new Station("잠실");
            Station 잠실새내 = new Station("잠실새내");
            Station 잠실나루 = new Station("잠실나루");
            Section section1 = new Section(잠실, 잠실새내, new Distance(10));
            Section section2 = new Section(잠실새내, 잠실나루, new Distance(10));
            List<Section> sections = List.of(section1, section2);

            assertDoesNotThrow(() -> new Sections(sections));
        }

        @DisplayName("중복된 Section은 추가할 수 없다.")
        @Test
        void addSectionFailTestByDuplication() {
            Station 잠실 = new Station("잠실");
            Station 잠실새내 = new Station("잠실새내");
            Section section = new Section(잠실, 잠실새내, new Distance(10));

            Sections sections = new Sections(List.of(section));

            assertThatThrownBy(() -> sections.add(section))
                    .isInstanceOf(DuplicateSectionAddException.class);
        }

        @DisplayName("중복된 Section은 추가할 수 없다.")
        @Test
        void addSectionFailTestByDuplication2() {
            Station 잠실 = new Station("잠실");
            Station 잠실새내 = new Station("잠실새내");
            Station 잠실나루 = new Station("잠실나루");
            Section section1 = new Section(잠실, 잠실새내, new Distance(10));
            Section section2 = new Section(잠실새내, 잠실나루, new Distance(10));

            Sections sections = new Sections(List.of(section1, section2));
            Section sectionForAdd = new Section(잠실, 잠실나루, new Distance(10));

            assertThatThrownBy(() -> sections.add(sectionForAdd))
                    .isInstanceOf(NotConnectSectionException.class);
        }

        @DisplayName("연결되지 않는 Section은 추가할 수 없다.")
        @Test
        void addSectionFailTestByNotConnection() {
            Station 잠실 = new Station("잠실");
            Station 잠실새내 = new Station("잠실새내");
            Station 잠실나루 = new Station("잠실나루");
            Station 건대 = new Station("건대");
            Section section = new Section(잠실, 잠실새내, new Distance(10));

            Sections sections = new Sections(List.of(section));
            Section sectionForAdd = new Section(잠실나루, 건대, new Distance(10));

            assertThatThrownBy(() -> sections.add(sectionForAdd))
                    .isInstanceOf(NotConnectSectionException.class);
        }

        @DisplayName("상행 종점에 Section을 연결할 수 있다.")
        @Test
        void addSectionSuccessTestToUpStation() {
            Station 잠실 = new Station("잠실");
            Station 잠실새내 = new Station("잠실새내");
            Station 잠실나루 = new Station("잠실나루");
            Section section = new Section(잠실, 잠실새내, new Distance(10));

            Sections sections = new Sections(List.of(section));
            Section sectionForAdd = new Section(잠실나루, 잠실, new Distance(5));

            assertDoesNotThrow(() -> sections.add(sectionForAdd));
        }

        @DisplayName("하행 종점에 Section을 연결할 수 있다.")
        @Test
        void addSectionSuccessTestToDownStation() {
            Station 잠실 = new Station("잠실");
            Station 잠실새내 = new Station("잠실새내");
            Station 잠실나루 = new Station("잠실나루");
            Section section = new Section(잠실, 잠실새내, new Distance(10));

            Sections sections = new Sections(List.of(section));
            Section sectionForAdd = new Section(잠실새내, 잠실나루, new Distance(5));

            assertDoesNotThrow(() -> sections.add(sectionForAdd));
        }

        @DisplayName("구간 사이에 Section을 연결할 수 있다.")
        @Test
        void addSectionSuccessTest1() {
            Station 잠실 = new Station("잠실");
            Station 잠실새내 = new Station("잠실새내");
            Station 잠실나루 = new Station("잠실나루");
            Section section = new Section(잠실, 잠실새내, new Distance(10));

            Sections sections = new Sections(List.of(section));
            Section sectionForAdd = new Section(잠실, 잠실나루, new Distance(5));

            assertDoesNotThrow(() -> sections.add(sectionForAdd));
        }

        @DisplayName("구간 사이에 Section을 연결할 수 있다.")
        @Test
        void addSectionSuccessTest2() {
            Station 잠실 = new Station("잠실");
            Station 잠실새내 = new Station("잠실새내");
            Station 잠실나루 = new Station("잠실나루");
            Section section = new Section(잠실, 잠실새내, new Distance(10));

            Sections sections = new Sections(List.of(section));
            Section sectionForAdd = new Section(잠실나루, 잠실새내, new Distance(5));

            assertDoesNotThrow(() -> sections.add(sectionForAdd));
        }

        @DisplayName("구간 사이에 Section 거리가 해당 구간보다 거리가 클 경우 연결할 수 없다.")
        @Test
        void addSectionFailTestByDistance1() {
            Station 잠실 = new Station("잠실");
            Station 잠실새내 = new Station("잠실새내");
            Station 잠실나루 = new Station("잠실나루");
            Section section = new Section(잠실, 잠실새내, new Distance(5));

            Sections sections = new Sections(List.of(section));
            Section sectionForAdd = new Section(잠실, 잠실나루, new Distance(6));

            assertThatThrownBy(() -> sections.add(sectionForAdd))
                    .isInstanceOf(NotConnectSectionException.class);
        }

        @DisplayName("구간 사이에 Section 거리가 해당 구간보다 거리가 클 경우 연결할 수 없다.")
        @Test
        void addSectionFailTestByDistance2() {
            Station 잠실 = new Station("잠실");
            Station 잠실새내 = new Station("잠실새내");
            Station 잠실나루 = new Station("잠실나루");
            Section section = new Section(잠실, 잠실새내, new Distance(5));

            Sections sections = new Sections(List.of(section));
            Section sectionForAdd = new Section(잠실나루, 잠실새내, new Distance(6));

            assertThatThrownBy(() -> sections.add(sectionForAdd))
                    .isInstanceOf(NotConnectSectionException.class);
        }
    }

    @Nested
    @DisplayName("역을 삭제한다.")
    class deleteStationBySection {
        @DisplayName("존재하지 않는 역을 삭제할 수 없다.")
        @Test
        void removeStationFailTestByNotExistStation() {
            Station 잠실 = new Station("잠실");
            Station 잠실새내 = new Station("잠실새내");
            Station 잠실나루 = new Station("잠실나루");
            Section section = new Section(잠실, 잠실새내, new Distance(5));

            Sections sections = new Sections(List.of(section));

            assertThatThrownBy(() -> sections.remove(잠실나루))
                    .isInstanceOf(InvalidSectionException.class);
        }

        @DisplayName("역이 단 두 개일때 삭제시, 모든 구간이 삭제된다.")
        @Test
        void removeStationSuccessTestByOnlyTwoStation() {
            Station 잠실 = new Station("잠실");
            Station 잠실새내 = new Station("잠실새내");
            Section section = new Section(잠실, 잠실새내, new Distance(5));

            Sections sections = new Sections(List.of(section));

            assertDoesNotThrow(() -> sections.remove(잠실));
            assertThat(sections.getSections()).hasSize(0);
        }

        @DisplayName("상행 종점역을 삭제할 수 있다.")
        @Test
        void removeSuccessTestByUpStation() {
            Station 잠실 = new Station("잠실");
            Station 잠실새내 = new Station("잠실새내");
            Station 잠실나루 = new Station("잠실나루");
            Section section1 = new Section(잠실, 잠실새내, new Distance(5));
            Section section2 = new Section(잠실새내, 잠실나루, new Distance(5));

            Sections sections = new Sections(List.of(section1, section2));

            assertDoesNotThrow(() -> sections.remove(잠실));
            assertThat(sections.getSections()).hasSize(1);
            assertThat(sections.getUpStation()).isEqualTo(잠실새내);
        }

        @DisplayName("하행 종점역을 삭제할 수 있다.")
        @Test
        void removeSuccessTestByDownStation() {
            Station 잠실 = new Station("잠실");
            Station 잠실새내 = new Station("잠실새내");
            Station 잠실나루 = new Station("잠실나루");
            Section section1 = new Section(잠실, 잠실새내, new Distance(5));
            Section section2 = new Section(잠실새내, 잠실나루, new Distance(5));

            Sections sections = new Sections(List.of(section1, section2));

            assertDoesNotThrow(() -> sections.remove(잠실나루));
            assertThat(sections.getSections()).hasSize(1);
            assertThat(sections.getDownStation()).isEqualTo(잠실새내);
        }

        @DisplayName("종점이 아닌 역을 삭제할 수 있다.")
        @Test
        void removeSuccessTestByNotTerminal1() {
            Station 잠실 = new Station("잠실");
            Station 잠실새내 = new Station("잠실새내");
            Station 잠실나루 = new Station("잠실나루");
            Section section1 = new Section(잠실, 잠실새내, new Distance(5));
            Section section2 = new Section(잠실새내, 잠실나루, new Distance(5));

            Sections sections = new Sections(List.of(section1, section2));

            assertDoesNotThrow(() -> sections.remove(잠실새내));
            assertThat(sections.getSections()).hasSize(1);
            assertThat(sections.getUpStation()).isEqualTo(잠실);
            assertThat(sections.getDownStation()).isEqualTo(잠실나루);
        }

        @DisplayName("종점이 아닌 역을 삭제할 수 있다.")
        @Test
        void removeSuccessTestByNotTerminal2() {
            Station 잠실 = new Station("잠실");
            Station 잠실새내 = new Station("잠실새내");
            Station 잠실나루 = new Station("잠실나루");
            Section section1 = new Section(잠실, 잠실새내, new Distance(5));
            Section section2 = new Section(잠실새내, 잠실나루, new Distance(5));

            Sections sections = new Sections(List.of(section2, section1));

            assertDoesNotThrow(() -> sections.remove(잠실새내));
            assertThat(sections.getSections()).hasSize(1);
            assertThat(sections.getUpStation()).isEqualTo(잠실);
            assertThat(sections.getDownStation()).isEqualTo(잠실나루);
        }
    }
}
