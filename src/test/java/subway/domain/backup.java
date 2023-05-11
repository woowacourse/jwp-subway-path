package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class backup {

    @Test
    @DisplayName("노선의 기존 구간에 역을 삽입할 수 있다.")
    void addSectionTest() {
        // given
        Sections sections = Sections.from(new ArrayList<>());
        Station station1 = Station.of(1L, "강변");
        Station station2 = Station.of(2L, "잠실");
        Station station3 = Station.of(3L, "잠실나루");
        Line line = Line.of(1L, "2호선", "초록색");
        Section originalSection = Section.of(station1, station2, 10, line);
        Section section1 = Section.of(station1, station3, 10, line);
        Section section2 = Section.of(station3, station2, 10, line);
        Section emptyUpwardSection = Section.ofEmptyUpwardSection(station1, line);
        Section emptyDownwardSection = Section.ofEmptyDownwardSection(station2, line);

        // when
        sections.addInitSection(station1, station2, 10, line);
        sections.addSection(station3, originalSection, 3, 7);
        List<Section> lineSections = sections.findLineSections(line);

        // then
        assertThat(lineSections).containsAll(List.of(section1, section2, emptyUpwardSection, emptyDownwardSection));
    }
}
