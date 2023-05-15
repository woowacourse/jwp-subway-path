package subway.domain.station;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;

@SuppressWarnings("NonAsciiCharacters")
class StationNameTest {

    @ParameterizedTest(name = "역 이름은 공백일 수 없다.")
    @EmptySource
    void 역_이름은_공백일_수_없다(final String input) {
        assertThatThrownBy(() -> new StationName(input))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
