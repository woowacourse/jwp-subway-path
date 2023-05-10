package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class LineTest {

    private static final Station STATION_A = new Station(1L, "a");
    private static final Station STATION_B = new Station(2L, "b");
    private static final Station STATION_C = new Station(3L, "c");
    private static final Station STATION_D = new Station(4L, "d");

    @DisplayName("Line에 Section을 추가한다.")
    @Test
    void addSection() {
        final Line line = new Line(1L, new LineName("2호선"));
        final Section section1 = new Section(STATION_A, STATION_B, new Distance(7));
        final Section section2 = new Section(STATION_B, STATION_C, new Distance(10));
        final Section newSection = new Section(STATION_B, STATION_D, new Distance(3));

        line.addSection(section1);
        line.addSection(section2);
        line.addSection(newSection);

        final List<Section> sections = line.getSections();
        assertAll(
                () -> assertThat(sections)
                        .extracting(Section::getBeforeStation)
                        .extracting(Station::getName)
                        .containsExactly("a", "b", "d"),
                () -> assertThat(sections)
                        .extracting(Section::getNextStation)
                        .extracting(Station::getName)
                        .containsExactly("b", "d", "c"),
                () -> assertThat(sections)
                        .extracting(Section::getDistance)
                        .extracting(Distance::getValue)
                        .containsExactly(7, 3, 7)
        );
    }
}
