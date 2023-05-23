package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.section.AlreadyConnectedSectionException;
import subway.exception.section.DisconnectedSectionException;
import subway.exception.section.DuplicateSectionException;
import subway.exception.section.InvalidAddSectionLengthException;
import subway.exception.section.NotFoundSectionException;

class SectionsTest {

    @DisplayName("중복된 Section이 있는 리스트는 Sections를 만들 수 없다")
    @Test
    void createSectionsFailTestByDuplication() {
        Station 잠실 = new Station("잠실");
        Station 잠실새내 = new Station("잠실새내");
        Distance distance = new Distance(10);
        Section section1 = Section.builder()
                .startStation(잠실)
                .endStation(잠실새내)
                .distance(distance)
                .build();
        Section section2 = Section.builder()
                .startStation(잠실)
                .endStation(잠실새내)
                .distance(distance)
                .build();
        List<Section> sections = List.of(section1, section2);

        assertThatThrownBy(() -> new Sections(sections))
                .isInstanceOf(DuplicateSectionException.class)
                .hasMessage("이미 존재하는 구간입니다.");
    }

    @DisplayName("중복된 Section이 없는 리스트는 Sections를 만들 수 있다")
    @Test
    void createSectionsSuccessTest() {
        Station 잠실 = new Station("잠실");
        Station 잠실새내 = new Station("잠실새내");
        Station 잠실나루 = new Station("잠실나루");
        Distance distance = new Distance(10);

        Section section1 = Section.builder()
                .startStation(잠실)
                .endStation(잠실새내)
                .distance(distance)
                .build();
        Section section2 = Section.builder()
                .startStation(잠실새내)
                .endStation(잠실나루)
                .distance(distance)
                .build();
        List<Section> sections = List.of(section1, section2);

        assertDoesNotThrow(() -> new Sections(sections));
    }

    @DisplayName("중복된 Section은 추가할 수 없다.")
    @Test
    void addSectionFailTestByDuplication() {
        Station 잠실 = new Station("잠실");
        Station 잠실새내 = new Station("잠실새내");
        Distance distance = new Distance(10);
        Section section = Section.builder()
                .startStation(잠실)
                .endStation(잠실새내)
                .distance(distance)
                .build();

        Sections sections = new Sections(List.of(section));

        assertThatThrownBy(() -> sections.add(section))
                .isInstanceOf(DuplicateSectionException.class)
                .hasMessage("이미 존재하는 구간입니다.");
    }

    @DisplayName("중복된 Section은 추가할 수 없다.")
    @Test
    void addSectionFailTestByDuplication2() {
        Station 잠실 = new Station("잠실");
        Station 잠실새내 = new Station("잠실새내");
        Station 잠실나루 = new Station("잠실나루");
        Distance distance = new Distance(10);
        Section section1 = Section.builder()
                .startStation(잠실)
                .endStation(잠실새내)
                .distance(distance)
                .build();
        Section section2 = Section.builder()
                .startStation(잠실새내)
                .endStation(잠실나루)
                .distance(distance)
                .build();

        Sections sections = new Sections(List.of(section1, section2));
        Section sectionForAdd = Section.builder()
                .startStation(잠실)
                .endStation(잠실나루)
                .distance(distance)
                .build();

        assertThatThrownBy(() -> sections.add(sectionForAdd))
                .isInstanceOf(AlreadyConnectedSectionException.class)
                .hasMessage("이미 연결되어 있는 구간입니다.");
    }

    @DisplayName("연결되지 않는 Section은 추가할 수 없다.")
    @Test
    void addSectionFailTestByNotConnection() {
        Station 잠실 = new Station("잠실");
        Station 잠실새내 = new Station("잠실새내");
        Station 잠실나루 = new Station("잠실나루");
        Station 건대 = new Station("건대");
        Distance distance = new Distance(10);
        Section section = Section.builder()
                .startStation(잠실)
                .endStation(잠실새내)
                .distance(distance)
                .build();

        Sections sections = new Sections(List.of(section));
        Section sectionForAdd = Section.builder()
                .startStation(잠실나루)
                .endStation(건대)
                .distance(distance)
                .build();

        assertThatThrownBy(() -> sections.add(sectionForAdd))
                .isInstanceOf(DisconnectedSectionException.class)
                .hasMessage("연결되어 있지 않은 구간입니다.");
    }

