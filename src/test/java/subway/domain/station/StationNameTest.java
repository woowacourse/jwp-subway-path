package subway.domain.station;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class StationNameTest {

    @Test
    void 역_이름은_10자_초과시_예외를_발생한다() {
        //given
        final String name = "역".repeat(11);

        //expect
        assertThatThrownBy(() -> new StationName(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("역 이름은 10글자 보다 작아야합니다.");
    }

    @Test
    void 역_이름은_공백일_시_예외를_발생한다() {
        //given
        final String name = " ";

        //expect
        assertThatThrownBy(() -> new StationName(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("역 이름은 공백일 수 없습니다.");
    }

    @Test
    void 역_이름은_공백이_아니고_10자_이하라면_정상_생성된다() {
        //given
        final String name = "잠실역";

        //expect
        assertDoesNotThrow(() -> new StationName(name));
    }
}
