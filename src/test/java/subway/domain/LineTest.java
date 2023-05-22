package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.line.Line;
import subway.domain.line.LineName;
import subway.domain.section.Distance;
import subway.domain.section.Section;
import subway.domain.section.Sections;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.integration.IntegrationFixture.STATION_A;
import static subway.integration.IntegrationFixture.STATION_B;

class LineTest {

    @DisplayName("역이 두개만 있을 때 하나를 제거하는 경우")
    @Test
    void removeWhenLineHas2Station() {
        final Line line = new Line(1L, new LineName("2호선"), new Sections(new ArrayList<>()));
        final Section section1 = new Section(STATION_A, STATION_B, new Distance(7));
        final Line newLine = line.addSection(section1);

        final Line removedLine = newLine.removeStation(STATION_B);
        final List<Section> sections = removedLine.getSections().getSections();

        assertThat(sections.isEmpty()).isTrue();
    }
}
