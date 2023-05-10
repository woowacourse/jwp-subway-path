package subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.GlobalException;

class SectionsTest {

    @DisplayName("중복된 Section이 있는 리스트는 Sections를 만들 수 없다")
    @Test
    void createSectionsFailTestByDuplication() {
        Station 잠실 = new Station("잠실");
        Station 잠실새내 = new Station("잠실새내");
        Section section1 = new Section(잠실, 잠실새내, new Distance(10));
        Section section2 = new Section(잠실, 잠실새내, new Distance(10));
        List<Section> sections = List.of(section1, section2);

        assertThatThrownBy(() -> new Sections(sections, 잠실, 잠실새내))
                .isInstanceOf(GlobalException.class);
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

        assertDoesNotThrow(() -> new Sections(sections, 잠실, 잠실나루));
    }

    @DisplayName("중복된 Section은 추가할 수 없다.")
    @Test
    void addSectionFailTestByDuplication() {
        Station 잠실 = new Station("잠실");
        Station 잠실새내 = new Station("잠실새내");
        Section section = new Section(잠실, 잠실새내, new Distance(10));

        Sections sections = new Sections(List.of(section), 잠실, 잠실새내);

        assertThatThrownBy(() -> sections.add(section))
                .isInstanceOf(GlobalException.class);
    }

    @DisplayName("중복된 Section은 추가할 수 없다.")
    @Test
    void addSectionFailTestByDuplication2() {
        Station 잠실 = new Station("잠실");
        Station 잠실새내 = new Station("잠실새내");
        Station 잠실나루 = new Station("잠실나루");
        Section section1 = new Section(잠실, 잠실새내, new Distance(10));
        Section section2 = new Section(잠실새내, 잠실나루, new Distance(10));

        Sections sections = new Sections(List.of(section1, section2), 잠실, 잠실나루);
        Section sectionForAdd = new Section(잠실, 잠실나루, new Distance(10));

        assertThatThrownBy(() -> sections.add(sectionForAdd))
                .isInstanceOf(GlobalException.class);
    }

    @DisplayName("연결되지 않는 Section은 추가할 수 없다.")
    @Test
    void addSectionFailTestByNotConnection() {
        Station 잠실 = new Station("잠실");
        Station 잠실새내 = new Station("잠실새내");
        Station 잠실나루 = new Station("잠실나루");
        Station 건대 = new Station("건대");
        Section section = new Section(잠실, 잠실새내, new Distance(10));

        Sections sections = new Sections(List.of(section), 잠실, 잠실나루);
        Section sectionForAdd = new Section(잠실나루, 건대, new Distance(10));

        assertThatThrownBy(() -> sections.add(sectionForAdd))
                .isInstanceOf(GlobalException.class);
    }

    @DisplayName("상행 종점에 Section을 연결할 수 있다.")
    @Test
    void addSectionSuccessTestToUpStation() {
        Station 잠실 = new Station("잠실");
        Station 잠실새내 = new Station("잠실새내");
        Station 잠실나루 = new Station("잠실나루");
        Section section = new Section(잠실, 잠실새내, new Distance(10));

        Sections sections = new Sections(List.of(section), 잠실, 잠실새내);
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

        Sections sections = new Sections(List.of(section), 잠실, 잠실새내);
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

        Sections sections = new Sections(List.of(section), 잠실, 잠실새내);
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

        Sections sections = new Sections(List.of(section), 잠실, 잠실새내);
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

        Sections sections = new Sections(List.of(section), 잠실, 잠실새내);
        Section sectionForAdd = new Section(잠실, 잠실나루, new Distance(6));

        assertThatThrownBy(() -> sections.add(sectionForAdd))
                .isInstanceOf(GlobalException.class);
    }

    @DisplayName("구간 사이에 Section 거리가 해당 구간보다 거리가 클 경우 연결할 수 없다.")
    @Test
    void addSectionFailTestByDistance2() {
        Station 잠실 = new Station("잠실");
        Station 잠실새내 = new Station("잠실새내");
        Station 잠실나루 = new Station("잠실나루");
        Section section = new Section(잠실, 잠실새내, new Distance(5));

        Sections sections = new Sections(List.of(section), 잠실, 잠실새내);
        Section sectionForAdd = new Section(잠실나루, 잠실새내, new Distance(6));

        assertThatThrownBy(() -> sections.add(sectionForAdd))
                .isInstanceOf(GlobalException.class);
    }

}