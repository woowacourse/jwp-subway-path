package subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static subway.integration.TestFixture.GANGNAM;
import static subway.integration.TestFixture.SAMSUNG;
import static subway.integration.TestFixture.SECTIONS;
import static subway.integration.TestFixture.SEONGLENUG;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.Distance;
import subway.domain.section.Section;

class ShortestPathFinderTest {

    private final ShortestPathFinder finder = new DijkstraShortestPathFinder();

    @DisplayName("List<Section>과 출발점 도착점을 넣으면 최단 경로를 반환한다.")
    @Test
    void findShortestPath() {
        //강남 - 5 - 삼성 - 5 - 선릉
        //given,when
        final List<Section> shortestPath = finder.findShortestPath(SECTIONS.getSections(), GANGNAM, SAMSUNG);

        assertThat(shortestPath)
                .extracting(Section::getPrevStation, Section::getNextStation, Section::getDistance)
                .containsExactly(
                        tuple(GANGNAM, SEONGLENUG, new Distance(5)),
                        tuple(SEONGLENUG, SAMSUNG, new Distance(5))
                );
    }
}