    @DisplayName("상행 종점에 Section을 연결할 수 있다.")
    @Test
    void addSectionSuccessTestToUpStation() {
        Station 잠실 = new Station("잠실");
        Station 잠실새내 = new Station("잠실새내");
        Station 잠실나루 = new Station("잠실나루");
        Distance distance = new Distance(10);

        Section section = Section.builder()
                .startStation(잠실)
                .endStation(잠실새내)
                .distance(distance)
                .build();

        Sections sections = new Sections(List.of(section));
        Section sectionForAdd = Section.builder()
                .startStation(잠실나루)
                .endStation(잠실)
                .distance(distance)
                .build();

        assertDoesNotThrow(() -> sections.add(sectionForAdd));
    }

    @DisplayName("하행 종점에 Section을 연결할 수 있다.")
    @Test
    void addSectionSuccessTestToDownStation() {
        Station 잠실 = new Station("잠실");
        Station 잠실새내 = new Station("잠실새내");
        Station 잠실나루 = new Station("잠실나루");
        Distance distance = new Distance(10);

        Section section = Section.builder()
                .startStation(잠실)
                .endStation(잠실새내)
                .distance(distance)
                .build();

        Sections sections = new Sections(List.of(section));
        Section sectionForAdd = Section.builder()
                .startStation(잠실새내)
                .endStation(잠실나루)
                .distance(distance)
                .build();

        assertDoesNotThrow(() -> sections.add(sectionForAdd));
    }

    @DisplayName("구간 사이에 Section을 연결할 수 있다.")
    @Test
    void addSectionSuccessTest1() {
        Station 잠실 = new Station("잠실");
        Station 잠실새내 = new Station("잠실새내");
        Station 잠실나루 = new Station("잠실나루");
        Distance distance1 = new Distance(10);
        Distance distance2 = new Distance(3);

        Section section = Section.builder()
                .startStation(잠실)
                .endStation(잠실새내)
                .distance(distance1)
                .build();

        Sections sections = new Sections(List.of(section));
        Section sectionForAdd = Section.builder()
                .startStation(잠실)
                .endStation(잠실나루)
                .distance(distance2)
                .build();

        assertDoesNotThrow(() -> sections.add(sectionForAdd));
    }

    @DisplayName("구간 사이에 Section을 연결할 수 있다.")
    @Test
    void addSectionSuccessTest2() {
        Station 잠실 = new Station("잠실");
        Station 잠실새내 = new Station("잠실새내");
        Station 잠실나루 = new Station("잠실나루");
        Distance distance1 = new Distance(10);
        Distance distance2 = new Distance(5);

        Section section = Section.builder()
                .startStation(잠실)
                .endStation(잠실새내)
                .distance(distance1)
                .build();

        Sections sections = new Sections(List.of(section));
        Section sectionForAdd = Section.builder()
                .startStation(잠실나루)
                .endStation(잠실새내)
                .distance(distance2)
                .build();

        assertDoesNotThrow(() -> sections.add(sectionForAdd));
    }

    @DisplayName("구간 사이에 Section 거리가 해당 구간보다 거리가 클 경우 연결할 수 없다.")
    @Test
    void addSectionFailTestByDistance1() {
        Station 잠실 = new Station("잠실");
        Station 잠실새내 = new Station("잠실새내");
        Station 잠실나루 = new Station("잠실나루");
        Distance distance1 = new Distance(5);
        Distance distance2 = new Distance(6);

        Section section = Section.builder()
                .startStation(잠실)
                .endStation(잠실새내)
                .distance(distance1)
                .build();

        Sections sections = new Sections(List.of(section));

        Section sectionForAdd = Section.builder()
                .startStation(잠실)
                .endStation(잠실나루)
                .distance(distance2)
                .build();

        assertThatThrownBy(() -> sections.add(sectionForAdd))
                .isInstanceOf(InvalidAddSectionLengthException.class)
                .hasMessage("구간 길이로 인해 연결할 수 없습니다.");
    }

