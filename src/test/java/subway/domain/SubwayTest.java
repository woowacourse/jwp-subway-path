package subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import subway.domain.graph.SubwayGraph;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.fixture.LineFixture.LINE_2;
import static subway.fixture.LineFixture.LINE_999;
import static subway.fixture.StationFixture.EXPRESS_BUS_TERMINAL_STATION;
import static subway.fixture.StationFixture.SAPYEONG_STATION;

class SubwayTest {

    Subway subway;

    @BeforeEach
    void setUp() {
        final Sections line2 = new Sections(LINE_999, new SubwayGraph());
        final Sections line999 = new Sections(LINE_2, new SubwayGraph());
        line999.createInitialSection(EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION, 5);

        final List<Sections> sections = List.of(line2, line999);

        subway = new Subway(sections);
    }

    @Test
    void findAllTest() {
        final Map<Line, List<Station>> subwayAll = subway.findAll();

        assertThat(subwayAll.keySet())
                .contains(LINE_2, LINE_999);
    }

    @Test
    void findAllLinesTest() {
        final List<Line> lines = subway.findAllLines();

        assertThat(lines)
                .contains(LINE_2, LINE_999);
    }

    @Test
    void createSectionsOfTest() {
        final Line newLine = new Line(99L, "new line!", "YELLOW");
        subway.createSectionsOf(newLine, new SubwayGraph());

        assertThat(subway.findAllLines())
                .contains(newLine, LINE_2, LINE_999);
    }
}
