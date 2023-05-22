package subway.application.costPolicy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.domain.Path;
import subway.domain.vo.Distance;

class DistanceCostPolicyChainTest {

    private static final long DEFAULT_COST = 1250L;
    private final CostPolicyChain costPolicyChain = new DistanceCostPolicyChain();

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L})
    @DisplayName("총 거리가 10km 이하 일 때 비용을 계산한다.")
    void testCalculateLessThan10(final long value) {
        //given
        final Distance distance = new Distance(value);
        final Path path = new Path(null, distance);

        //when
        final long result = costPolicyChain.calculate(path, 0, DEFAULT_COST);

        //then
        assertThat(result).isEqualTo(DEFAULT_COST);
    }

    @ParameterizedTest
    @ValueSource(longs = {11L, 12L, 13L, 14L, 15L})
    @DisplayName("총 거리가 11km 이상 15km 이하일 때 비용을 계산한다.")
    void testCalculateExceedThan11LessThan15(final long value) {
        //given
        final Distance distance = new Distance(value);
        final Path path = new Path(null, distance);

        //when
        final long result = costPolicyChain.calculate(path, 0, DEFAULT_COST);

        //then
        assertThat(result).isEqualTo(1350L);
    }

    @ParameterizedTest
    @ValueSource(longs = {16L, 17L, 18L, 19L, 20L})
    @DisplayName("총 거리가 16km 이상 19km 이하일 때 비용을 계산한다.")
    void testCalculateExceedThan16LessThan19(final long value) {
        //given
        final Distance distance = new Distance(value);
        final Path path = new Path(null, distance);

        //when
        final long result = costPolicyChain.calculate(path, 0, DEFAULT_COST);

        //then
        assertThat(result).isEqualTo(1450L);
    }

    @ParameterizedTest
    @ValueSource(longs = {21L, 22L, 23L, 24L, 25L})
    @DisplayName("총 거리가 21km 이상 25km 이하일 때 비용을 계산한다.")
    void testCalculateExceedThan21LessThan25(final long value) {
        //given
        final Distance distance = new Distance(value);
        final Path path = new Path(null, distance);

        //when
        final long result = costPolicyChain.calculate(path, 0, DEFAULT_COST);

        //then
        assertThat(result).isEqualTo(1550L);
    }

    @ParameterizedTest
    @ValueSource(longs = {46L, 47L, 48L, 49L, 50L})
    @DisplayName("총 거리가 46km 이상 50km 이하일 때 비용을 계산한다.")
    void testCalculateExceedThan46LessThan50(final long value) {
        //given
        final Distance distance = new Distance(value);
        final Path path = new Path(null, distance);

        //when
        final long result = costPolicyChain.calculate(path, 0, DEFAULT_COST);

        //then
        assertThat(result).isEqualTo(2050L);
    }

    @ParameterizedTest
    @ValueSource(longs = {51L, 52L, 53L, 54L, 55L, 56L, 57L, 58L})
    @DisplayName("총 거리가 51km 이상 58km 이하일 때 비용을 계산한다.")
    void testCalculateExceedThan51LessThan55(final long value) {
        //given
        final Distance distance = new Distance(value);
        final Path path = new Path(null, distance);

        //when
        final long result = costPolicyChain.calculate(path, 0, DEFAULT_COST);

        //then
        assertThat(result).isEqualTo(2150L);
    }

    @ParameterizedTest
    @ValueSource(longs = {59L, 60L, 61L, 62L, 63L, 64L, 65L, 66L})
    @DisplayName("총 거리가 59km 이상 58km 이하일 때 비용을 계산한다.")
    void testCalculateExceedThan59LessThan66(final long value) {
        //given
        final Distance distance = new Distance(value);
        final Path path = new Path(null, distance);

        //when
        final long result = costPolicyChain.calculate(path, 0, DEFAULT_COST);

        //then
        assertThat(result).isEqualTo(2250L);
    }
}
