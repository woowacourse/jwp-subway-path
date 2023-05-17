package subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.integration.TestFixture.GANGNAM;
import static subway.integration.TestFixture.SAMSUNG;
import static subway.integration.TestFixture.SECTIONS;
import static subway.integration.TestFixture.SEONGLENUG;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}
