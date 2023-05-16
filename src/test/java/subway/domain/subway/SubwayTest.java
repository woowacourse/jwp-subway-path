package subway.domain.subway;

import static fixtures.SubwayFixtures.GANGNAM;
import static fixtures.SubwayFixtures.GYODAE;
import static fixtures.SubwayFixtures.NAMBU;
import static fixtures.SubwayFixtures.YANGJAE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.line.Line;

class SubwayTest {

    @Test
    @DisplayName("최단 경로와 최단 거리를 확인한다.")
    void findShortestPat() {
        final Line lineOfTwo = new Line(2L, "2호선", "초록색");
        final Line lineOfThree = new Line(3L, "3호선", "주황색");
        final Line lineOfNew = new Line(9L, "9호선", "빨간색");
        lineOfTwo.addSection(GYODAE, GANGNAM, 20);
        lineOfThree.addSection(GYODAE, NAMBU, 5);
        lineOfThree.addSection(NAMBU, YANGJAE, 5);
        lineOfNew.addSection(GANGNAM, YANGJAE, 5);

        final SubwayGraph subwayGraph = new SubwayJgraphtGraph(List.of(lineOfTwo, lineOfThree, lineOfNew));
        final Subway subway = new Subway(subwayGraph);
        final Path result = subway.findShortestPath(GYODAE, GANGNAM);

        assertAll(
                () -> assertThat(result.getStations()).containsExactly(GYODAE, NAMBU, YANGJAE, GANGNAM),
                () -> assertThat(result.getDistance()).isEqualTo(15)
        );
    }
}
