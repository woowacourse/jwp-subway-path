package subway.domain.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.LineNumberUnderMinimumNumber;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class LineNumberTest {

    @Test
    @DisplayName("lineNumber가 0보다 작으면 예외를 발생시킨다.")
    void throws_exception_when_line_number_invalid() {
        // when & then
        assertThatThrownBy(() -> new LineNumber(-1L))
                .isInstanceOf(LineNumberUnderMinimumNumber.class);
    }

    @Test
    @DisplayName("lineNumber를 생성한다.")
    void create_line_number_success() {
        // given
        Long givenLineNumber = 3L;

        // when
        LineNumber lineNumber = new LineNumber(givenLineNumber);

        // then
        assertThat(lineNumber.getLineNumber()).isEqualTo(givenLineNumber);
    }
}
