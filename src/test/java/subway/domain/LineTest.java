package subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LineTest {

    @ParameterizedTest(name = "세글자 이상 열글자 초과의 이름은 정상 생성된다")
    @ValueSource(strings = {"일이삼", "일이삼사오륙칠팔구십"})
    void 세글자부터_열글자_사이의_이름은_정상_생성된다(final String name) {
        assertDoesNotThrow(() -> new Line(name, "초록색", new ArrayList<>()));
    }

    @ParameterizedTest(name = "세글자 미만 열글자 초과의 이름은 예외가 발생한다.")
    @ValueSource(strings = {"일이", "영일이삼사오륙칠팔구십"})
    void 세글자_미만_열글자_초과의_이름은_예외가_발생한다(final String name) {
        assertThatThrownBy(() -> new Line(name, "초록색", new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("노선 이름은 3~10자 사이여야 합니다");
    }
}
