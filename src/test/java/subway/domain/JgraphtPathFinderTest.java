package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Stream;

import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import fixture.Fixture;
import subway.domain.pathfinder.JgraphtPathFinder;
import subway.domain.pathfinder.LineWeightedEdge;
import subway.exception.DomainException;
import subway.exception.ExceptionType;

class JgraphtPathFinderTest {
    JgraphtPathFinder jgraphtPathFinder = new JgraphtPathFinder(new WeightedMultigraph<>(LineWeightedEdge.class));

    @BeforeEach
    void setUp() {
        jgraphtPathFinder.addSections(Fixture.SECTIONS);
    }

    public static Stream<Arguments> provideStationIdsAndPath() {
        return Stream.of(
                Arguments.of(1L, 4L, List.of(Fixture.SECTION_8)),
                Arguments.of(1L, 5L, List.of(Fixture.SECTION_8, Fixture.SECTION_4)),
                Arguments.of(1L, 8L, List.of(Fixture.SECTION_8, Fixture.SECTION_9, Fixture.SECTION_10))
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

    @Test
    @DisplayName("station이 line에 존재하지 않아 경로 조회가 실패히면 예외가 발생한다.")
    void computeShortestPathFail1() {
        assertThatThrownBy(() -> jgraphtPathFinder.computeShortestPath(1L, 9L))
                .isInstanceOf(DomainException.class)
                .hasMessage(ExceptionType.STATION_IS_NOT_IN_SECTION.name());
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