    @DisplayName("구간 사이에 Section 거리가 해당 구간보다 거리가 클 경우 연결할 수 없다.")
    @Test
    void addSectionFailTestByDistance2() {
        Station 잠실 = new Station("잠실");
        Station 잠실새내 = new Station("잠실새내");
        Station 잠실나루 = new Station("잠실나루");
        Distance distance1 = new Distance(5);
        Distance distance2 = new Distance(6);

        Section section = Section.builder()
                .startStation(잠실)
                .endStation(잠실새내)
                .distance(distance1)
                .build();

        Sections sections = new Sections(List.of(section));
        Section sectionForAdd = Section.builder(section)
                .startStation(잠실나루)
                .distance(distance2)
                .build();

        assertThatThrownBy(() -> sections.add(sectionForAdd))
                .isInstanceOf(InvalidAddSectionLengthException.class)
                .hasMessage("구간 길이로 인해 연결할 수 없습니다.");
    }

    @DisplayName("존재하지 않는 역을 삭제할 수 없다.")
    @Test
    void removeStationFailTestByNotExistStation() {
        Station 잠실 = new Station("잠실");
        Station 잠실새내 = new Station("잠실새내");
        Station 잠실나루 = new Station("잠실나루");
        Distance distance = new Distance(5);

        Section section = Section.builder()
                .startStation(잠실)
                .endStation(잠실새내)
                .distance(distance)
                .build();

        Sections sections = new Sections(List.of(section));

        assertThatThrownBy(() -> sections.remove(잠실나루))
                .isInstanceOf(NotFoundSectionException.class)
                .hasMessage("존재하지 않는 구간입니다.");
    }

    @DisplayName("역이 단 두 개일때 삭제시, 모든 구간이 삭제된다.")
    @Test
    void removeStationSuccessTestByOnlyTwoStation() {
        Station 잠실 = new Station("잠실");
        Station 잠실새내 = new Station("잠실새내");
        Distance distance = new Distance(5);

        Section section = Section.builder()
                .startStation(잠실)
                .endStation(잠실새내)
                .distance(distance)
                .build();

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
        Distance distance = new Distance(5);

        Section section1 = Section.builder()
                .startStation(잠실)
                .endStation(잠실새내)
                .distance(distance)
                .build();
        Section section2 = Section.builder()
                .startStation(잠실새내)
                .endStation(잠실나루)
                .distance(distance)
                .build();

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
        Distance distance = new Distance(5);

        Section section1 = Section.builder()
                .startStation(잠실)
                .endStation(잠실새내)
                .distance(distance)
                .build();
        Section section2 = Section.builder()
                .startStation(잠실새내)
                .endStation(잠실나루)
                .distance(distance)
                .build();

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
        Distance distance = new Distance(5);
        Section section1 = Section.builder()
                .startStation(잠실)
                .endStation(잠실새내)
                .distance(distance)
                .build();
        Section section2 = Section.builder()
                .startStation(잠실새내)
                .endStation(잠실나루)
                .distance(distance)
                .build();

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
        Distance distance = new Distance(10);
        Section section1 = Section.builder()
                .startStation(잠실)
                .endStation(잠실새내)
                .distance(distance)
                .build();
        Section section2 = Section.builder()
                .startStation(잠실새내)
                .endStation(잠실나루)
                .distance(distance)
                .build();

        Sections sections = new Sections(List.of(section2, section1));

        assertDoesNotThrow(() -> sections.remove(잠실새내));
        assertThat(sections.getSections()).hasSize(1);
        assertThat(sections.getUpStation()).isEqualTo(잠실);
        assertThat(sections.getDownStation()).isEqualTo(잠실나루);
    }
}