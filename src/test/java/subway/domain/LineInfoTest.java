package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.line.InvalidLineNameLengthException;

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
                .isInstanceOf(InvalidLineNameLengthException.class)
                .hasMessage("호선은 이름은 1글자 이상, 10글자 이하여야 한다.");
    }

    @DisplayName("같은 호선끼리 비교 테스트")
    @Test
    void equalsLine() {
        LineInfo 호선1 = new LineInfo("1호선");
        LineInfo 호선2 = new LineInfo("1호선");

        assertThat(호선1).isEqualTo(호선2);
    }

}
