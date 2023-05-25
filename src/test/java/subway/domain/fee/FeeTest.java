package subway.domain.fee;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.path.TestDomainData;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    @DisplayName("0미만의 요금으로 생성되면 예외가 발생한다.")
    void construct_fail_when_amount_is_under_zero() {
        assertThatThrownBy(() -> new Fee(-1))
                .isInstanceOf(RuntimeException.class);
    }
}
