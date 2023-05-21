package subway.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.section.Distance;

import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static subway.common.fixture.DomainFixture.이호선_초록색_침착맨_디노_로운;
import static subway.common.fixture.DomainFixture.일호선_남색_후추_디노_조앤;
import static subway.common.fixture.DomainFixture.침착맨;
import static subway.common.fixture.DomainFixture.침착맨_디노;
import static subway.common.fixture.DomainFixture.후추;
import static subway.common.fixture.DomainFixture.후추_디노;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PathFinderTest {

    // 일호선 : 후추 - 7 - 디노 - 4 - 조앤
    // 이호선 : 침착맨 - 5 - 디노 - 2 - 로운
    @Test
    void 최단_경로를_찾는다() {
        //given
        final PathFinder pathFinder = new PathFinder(new SubwayMap(List.of(일호선_남색_후추_디노_조앤, 이호선_초록색_침착맨_디노_로운)));

        //when
        final Path shortestPath = pathFinder.findShortest(후추, 침착맨);

        //then
        assertSoftly(softly -> {
            softly.assertThat(shortestPath.getDistance()).isEqualTo(new Distance(12));
            softly.assertThat(shortestPath.getSectionEdges())
                    .isEqualTo(List.of(
                            new SectionEdge(1L, 후추_디노),
                            new SectionEdge(2L, 침착맨_디노)
                    ));
        });
    }
}
