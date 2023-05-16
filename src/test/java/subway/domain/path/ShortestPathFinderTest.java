package subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.integration.TestFixture.GANGNAM;
import static subway.integration.TestFixture.SAMSUNG;
import static subway.integration.TestFixture.SECTIONS;
import static subway.integration.TestFixture.SEONGLENUG;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import subway.domain.section.Sections;

@Import(DijkstraShortestPathFinder.class)
class ShortestPathFinderTest {

    @Autowired
    private ShortestPathFinder finder;

    @DisplayName("List<Section>과 출발점 도착점을 넣으면 최단 경로를 반환한다.")
    @Test
    void findShortestPath() {
        //강남 - 5 - 삼성 - 5 - 선릉
        //given,when
        final Sections shortestPath = finder.findShortestPath(SECTIONS, GANGNAM, SEONGLENUG);

        assertThat(shortestPath.getAllStations())
                .containsExactly(GANGNAM, SAMSUNG, SEONGLENUG);
    }
}
