package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import subway.domain.pathfinder.JgraphtPathFinder;

class JgraphtPathFinderTest {

    JgraphtPathFinder jgraphtPathFinder = new JgraphtPathFinder(new WeightedMultigraph<>(DefaultWeightedEdge.class));

    @BeforeEach
    void setUp() {
        final Section section1 = new Section(1L, 2L, 1L, 2);
        final Section section2 = new Section(2L, 3L, 1L, 3);
        final Section section3 = new Section(3L, 4L, 1L, 4);
        final Section section4 = new Section(4L, 5L, 1L, 5);

        final Section section5 = new Section(3L, 6L, 2L, 6);
        final Section section6 = new Section(6L, 7L, 2L, 7);
        final Section section7 = new Section(7L, 8L, 2L, 8);

        final Section section8 = new Section(1L, 4L, 3L, 4);
        final Section section9 = new Section(4L, 6L, 3L, 5);
        final Section section10 = new Section(6L, 8L, 3L, 6);

        final List<Section> sections = List.of(
                section1,
                section2,
                section3,
                section4,
                section5,
                section6,
                section7,
                section8,
                section9,
                section10
        );

        jgraphtPathFinder.addSections(sections);
    }

    public static Stream<Arguments> provideStationIdsAndPath() {
        return Stream.of(
                Arguments.of(1L, 4L, List.of(1L, 4L)),
                Arguments.of(1L, 5L, List.of(1L, 4L, 5L)),
                Arguments.of(1L, 5L, List.of(1L, 4L, 5L)),
                Arguments.of(1L, 8L, List.of(1L, 4L, 6L, 8L))
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
    void computeShortestPath(Long sourceStationId, Long targetStationId, List<Long> expected) {
        //when
        final List<Long> result = jgraphtPathFinder.computeShortestPath(sourceStationId, targetStationId);

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
