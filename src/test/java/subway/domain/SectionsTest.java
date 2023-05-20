package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.DuplicateException;

class SectionsTest {
    private List<Section> sectionList;
    private Section section1;
    private Section section2;
    private Station station1;
    private Station station2;
    private Station station3;
    private Sections sections;

    @BeforeEach
    void setUp() {
        station1 = new Station(1L, "잠실역");
        station2 = new Station(2L, "잠실새내역");
        station3 = new Station(3L, "봉천역");

        section1 = new Section(station1, station2, 10);
        section2 = new Section(station2, station3, 4);

        sectionList = List.of(section2, section1);

        sections = Sections.from(sectionList);
    }

    @Test
    @DisplayName("생성 테스트")
    void createFrom() {
        // given
        sections = Sections.from(sectionList);

        // expected
        assertAll(
                () -> assertThat(sections.getSections().get(0)).isEqualTo(section1),
                () -> assertThat(sections.getSections().get(1)).isEqualTo(section2)
        );
    }

    @Test
    @DisplayName("노선의 모든 역을 반환한다.")
    void getStations() {
        // when
        List<Station> stations = sections.getStations();

        // expected
        assertThat(stations).contains(station1, station2, station3);
    }

    @Test
    @DisplayName("새로운 구간을 추가한다.")
    void addSection() {
        // given
        Station upStation = new Station(4L, "종합운동장역");
        Section newSection = new Section(upStation, station3, 2);

        // when
        sections.addSection(newSection);

        // expected
        assertAll(
                () -> assertThat(sections.getStations()).hasSize(4),
                () -> assertThat(sections.getSections().get(1)).isEqualTo(newSection)
        );
    }

    @Test
    @DisplayName("새로운 역들이 노선에 이미 존재하는 경우 예외가 발생한다.")
    void validateDuplicateSection() {
        // given
        Section newSection = new Section(station1, station3, 2);

        // expected
        assertThatThrownBy(() -> sections.addSection(newSection)).isInstanceOf(DuplicateException.class);
    }

    @Test
    @DisplayName("새로운 역이 상행 종점으로 추가된다.")
    void addSectionFirstStation() {
        // given
        Station newUpStation = new Station(4L, "베로역");
        Section newSection = new Section(newUpStation, station1, 10);

        // when
        sections.addSection(newSection);

        // expected
        assertThat(sections.getSections().get(0)).isEqualTo(newSection);
    }

    @Test
    @DisplayName("새로운 역이 하행 종점으로 추가된다.")
    void addSectionLastStation() {
        // given
        Station newDownStation = new Station(4L, "베로역");
        Section newSection = new Section(station3, newDownStation, 10);

        // when
        sections.addSection(newSection);

        // expected
        assertThat(sections.getSections().get(2)).isEqualTo(newSection);
    }

    @Test
    @DisplayName("중간 역을 삭제하면 연결된 구간이 삭제된다.")
    void delete() {
        // when
        sections.deleteSection(station2);

        // expected
        assertAll(
                () -> assertThat(sections.getStations()).hasSize(2),
                () -> assertThat(sections.getSections()).hasSize(1)
        );
    }

    @Test
    @DisplayName("종점을 삭제한다.")
    void deleteTerminal() {
        // when
        sections.deleteSection(station1);

        // expected
        assertAll(
                () -> assertThat(sections.getStations()).hasSize(2),
                () -> assertThat(sections.getSections()).hasSize(1)
        );
    }

    @Test
    @DisplayName("노선에 역이 두 개 존재할 때, 모든 노선이 삭제된다.")
    void deleteAll() {
        // given
        sections = Sections.from(List.of(section1));

        // when
        sections.deleteSection(station1);

        // expected
        assertAll(
                () -> assertThat(sections.getStations()).hasSize(0),
                () -> assertThat(sections.getSections()).hasSize(0)
        );
    }
}
