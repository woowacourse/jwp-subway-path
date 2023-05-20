package subway.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static subway.TestData.남성역;
import static subway.TestData.당곡역;
import static subway.TestData.보라매병원역;
import static subway.TestData.보라매역;
import static subway.TestData.봉천역;
import static subway.TestData.사당역;
import static subway.TestData.상도역;
import static subway.TestData.서울대입구역;
import static subway.TestData.서원역;
import static subway.TestData.신림역;
import static subway.TestData.장승배기역;
import static subway.TestData.총신대입구역;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import subway.TestData;
import java.util.List;
import java.util.stream.Stream;

class PathFinderTest {

    private PathFinder pathFinder;

    @BeforeEach
    void setUp() {
        this.pathFinder = new PathFinder(TestData.getAllSections().getSections());
    }

    @Test
    void 모든_Section_정보를_토대로_그래프_자료구조를_초기화한다() {
        Graph<Station, DefaultEdge> subwayRoute = pathFinder.getSubwayRoute();

        assertThat(subwayRoute.vertexSet().isEmpty()).isFalse();
        assertThat(subwayRoute.edgeSet().isEmpty()).isFalse();
    }

    @ParameterizedTest
    @MethodSource("startToEndStationAndExpectRoute")
    void 시작역과_도착역의_최단_경로를_구한다(final Station startStation, final Station endStation, final List<Station> expect) {
        List<Station> result = pathFinder.findShortestPath(startStation, endStation);

        assertThat(result).isEqualTo(expect);
    }

    private static Stream<Arguments> startToEndStationAndExpectRoute() {
        return Stream.of(
                Arguments.arguments(서원역, 상도역, List.of(서원역, 신림역, 당곡역, 보라매병원역, 보라매역, 장승배기역, 상도역)),
                Arguments.arguments(서울대입구역, 보라매역, List.of(서울대입구역, 봉천역, 신림역, 당곡역, 보라매병원역, 보라매역)),
                Arguments.arguments(서울대입구역, 장승배기역, List.of(서울대입구역, 사당역, 총신대입구역, 남성역, 상도역, 장승배기역))
        );
    }

    @Test
    void 최단_경로를_구할_때_존재하지_않은_역이_포함되면_예외가_발생한다() {
        Station 천안역 = new Station("천안역");

        assertThatIllegalArgumentException().isThrownBy(
                () -> pathFinder.findShortestPath(신림역, 천안역)
        );
    }

    @ParameterizedTest
    @MethodSource("startToEndStationAndExpectDistance")
    void 시작역과_도착역의_최단_거리를_구한다(final Station startStation, final Station endStation, final double expect) {
        var result = pathFinder.calculateShortestDistance(startStation, endStation);

        assertThat(result).isEqualTo(expect);
    }

    private static Stream<Arguments> startToEndStationAndExpectDistance() {
        return Stream.of(
                Arguments.arguments(서원역, 상도역, 34.0),
                Arguments.arguments(서울대입구역, 보라매역, 27.0),
                Arguments.arguments(서울대입구역, 장승배기역, 22.0)
        );
    }

    @Test
    void 최단_거리를_구할_때_존재하지_않은_역이_포함되면_예외가_발생한다() {
        Station 천안역 = new Station("천안역");

        assertThatIllegalArgumentException().isThrownBy(
                () -> pathFinder.calculateShortestDistance(신림역, 천안역)
        );
    }
}
