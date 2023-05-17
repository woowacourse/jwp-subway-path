package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;

import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import subway.domain.pathfinder.JgraphtPathFinder;
import subway.domain.pathfinder.LineWeightedEdge;

class JgraphtPathFinderTest {

    public static final Section SECTION_1 = new Section(1L, 2L, 1L, 2);
    public static final Section SECTION_2 = new Section(2L, 3L, 1L, 3);
    public static final Section SECTION_3 = new Section(3L, 4L, 1L, 4);
    public static final Section SECTION_4 = new Section(4L, 5L, 1L, 5);
    public static final Section SECTION_5 = new Section(3L, 6L, 2L, 6);
    public static final Section SECTION_6 = new Section(6L, 7L, 2L, 7);
    public static final Section SECTION_7 = new Section(7L, 8L, 2L, 8);
    public static final Section SECTION_8 = new Section(1L, 4L, 3L, 4);
    public static final Section SECTION_9 = new Section(4L, 6L, 3L, 5);
    public static final Section SECTION_10 = new Section(6L, 8L, 3L, 6);
    JgraphtPathFinder jgraphtPathFinder = new JgraphtPathFinder(new WeightedMultigraph<>(LineWeightedEdge.class));

    @BeforeEach
    void setUp() {

        final List<Section> sections = List.of(
                SECTION_1,
                SECTION_2,
                SECTION_3,
                SECTION_4,
                SECTION_5,
                SECTION_6,
                SECTION_7,
                SECTION_8,
                SECTION_9,
                SECTION_10
        );

        jgraphtPathFinder.addSections(sections);
    }

    public static Stream<Arguments> provideStationIdsAndPath() {
        return Stream.of(
                Arguments.of(1L, 4L, List.of(SECTION_8)),
                Arguments.of(1L, 5L, List.of(SECTION_8, SECTION_4)),
                Arguments.of(1L, 8L, List.of(SECTION_8, SECTION_9, SECTION_10))
        );
    }

    public static Stream<Arguments> provideStationIdsAndDistance() {
        return Stream.of(
                Arguments.of(1L, 4L, 4),
                Arguments.of(1L, 5L, 9),
                Arguments.of(1L, 8L, 15)
        );
    }

    @ParameterizedTest(name = "최단경로를 구한다.")
    @MethodSource("provideStationIdsAndPath")
    void computeShortestPath(Long sourceStationId, Long targetStationId, List<Section> expected) {
        //when
        final List<Section> result = jgraphtPathFinder.computeShortestPath(sourceStationId, targetStationId);

        //then
        assertThat(result).containsExactlyElementsOf(expected);
    }


    @ParameterizedTest(name = "최단 거리를 구한다.")
    @MethodSource("provideStationIdsAndDistance")
    void computeShortestDistance(Long sourceStationId, Long targetStationId, int expected) {
        //when
        final int result = jgraphtPathFinder.computeShortestDistance(sourceStationId, targetStationId);

        //then
        assertThat(result).isEqualTo(expected);
    }
}
