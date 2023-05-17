package subway.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import subway.exception.InvalidException;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StationTest {
    @Test
    void 역을_생성한다() {
        // expected
        assertDoesNotThrow(() -> new Station(1L, "잠실역"));
    }

    @ParameterizedTest(name = "{displayName}")
    @EmptySource
    void 이름에_공백이_입력되면_예외가_발생한다(String name) {
        // expected
        Assertions.assertThatThrownBy(() -> new Station(1L, name)).isInstanceOf(InvalidException.class);
    }
}
