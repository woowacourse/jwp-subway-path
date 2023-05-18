package subway.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import subway.domain.station.Station;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StationTest {

    @ParameterizedTest
    @NullAndEmptySource
    void 이름이_빈_값이면_예외를_발생한다(String input) {
        assertThatThrownBy(() -> new Station(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("역 이름이 입력되지 않았습니다.");
    }
}
