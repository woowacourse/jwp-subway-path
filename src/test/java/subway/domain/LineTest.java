package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {

    private Line line;
    private Station station1;
    private Station station2;
    private Station station3;

    @BeforeEach
    void setUp() {
        station1 = new Station(1L, "잠실역");
        station2 = new Station(2L, "강남역");
        station3 = new Station(3L, "선릉역");

        Section section1 = new Section(station1, station2, 5);
        Section section2 = new Section(station2, station3, 7);

        line = new Line(1L, "1호선", new LinkedList<>(List.of(section1, section2)));
    }

    @DisplayName("상행 종점에 해당하는 역이면 true를 반환한다.")
    @Test
    void isLastStationAtLeftTrue() {
        // given
        Station station = new Station(1L, "잠실역");

        // when, then
        assertThat(line.isLastStationAtLeft(station)).isTrue();
    }

    @DisplayName("상행 종점에 해당하지 않는 역이면 false를 반환한다.")
    @Test
    void isLastStationAtLeftFalse() {
        // given
        Station station = new Station(2L, "강남역");

        // when, then
        assertThat(line.isLastStationAtLeft(station)).isFalse();
    }

    @DisplayName("하행 종점에 해당하는 역이면 true를 반환한다.")
    @Test
    void isLastStationAtRightTrue() {
        // given
        Station station = new Station(3L, "선릉역");

        // when, then
        assertThat(line.isLastStationAtRight(station)).isTrue();
    }

    @DisplayName("하행 종점에 해당하지 않는 역이면 false를 반환한다.")
    @Test
    void isLastStationAtRightFalse() {
        // given
        Station station = new Station(2L, "강남역");

        // when, then
        assertThat(line.isLastStationAtRight(station)).isFalse();
    }

    @DisplayName("노선의 전체 역을 조회한다.")
    @Test
    void findLeftToRightRoute() {
        // when
        List<Station> stations = line.findLeftToRightRoute();

        // then
        assertThat(stations).containsExactly(station1, station2, station3);
    }

    @DisplayName("노선에 구간을 추가한다. - 상행 종점을 추가")
    @Test
    void addSectionAtLeftEnd() {
        // given
        Station newStation = new Station("강변역");
        Section sectionToAdd = new Section(newStation, station1, new Distance(3));

        // when
        line.addSection(sectionToAdd);
        LinkedList<Section> sections = line.getSections().getSections();

        // then
        assertSoftly(softly -> {
            softly.assertThat(sections).hasSize(3);
            softly.assertThat(sections.getFirst().getLeft().getName()).isEqualTo("강변역");
        });
    }

    @DisplayName("노선에 구간을 추가한다. - 하행 종점을 추가")
    @Test
    void addSectionAtRightEnd() {
        // given
        Station newStation = new Station("서초역");
        Section sectionToAdd = new Section(station3, newStation, new Distance(3));

        // when
        line.addSection(sectionToAdd);
        LinkedList<Section> sections = line.getSections().getSections();

        // then
        assertSoftly(softly -> {
            softly.assertThat(sections).hasSize(3);
            softly.assertThat(sections.getLast().getRight().getName()).isEqualTo("서초역");
        });
    }

    @DisplayName("노선에 구간을 추가한다. - 역과 역 사이 기준역 좌측에 추가한다.")
    @Test
    void addSectionLeftBetween() {
        // given
        Station newStation = new Station("삼성역");
        Section section = new Section(newStation, station2, new Distance(4));

        // when
        line.addSection(section);
        LinkedList<Section> sections = line.getSections().getSections();

        // then
        assertSoftly(softly -> {
            softly.assertThat(sections).hasSize(3);
            softly.assertThat(sections.get(1).getLeft().getName()).isEqualTo("삼성역");
        });
    }

    @DisplayName("노선에 구간을 추가한다. - 역과 역 사이 기준역 우측에 추가한다.")
    @Test
    void addSectionRightBetween() {
        // given
        Station newStation = new Station("삼성역");
        Section section = new Section(station2, newStation, new Distance(4));

        // when
        line.addSection(section);
        LinkedList<Section> sections = line.getSections().getSections();

        // then
        assertSoftly(softly -> {
            softly.assertThat(sections).hasSize(3);
            softly.assertThat(sections.get(1).getRight().getName()).isEqualTo("삼성역");
        });
    }

    @DisplayName("노선에 구간을 삭제한다. - 상행 종점을 삭제한다.")
    @Test
    void deleteSectionAtLeftEnd() {
        // when
        line.deleteSection(station1);
        LinkedList<Section> sections = line.getSections().getSections();

        // then
        assertSoftly(softly -> {
            softly.assertThat(sections).hasSize(1);
            softly.assertThat(sections.get(0).getLeft()).isEqualTo(station2);
            softly.assertThat(sections.get(0).getRight()).isEqualTo(station3);
        });
    }

    @DisplayName("노선에 구간을 삭제한다. - 하행 종점을 삭제한다.")
    @Test
    void deleteSectionAtRightEnd() {
        // when
        line.deleteSection(station3);
        LinkedList<Section> sections = line.getSections().getSections();

        // then
        assertSoftly(softly -> {
            softly.assertThat(sections).hasSize(1);
            softly.assertThat(sections.get(0).getLeft()).isEqualTo(station1);
            softly.assertThat(sections.get(0).getRight()).isEqualTo(station2);
        });
    }

    @DisplayName("노선에 구간을 삭제한다. - 역과 역 사이의 역을 삭제한다.")
    @Test
    void deleteSectionMiddle() {
        // when
        line.deleteSection(station2);
        LinkedList<Section> sections = line.getSections().getSections();

        // then
        assertSoftly(softly -> {
            softly.assertThat(sections).hasSize(1);
            softly.assertThat(sections.get(0).getLeft()).isEqualTo(station1);
            softly.assertThat(sections.get(0).getRight()).isEqualTo(station3);
        });
    }
}
