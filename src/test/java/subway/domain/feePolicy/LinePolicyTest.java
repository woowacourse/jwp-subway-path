package subway.domain.feePolicy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import subway.domain.Line;
import subway.exception.LineNotFoundException;
import java.util.Collections;
import java.util.List;

class LinePolicyTest {

    @Test
    void 통과한_노선이_없으면_예외를_발생한다() {
        // given
        LinePolicy linePolicy = new LinePolicy();
        List<Line> passLines = Collections.emptyList();

        Assertions.assertThatThrownBy(
                () -> linePolicy.calculateExtraFee(passLines)
        ).isInstanceOf(LineNotFoundException.class);
    }
}
