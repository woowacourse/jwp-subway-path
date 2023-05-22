package subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.TestFixture.GANGNAM;
import static subway.TestFixture.JAMSIL;
import static subway.TestFixture.SAMSUNG;
import static subway.TestFixture.SECTIONS;
import static subway.TestFixture.SEONGLENUG;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.Distance;
import subway.domain.section.Section;
import subway.exception.CanNotFindPathException;
import subway.exception.StationNotFoundException;

class ShortestPathFinderTest {

    private final ShortestPathFinder finder = new DijkstraShortestPathFinder();

    @DisplayName("List<Section>과 출발점 도착점을 넣으면 최단 경로를 반환한다.")
    @Test
    void findShortestPath() {
        //강남 - 5 - 삼성 - 5 - 선릉
        //given,when
        final Path shortestPath = finder.findShortestPath(SECTIONS.getSections(), GANGNAM, SAMSUNG);

        assertThat(shortestPath.getStations())
                .containsExactly(GANGNAM, SEONGLENUG, SAMSUNG);
    }

    @DisplayName("출발지에서 도착지까지 이동할 수 있는 경로가 없는 경우 예외를 반환한다.")
    @Test
    void canNotFindPath() {
        final Section section1 = new Section(GANGNAM, SEONGLENUG, new Distance(2));
        final Section section2 = new Section(SAMSUNG, JAMSIL, new Distance(2));
        final List<Section> sections = List.of(section1, section2);

        assertThatThrownBy(() -> finder.findShortestPath(sections, GANGNAM, JAMSIL))
                .isInstanceOf(CanNotFindPathException.class);
    }

    @DisplayName("출발지나 도착지가 Sections에 없는 경우 예외를 반환한다.")
    @Test
    void canNotFindStation() {
        final Section section1 = new Section(GANGNAM, SEONGLENUG, new Distance(2));
        final Section section2 = new Section(SEONGLENUG, JAMSIL, new Distance(2));
        final List<Section> sections = List.of(section1, section2);

        assertThatThrownBy(() -> finder.findShortestPath(sections, GANGNAM, SAMSUNG))
                .isInstanceOf(StationNotFoundException.class);
    }
}
