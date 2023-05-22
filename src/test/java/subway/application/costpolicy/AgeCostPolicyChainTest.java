package subway.application.costpolicy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.domain.Path;
import subway.domain.vo.Age;

class AgeCostPolicyChainTest {

    private static final long DEFAULT_COST = 1250L;
    final CostPolicyChain costPolicyChain = new AgeCostPolicyChain();

    @DisplayName("어린이일 경우 연령별 요금을 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {6, 7, 8, 9, 10, 11, 12})
    void testCalculateWhenChild(final int value) {
        //given
        final Path path = new Path(null, null);

        //when
        final long result = costPolicyChain.calculate(path, Age.from(value), DEFAULT_COST);

        //then
        assertThat(result).isEqualTo(720L);
    }

    @DisplayName("청소년일 경우 연령별 요금을 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {13, 14, 15, 16, 17, 18})
    void testCalculateWhenTeenager(final int value) {
        //given
        final Path path = new Path(null, null);

        //when
        final long result = costPolicyChain.calculate(path, Age.from(value), DEFAULT_COST);

        //then
        assertThat(result).isEqualTo(450L);
    }

    @DisplayName("할인에 해당되지 않을 경우 연령별 요금을 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 19, 20, 21})
    void testCalculateWhenNothing(final int value) {
        //given
        final Path path = new Path(null, null);

        //when
        final long result = costPolicyChain.calculate(path, Age.from(value), DEFAULT_COST);

        //then
        assertThat(result).isEqualTo(1250L);
    }
}
