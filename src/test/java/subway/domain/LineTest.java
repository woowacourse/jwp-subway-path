package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.service.domain.Distance;
import subway.service.domain.Line;
import subway.service.domain.LineProperty;
import subway.service.domain.Section;
import subway.service.domain.Sections;
import subway.service.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

}
