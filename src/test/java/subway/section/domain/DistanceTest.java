package subway.section.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
class DistanceTest {
    @Test
    void 거리_정상_입력() {
        // expect
        assertThatNoException()
                .isThrownBy(() -> new Distance(1L));
    }
    
    @Test
    void 거리가_null일_경우_예외_처리() {
        // expect
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Distance(null));
    }
    
    @ParameterizedTest(name = "{displayName} : distance = {0}")
    @ValueSource(ints = {-1, 0})
    void 거리가_1미만일_경우_예외_처리(final long distance) {
        // expect
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Distance(distance));
    }
}
