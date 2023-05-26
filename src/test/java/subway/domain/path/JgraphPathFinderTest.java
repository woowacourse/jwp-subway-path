package subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.domain.line.Line;
import subway.domain.line.LineColor;
import subway.domain.line.LineName;
import subway.domain.section.Distance;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.StationName;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class JgraphPathFinderTest {

    @Test
    void 상행_출발역과_하행_도착역을_받아_경로를_찾는다() {
        // given
        final Station A = new Station(1L, new StationName("A"));
        final Station B = new Station(2L, new StationName("B"));
        final Station C = new Station(3L, new StationName("C"));
        final Section section1 = new Section(1L, A, B, new Distance(5));
        final Section section2 = new Section(2L, B, C, new Distance(6));

        final Line line = new Line(1L, new LineName("1호선"), new LineColor("파랑"),
                new Sections(List.of(section1, section2)));

        // when
        final JgraphPathFinder jgraphPathFinder = new JgraphPathFinder(List.of(line));

        // then
        final Path path = jgraphPathFinder.findPath(A, C);
        assertThat(path.getPathStations().stations()).containsExactly(A, B, C);
    }

    @Test
    void 하행_출발역과_상행_도착역을_받아_경로를_찾는다() {
        // given
        final Station A = new Station(1L, new StationName("A"));
        final Station B = new Station(2L, new StationName("B"));
        final Station C = new Station(3L, new StationName("C"));
        final Section section1 = new Section(1L, A, B, new Distance(5));
        final Section section2 = new Section(2L, B, C, new Distance(6));

        final Line line = new Line(1L, new LineName("1호선"), new LineColor("파랑"),
                new Sections(List.of(section1, section2)));

        // when
        final JgraphPathFinder jgraphPathFinder = new JgraphPathFinder(List.of(line));

        // then
        final Path path = jgraphPathFinder.findPath(C, A);
        assertThat(path.getPathStations().stations()).containsExactly(C, B, A);
    }

    @Test
    void 환승을_포함하여_경로를_계산한다() {
        // given
        final Station A = new Station(1L, new StationName("A"));
        final Station B = new Station(2L, new StationName("B"));
        final Station C = new Station(3L, new StationName("C"));
        final Station D = new Station(4L, new StationName("D"));

        final Section section1 = new Section(1L, A, B, new Distance(5));
        final Section section2 = new Section(2L, B, C, new Distance(6));
        final Section section3 = new Section(3L, D, B, new Distance(6));

        final Line line1 = new Line(1L, new LineName("1호선"), new LineColor("파랑"),
                new Sections(List.of(section1, section2)));
        final Line line2 = new Line(2L, new LineName("2호선"), new LineColor("초록"),
                new Sections(List.of(section3)));

        // when
        final JgraphPathFinder jgraphPathFinder = new JgraphPathFinder(List.of(line1, line2));

        // then
        final Path path = jgraphPathFinder.findPath(D, C);
        assertThat(path.getPathStations().stations()).containsExactly(D, B, C);
    }

    @Test
    void 출발역과_도착역_사이에_경로가_존재하지_않으면_예외를_발생한다() {
        // given
        final Station A = new Station(1L, new StationName("A"));
        final Station B = new Station(2L, new StationName("B"));
        final Station C = new Station(3L, new StationName("C"));
        final Station D = new Station(4L, new StationName("D"));
        final Station E = new Station(5L, new StationName("E"));

        final Section section1 = new Section(1L, A, B, new Distance(5));
        final Section section2 = new Section(2L, B, C, new Distance(6));
        final Section section3 = new Section(3L, D, E, new Distance(6));

        final Line line1 = new Line(1L, new LineName("1호선"), new LineColor("파랑"),
                new Sections(List.of(section1, section2)));
        final Line line2 = new Line(2L, new LineName("2호선"), new LineColor("초록"),
                new Sections(List.of(section3)));

        // when
        final JgraphPathFinder jgraphPathFinder = new JgraphPathFinder(List.of(line1, line2));

        // then
        assertThatThrownBy(() -> jgraphPathFinder.findPath(A, D))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("두 역은 이어져 있지 않습니다.");
    }

    @Test
    void 출발역과_도착역_중_하나의_역이라도_존재하지_않으면_예외가_발생한다() {
        // given
        final Station A = new Station(1L, new StationName("A"));
        final Station B = new Station(2L, new StationName("B"));
        final Station C = new Station(3L, new StationName("C"));
        final Station D = new Station(4L, new StationName("D"));

        final Section section1 = new Section(1L, A, B, new Distance(5));
        final Section section2 = new Section(2L, B, C, new Distance(6));

        final Line line1 = new Line(1L, new LineName("1호선"), new LineColor("파랑"),
                new Sections(List.of(section1, section2)));

        // when
        final JgraphPathFinder jgraphPathFinder = new JgraphPathFinder(List.of(line1));

        // then
        assertThatThrownBy(() -> jgraphPathFinder.findPath(A, D))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("전체 경로상 없는 역이 있습니다.");
    }
}
