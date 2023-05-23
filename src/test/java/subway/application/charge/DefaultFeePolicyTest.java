package subway.application.charge;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DefaultFeePolicyTest {

    private ChargePolicy feePolicy;

    @BeforeEach
    void setUp() {
        feePolicy = new DefaultChargePolicy();
    }

    @Test
    void 기본운임을_계산한다() {
        int fee = feePolicy.calculateFee(9);

        assertThat(fee).isEqualTo(1250);
    }

    @Test
    void 짧은_거리의_추가_운임을_계산한다() {
        int fee = feePolicy.calculateFee(12);

        assertThat(fee).isEqualTo(1350);
    }

    @Test
    void 긴_거리의_추가_운임을_계산한다() {
        int fee = feePolicy.calculateFee(58);

        assertThat(fee).isEqualTo(2150);
    }
}
