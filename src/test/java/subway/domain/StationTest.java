package subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StationTest {

    @NullAndEmptySource
    @ParameterizedTest
    void 이름에는_공백이나_null이_들어올_수_없다(String value) {
        assertThatThrownBy(() -> new Station(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("아름에는 빈 문자가 들어올 수 없습니다.");
    }
}
