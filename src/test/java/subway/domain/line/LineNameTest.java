package subway.domain.line;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static subway.exception.ErrorCode.LINE_NAME_LENGTH;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.BadRequestException;

class LineNameTest {

    @ParameterizedTest(name = "세글자부터 열글자 사이의 이름은 정상 생성된다.")
    @ValueSource(strings = {"일이삼", "일이삼사오륙칠팔구십"})
    void line_name_success(final String name) {
        final LineName lineName = assertDoesNotThrow(() -> new LineName(name));
        assertThat(lineName)
            .extracting(LineName::name)
            .isEqualTo(name);
    }

    @ParameterizedTest(name = "세글자 미만 열글자 초과의 이름은 예외가 발생한다.")
    @ValueSource(strings = {"일이", "영일이삼사오륙칠팔구십"})
    void line_name_fail(final String name) {
        assertThatThrownBy(() -> new LineName(name))
            .isInstanceOf(BadRequestException.class)
            .extracting("errorCode")
            .isEqualTo(LINE_NAME_LENGTH);
    }
}
