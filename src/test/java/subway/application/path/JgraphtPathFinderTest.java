package subway.application.path;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.MultiLineSections;
import subway.domain.Section;
import subway.domain.ShortestPath;
import subway.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
class JgraphtPathFinderTest {

    private JgraphtPathFinder jgraphtPathFinder;
    private MultiLineSections sections;
    private Station 석촌;
    private Station 잠실새내;

    @BeforeEach
    void init() {
        jgraphtPathFinder = new JgraphtPathFinder(new WeightedMultigraph<>(DefaultWeightedEdge.class));
        sections = createSample();
    }

    /**
     * 8호선                    9호선
     * 몽촌토성                올림픽공원
     * |                 /
     * 10               20
     * |              /
     * 2호선 : 잠실 -- 10 -- 잠실새내 -- 20 -- 삼성 -- 15 -- 선릉
     * |          /
     * 15       30
     * |      /
     * |    /
     * 석촌
     */
    @Test
    void 최단_거리를_조회한다() {
        // when
        final ShortestPath shortestPath = jgraphtPathFinder.findShortestPath(sections, 석촌, 잠실새내);
        System.out.println(shortestPath.getRoutes());
        // then
        assertAll(
                () -> assertThat(shortestPath.getRoutes()).extracting("name")
                        .containsExactly("석촌", "잠실", "잠실새내"),
                () -> assertThat(shortestPath.getDistance().getDistance()).isEqualTo(25)
        );
    }

    private MultiLineSections createSample() {
        final Long 이호선 = new Line(1L, "2호선", "초록색").getId();
        final Long 팔호선 = new Line(2L, "8호선", "분홍색").getId();
        final Long 구호선 = new Line(3L, "9호선", "갈색").getId();

        final Station 잠실 = new Station(1L, "잠실");
        잠실새내 = new Station(2L, "잠실새내");
        final Station 삼성 = new Station(3L, "삼성");
        final Station 선릉 = new Station(4L, "선릉");
        석촌 = new Station(5L, "석촌");
        final Station 몽촌토성 = new Station(6L, "몽촌토성");
        final Station 올림픽공원 = new Station(7L, "올림픽공원");

        final Section 이호선_구간1 = new Section(1L, Distance.from(10), 잠실, 잠실새내, 이호선);
        final Section 이호선_구간2 = new Section(2L, Distance.from(20), 잠실새내, 삼성, 이호선);
        final Section 이호선_구간3 = new Section(3L, Distance.from(15), 삼성, 선릉, 이호선);

        final Section 팔호선_구간1 = new Section(4L, Distance.from(15), 석촌, 잠실, 팔호선);
        final Section 팔호선_구간2 = new Section(5L, Distance.from(10), 잠실, 몽촌토성, 팔호선);

        final Section 구호선_구간1 = new Section(6L, Distance.from(30), 석촌, 잠실새내, 구호선);
        final Section 구호선_구간2 = new Section(7L, Distance.from(20), 잠실새내, 올림픽공원, 구호선);

        return MultiLineSections.from(List.of(이호선_구간1, 이호선_구간2, 이호선_구간3, 팔호선_구간1, 팔호선_구간2, 구호선_구간1, 구호선_구간2));
    }
}
