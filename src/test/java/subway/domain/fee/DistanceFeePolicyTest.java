package subway.domain.fee;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import subway.domain.Section;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class DistanceFeePolicyTest {

    private DistanceFeePolicy distanceFeePolicy = new DistanceFeePolicy();

    @DisplayName("10Km이하이면 요금이 1250원이다.")
    @Test
    void calculateDefaultFee() {
        // given
        List<Section> sections = List.of(
                new Section(1L, 2L, 1L, 3),
                new Section(1L, 2L, 1L, 5),
                new Section(1L, 2L, 1L, 2)
        );
        int expected = 1250;

        // when
        int actual = distanceFeePolicy.calculateFee(sections);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("10Km이상 50Km이하인 구간에는 5Km마다 요금이 100원 추가 부과된다.")
    @MethodSource("tenToFiftyProvider")
    @ParameterizedTest
    void calculateFeeBetween10KmAnd50KmDistance(List<Section> sections, int expected) {
        // when
        int actual = distanceFeePolicy.calculateFee(sections);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> tenToFiftyProvider() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                new Section(1L, 2L, 1L, 10),
                                new Section(1L, 2L, 1L, 20),
                                new Section(1L, 2L, 1L, 13)),
                        1950),
                Arguments.of(
                        List.of(
                                new Section(1L, 2L, 1L, 10),
                                new Section(1L, 2L, 1L, 20),
                                new Section(1L, 2L, 1L, 20)),
                        2050),
                Arguments.of(
                        List.of(
                                new Section(1L, 2L, 1L, 2),
                                new Section(1L, 2L, 1L, 5),
                                new Section(1L, 2L, 1L, 3)),
                        1250)
        );
    }

    @DisplayName("50Km이상인 구간에는 8Km마다 요금이 100원 추가 부과된다.")
    @MethodSource("overFiftyProvider")
    @ParameterizedTest
    void calculateFeeOver50KmDistance(List<Section> sections, int expected) {
        // when
        int actual = distanceFeePolicy.calculateFee(sections);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> overFiftyProvider() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                new Section(1L, 2L, 1L, 10),
                                new Section(1L, 2L, 1L, 20),
                                new Section(1L, 2L, 1L, 20)),
                        2050),
                Arguments.of(
                        List.of(
                                new Section(1L, 2L, 1L, 18),
                                new Section(1L, 2L, 1L, 20),
                                new Section(1L, 2L, 1L, 20)),
                        2150),
                Arguments.of(
                        List.of(
                                new Section(1L, 2L, 1L, 30),
                                new Section(1L, 2L, 1L, 20),
                                new Section(1L, 2L, 1L, 33)),
                        2550)
        );
    }
}
