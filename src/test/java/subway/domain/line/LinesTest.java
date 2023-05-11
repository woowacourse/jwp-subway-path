package subway.domain.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static subway.exception.ErrorCode.LINE_NAME_DUPLICATED;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.GlobalException;

class LinesTest {

    @Test
    @DisplayName("노선 이름이 중복되면 예외가 발생한다.")
    void add_fail_test() {
        // given
        final Line 신분당선 = new Line("신분당선", "bg-red-600");
        final Line 구신분당선 = new Line("구신분당선", "bg-red-600");
        final Lines lines = new Lines(Arrays.asList(신분당선, 구신분당선));

        // expect
        final Line 중복된_신분당선 = new Line("신분당선", "bg-red-500");
        assertThatThrownBy(() -> lines.validateDuplication(중복된_신분당선))
            .isInstanceOf(GlobalException.class)
            .extracting("errorCode")
            .isEqualTo(LINE_NAME_DUPLICATED);
    }

    @Test
    @DisplayName("중복되는 이름의 노선이 없으면, 노선을 추가할 수 있다.")
    void validateDuplication_success_test() {
        // given
        final Line 구신분당선 = new Line("구신분당선", "bg-red-600");
        final Lines lines = new Lines(new ArrayList<>());

        // expected
        assertDoesNotThrow(() -> lines.validateDuplication(구신분당선));
    }
}
