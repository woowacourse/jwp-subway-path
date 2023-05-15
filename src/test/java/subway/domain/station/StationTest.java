package subway.domain.station;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class StationTest {

    @Test
    void 역은_역이름이_없을_시_예외를_발생한다() {
        // expect
        assertThatThrownBy(() -> new Station(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("역의 이름은 필수 입니다.");
    }

    @Test
    void 역은_역이름을_받아_정상생성된다() {
        // given
        final StationName name = new StationName("잠실");

        // expect
        assertDoesNotThrow(() -> new Station(name));
    }
}
