package subway.station.domain;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.station.domain.exception.StationNameException;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("역 이름은")
class StationNameTest {

    @Test
    void 정상적으로_생성된다() {
        final String input = "강남역";

        assertThatCode(() -> new StationName(input))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest(name = "입력값: {0}")
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void 공백이_아니어야_한다(String input) {
        assertThatCode(() -> new StationName(input))
                .isInstanceOf(StationNameException.class)
                .hasMessage("역 이름이 공백입니다. 글자를 입력해주세요");
    }

    @Test
    void 최대_글자를_초과하면_안된다() {
        String input = "a".repeat(21);

        assertThatCode(() -> new StationName(input))
                .isInstanceOf(StationNameException.class)
                .hasMessage("역 이름이 20글자를 초과했습니다");
    }
}
