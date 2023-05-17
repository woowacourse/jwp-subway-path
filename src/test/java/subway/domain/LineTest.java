package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.service.domain.Direction;
import subway.service.domain.Distance;
import subway.service.domain.Line;
import subway.service.domain.LineProperty;
import subway.service.domain.Section;
import subway.service.domain.Sections;
import subway.service.domain.Station;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.service.domain.Direction.UP;

class LineTest {

    @Test
    @DisplayName("Line 에 포함된 Section 에서 현재 Station 을 포함한 Section 모두를 가져온다.")
    void findSectionByStation() {
        LineProperty lineProperty = new LineProperty("7호선", "rg-olive-600");
        Station previousStation = new Station("previous");
        Station standardStation = new Station("standard");
        Station nextStation = new Station("next");
        Distance ignored = Distance.from(10);
        Sections sections = new Sections(List.of(
                new Section(previousStation, standardStation, ignored), new Section(standardStation, nextStation, ignored)));
        Line line = new Line(lineProperty, sections);

        List<Section> sectionsWithStandardStation = line.findSectionByStation(standardStation);

        assertThat(sectionsWithStandardStation)
                .hasSize(2)
                .allMatch(section -> section.getPreviousStation().equals(standardStation)
                        || section.getNextStation().equals(standardStation));
    }

    @Test
    @DisplayName("해당 Line Sections 에 추가하려는 Section 에 포함된 역이 모두 포함되거나, 하나도 포함되지 않은 경우는 예외가 발생한다.")
    void findSectionByDirectionAndStation_fail() {
        LineProperty lineProperty = new LineProperty("7호선", "rg-olive-600");
        Station previousStation = new Station("previous");
        Station standardStation = new Station("standard");
        Station nextStation = new Station("next");
        Distance ignored = Distance.from(10);
        Sections sections = new Sections(List.of(
                new Section(previousStation, standardStation, ignored), new Section(standardStation, nextStation, ignored)));
        Line line = new Line(lineProperty, sections);

        assertThatThrownBy(() -> line.findSectionByDirectionAndStation(UP, previousStation, nextStation))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> line.findSectionByDirectionAndStation(UP, new Station("not"), new Station("exists")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("인수로 넘어온 Station 에서 Direction 방향으로 있는 Section 을 가져온다. (조회 결과가 없는 경우)")
    void findSectionByDirectionAndStation_success_isNotPresent() {
        LineProperty lineProperty = new LineProperty("7호선", "rg-olive-600");
        Station previousStation = new Station("previous");
        Station nextStation = new Station("next");
        Station additionalStation = new Station("additional");
        Distance ignored = Distance.from(10);
        Sections sections = new Sections(List.of(
                new Section(previousStation, nextStation, ignored)));
        Line line = new Line(lineProperty, sections);

        Optional<Section> emptySectionByPreviousStation = line.findSectionByDirectionAndStation(Direction.DOWN, previousStation, additionalStation);
        Optional<Section> emptySectionByNextStation = line.findSectionByDirectionAndStation(UP, nextStation, additionalStation);

        assertThat(emptySectionByPreviousStation).isEmpty();
        assertThat(emptySectionByNextStation).isEmpty();
    }

    @Test
    @DisplayName("인수로 넘어온 Station 에서 Direction 방향으로 있는 Section 을 가져온다. (조회 결과가 있는 경우)")
    void findSectionByDirectionAndStation_success_isPresent() {
        LineProperty lineProperty = new LineProperty("7호선", "rg-olive-600");
        Station previousStation = new Station("previous");
        Station standardStation = new Station("standard");
        Station nextStation = new Station("next");
        Station additionalStation = new Station("additional");
        Distance ignored = Distance.from(10);
        Sections sections = new Sections(List.of(
                new Section(previousStation, standardStation, ignored), new Section(standardStation, nextStation, ignored)));
        Line line = new Line(lineProperty, sections);

        Optional<Section> standardToNextSection = line.findSectionByDirectionAndStation(UP, standardStation, additionalStation);
        Optional<Section> standardToPreviousSection = line.findSectionByDirectionAndStation(Direction.DOWN, standardStation, additionalStation);

        assertThat(standardToNextSection).isPresent();
        assertThat(standardToNextSection.get().getPreviousStation()).isEqualTo(standardStation);
        assertThat(standardToNextSection.get().getNextStation()).isEqualTo(nextStation);
        assertThat(standardToPreviousSection).isPresent();
        assertThat(standardToPreviousSection.get().getNextStation()).isEqualTo(standardStation);
        assertThat(standardToPreviousSection.get().getPreviousStation()).isEqualTo(previousStation);
    }

}
