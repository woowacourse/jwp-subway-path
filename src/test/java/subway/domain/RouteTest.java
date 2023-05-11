package subway.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class RouteTest {

    @Test
    void 호선이_존재하지_않는_경우_역을_추가할_수_없다() {
        // given
        final Route route = new Route(new HashMap<>());
        final Line line = new Line(1L, "2호선", "green");

        // expect
        assertThatThrownBy(() -> route.insertSection(line, new Station(1L, "디노"), new Station(2L, "후추"), 7))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 호선이 존재하지 않습니다.");
    }

    @Test
    void 호선을_추가한다() {
        // given
        final Line line = new Line(1L, "2호선", "green");
        final Route route = new Route(new HashMap<>());
        // when
        route.insertLine(line);
        // then
        assertThat(route.getSectionsByLine()).containsOnlyKeys(new Line(1L, "2호선", "green"));
    }

    @Test
    void 역을_추가한다() {
        // given
        final Line lineNumber2 = new Line(1L, "2호선", "green");
        final Station 뚝섬 = new Station(1L, "뚝섬");
        final Station 성수 = new Station(2L, "성수");

        final Route route = new Route(new HashMap<>());
        route.insertLine(lineNumber2);
        // when
        route.insertSection(lineNumber2, 뚝섬, 성수, 5);
        // then
        assertSoftly(softly -> {
            final Map<Line, Sections> sectionsByLine = route.getSectionsByLine();
            softly.assertThat(sectionsByLine).containsOnlyKeys(new Line(1L, "2호선", "green"));
            final Sections sections = sectionsByLine.get(new Line(1L, "2호선", "green"));
            softly.assertThat(sections.getSections()).contains(new Section(new Station(1L, "뚝섬"), new Station(2L, "성수"), 5));
        });
    }

    @Test
    void 역을_수정한다() {
        // given
        final Line lineNumber2 = new Line(1L, "2호선", "green");
        final Station 뚝섬 = new Station(1L, "뚝섬");
        final Station 성수 = new Station(2L, "성수");
        final Station 후추 = new Station(3L, "후추");

        final Route route = new Route(new HashMap<>());
        route.insertLine(lineNumber2);
        route.insertSection(lineNumber2, 뚝섬, 성수, 5);
        // when
        route.updateStation(lineNumber2, 성수, 후추);

        // then
        assertSoftly(softly -> {
            final Map<Line, Sections> sectionsByLine = route.getSectionsByLine();
            final Sections sections = sectionsByLine.get(new Line(1L, "2호선", "green"));
            softly.assertThat(sections.getSections()).contains(new Section(new Station(1L, "뚝섬"), new Station(3L, "후추"), 5));
        });
    }

    @Test
    void 호선을_수정한다() {
        // given
        final Line lineNumber2 = new Line(1L, "2호선", "green");
        final Line lineNumber8 = new Line("8호선", "pink");
        final Station 뚝섬 = new Station(1L, "뚝섬");
        final Station 성수 = new Station(2L, "성수");

        final Route route = new Route(new HashMap<>());
        route.insertLine(lineNumber2);
        route.insertSection(lineNumber2, 뚝섬, 성수, 5);
        // when
        route.updateLine(lineNumber2, lineNumber8);
        // then
        assertSoftly(softly -> {
            final Map<Line, Sections> sectionsByLine = route.getSectionsByLine();
            softly.assertThat(sectionsByLine).doesNotContainKey(lineNumber2);
            softly.assertThat(sectionsByLine).containsKey(lineNumber8);
            final Sections sections = sectionsByLine.get(lineNumber8);
            softly.assertThat(sections.getSections()).containsOnly(new Section(뚝섬, 성수, 5));
        });
    }

    @Test
    void 호선이_존재하지_않으면_수정할_수_없다() {
        // given
        final Line lineNumber2 = new Line(1L, "2호선", "green");
        final Line lineNumber8 = new Line("8호선", "pink");

        final Route route = new Route(new HashMap<>());

        // expect
        assertThatThrownBy(() -> route.updateLine(lineNumber2, lineNumber8))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 호선이 존재하지 않습니다.");
    }
}
