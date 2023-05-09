package subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.GlobalException;

public class LineInfoTest {

    @ParameterizedTest(name = "호선 이름은 1글자 이상, 10글자 이하여야 한다")
    @ValueSource(strings = {"선", "경의잠실중앙나루호선"})
    void createLineInfoSuccessTest(String name) {
        assertDoesNotThrow(() -> new LineInfo(name));
    }

    @ParameterizedTest(name = "호선 이름은 1글자 이상, 10글자 이하여야 한다")
    @ValueSource(strings = {"", "경의잠실중앙나루호호선"})
    @NullSource
    void createLineInfoFailTestByNameLength(String name) {
        assertThatThrownBy(() -> new LineInfo(name))
                .isInstanceOf(GlobalException.class);
    }

}
