package subway.domain.subwaymap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.pathfinder.JgraphtShortestPathFinder;
import subway.domain.pathfinder.ShortestPath;

class SubwayMapTest {

    private static final Line FIRST_LINE = Line.of(1L, "1호선", "빨강", 100);
    private static final Line SECOND_LINE = Line.of(2L, "2호선", "빨강", 0);
    private static final Station 첫번쩨_ = Station.of(1L, "첫번째");
    private static final Station 두번째_ = Station.of(2L, "두번째");
    private static final Station 세번째_ = Station.of(3L, "세번째");

    @DisplayName("노선이 다른 두 역사이의 최단 경로와 거리를 구한다.")
    @Test
    void getShortestPath_differentStation() {
        //given
        final Sections firstLineInfo = Sections.of(
            List.of(Section.withNullId(첫번쩨_, 두번째_, 10), Section.withNullId(두번째_, 세번째_,
                15)));
        final Sections secondLineInfo = Sections.of(List.of(Section.withNullId(첫번쩨_, 세번째_, 24)));
        final SubwayMap subwayMap = new SubwayMap(List.of(FIRST_LINE, SECOND_LINE),
            List.of(firstLineInfo, secondLineInfo), new JgraphtShortestPathFinder());

        //when
        final ShortestPath shortestPath = subwayMap.getShortestPath(첫번쩨_, 세번째_);

        //then
        assertAll(
            () -> assertThat(shortestPath.getDistance()).isEqualTo(24),
            () -> assertThat(shortestPath.getPath()).hasSize(2),
            () -> assertThat(shortestPath.getPath().get(0).getStation()).isEqualTo(첫번쩨_),
            () -> assertThat(shortestPath.getPath().get(1).getStation()).isEqualTo(세번째_),
            () -> assertThat(shortestPath.getPath().get(1).getLine()).isEqualTo(SECOND_LINE)
        );
    }

    @DisplayName("같은 역일 때 최단 거리를 구한다.")
    @Test
    void getShortestPath_sameStation() {
        //given
        final Sections firstLineInfo = Sections.of(List.of(Section.withNullId(첫번쩨_, 두번째_, 10)));
        final SubwayMap subwayMap = new SubwayMap(List.of(FIRST_LINE), List.of(firstLineInfo),
            new JgraphtShortestPathFinder());

        //when
        final ShortestPath shortestPath = subwayMap.getShortestPath(첫번쩨_, 첫번쩨_);

        //then
        assertAll(
            () -> assertThat(shortestPath.getDistance()).isEqualTo(0),
            () -> assertThat(shortestPath.getPath()).hasSize(1),
            () -> assertThat(shortestPath.getPath().get(0).getStation()).isEqualTo(첫번쩨_)
        );
    }
}
