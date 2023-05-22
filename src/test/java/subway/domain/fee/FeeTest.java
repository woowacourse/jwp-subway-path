package subway.domain.fee;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FeeTest {

    @Test
    @DisplayName("덧셈 테스트")
    void add_test() {
        // given
        final Fee fee1 = new Fee(1000);
        final Fee fee2 = new Fee(300);

        // when
        final Fee added = fee1.add(fee2);

        // then
        assertThat(added).isEqualTo(new Fee(1300));
    }
}
