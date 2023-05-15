package subway.domain.section;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class DistanceTest {

    @Test
    void 거리는_1_미만일_시_예외를_발생한다() {
        // given
        int distance = 0;

        //expect
        assertThatThrownBy(() -> new Distance(distance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("길이는 1 이상이어야합니다.");
    }

    @Test
    void 거리는_1_이상일_시_정상_생성된다() {
        // given
        int distance = 1;

        //expect
        Assertions.assertDoesNotThrow(() -> new Distance(distance));
    }
}
