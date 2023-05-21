package subway.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class CostTest {

    @ParameterizedTest(name = "기본 운임(10km 이하) 요금은 1250원이다.")
    @ValueSource(ints = {1, 3, 5, 10})
    void calculateDefaultCost(int distance) {
        Cost cost = new Cost(new Distance(distance));

        assertThat(cost.getCost()).isEqualTo(1250);
    }

    @ParameterizedTest(name = "중거리 운행(10km ~ 50km)은 5km당 100원의 추가요금이 붙는다.")
    @MethodSource("middleDistanceProvider")
    void calculateMiddleDistanceCost(Distance distance, int expectedCost) {
        Cost cost = new Cost(distance);

        assertThat(cost.getCost()).isEqualTo(expectedCost);
    }

    static Stream<Arguments> middleDistanceProvider() {
        return Stream.of(
                Arguments.arguments(new Distance(11), 1550),
                Arguments.arguments(new Distance(23), 1750),
                Arguments.arguments(new Distance(50), 2250)
        );
    }

    @ParameterizedTest(name = ("장거리 운행(50km 초과)은 8km당 100원의 추가요금이 붙는다."))
    @MethodSource("longDistanceProvider")
    void calculateLongDistanceCost(Distance distance, int expectedCost) {
        Cost cost = new Cost(distance);

        assertThat(cost.getCost()).isEqualTo(expectedCost);
    }

    static Stream<Arguments> longDistanceProvider() {
        return Stream.of(
                Arguments.arguments(new Distance(51), 1950),
                Arguments.arguments(new Distance(100), 2550),
                Arguments.arguments(new Distance(200), 3750)
        );
    }
}
