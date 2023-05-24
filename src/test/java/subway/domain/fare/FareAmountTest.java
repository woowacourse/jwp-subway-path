package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "SpellCheckingInspection"})
class FareAmountTest {

    @Test
    void from_메소드는_0_이하의_값을_전달하면_예외가_발생한다() {
        assertThatThrownBy(() -> FareAmount.from(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("요금은 양수여야 합니다.");
    }
}
