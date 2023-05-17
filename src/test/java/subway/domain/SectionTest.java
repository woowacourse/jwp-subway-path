package subway.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionTest {

    @ParameterizedTest
    @ValueSource(longs = {-1, 0})
    void 거리_정보는_양의_정수로_제한한다(Long distance) {
        ///when, then
        assertThatThrownBy(
                () -> new Section(new Station("upStation"), new Station("downStation"), distance)
        ).isInstanceOf(IllegalArgumentException.class);
    }

}