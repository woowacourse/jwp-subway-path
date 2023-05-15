package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("DefaultFeePolicy 은(는)")
class DefaultFeePolicyTest {

    private static final int DEFAULT_FEE = 1250;

    private final DefaultFeePolicy defaultFeePolicy = new DefaultFeePolicy();

    @ParameterizedTest
    @ValueSource(ints = {1,5,8,10})
    void 거리가_10km_이하면_기본요금만_반환한다(int distance) {
        // when
        int actual = defaultFeePolicy.calculate(distance);

        // then
        assertThat(actual).isEqualTo(DEFAULT_FEE);
    }

    @ParameterizedTest
    @ValueSource(ints = {-100, -1, 0})
    void 거리가_0이하면_예외(int distance) {
        // when & then
        assertThatThrownBy(() -> defaultFeePolicy.calculate(distance))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Nested
    class 거리가_10km를_넘어갈_떄 {

        @ParameterizedTest
        @CsvSource(value= {"12:1350", "15:1350", "16:1450", "20:1450", "23:1550"}, delimiter = ':')
        void 거리가_50km이하면_5km마다_추가요금이_붙는다(int distance, int expected) {
            // when
            int actual = defaultFeePolicy.calculate(distance);

            // then
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource(value= {"58:2150", "59:2250", "66:2250", "67:2350"}, delimiter = ':')
        void 거리가_50km을_초과하면_50km_이상의_거리는_8km마다_추가요금이_붙는다(int distance, int expected) {
            // when
            int actual = defaultFeePolicy.calculate(distance);

            // then
            assertThat(actual).isEqualTo(expected);
        }
    }
}
