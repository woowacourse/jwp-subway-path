package subway.domain.fare.policy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.domain.fare.FareInformation;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("DefaultFeePolicy 은(는)")
class DistanceFarePolicyTest {

    private final DistanceFarePolicy distanceFeePolicy = new DistanceFarePolicy();

    @ParameterizedTest
    @ValueSource(ints = {1,5,8,10})
    void 거리가_10km_이하면_추가요금이_없다(final int distance) {
        // when
        final int actual = distanceFeePolicy.calculate(new FareInformation(distance, null, null));

        // then
        assertThat(actual).isEqualTo(0);
    }

    @ParameterizedTest
    @ValueSource(ints = {-100, -1, 0})
    void 거리가_0이하면_예외(final int distance) {
        // when & then
        assertThatThrownBy(() -> distanceFeePolicy.calculate(new FareInformation(distance, null, null)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Nested
    class 거리가_10km를_넘어갈_떄 {

        @ParameterizedTest
        @CsvSource(value= {"12:100", "15:100", "16:200", "20:200", "23:300"}, delimiter = ':')
        void 거리가_50km이하면_5km마다_추가요금이_붙는다(final int distance,final int expected) {
            // when
            final int actual = distanceFeePolicy.calculate(new FareInformation(distance, null, null));

            // then
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource(value= {"58:900", "59:1000", "66:1000", "67:1100"}, delimiter = ':')
        void 거리가_50km을_초과하면_50km_이상의_거리는_8km마다_추가요금이_붙는다(final int distance,final int expected) {
            // when
            final int actual = distanceFeePolicy.calculate(new FareInformation(distance, null, null));

            // then
            assertThat(actual).isEqualTo(expected);
        }
    }
}
