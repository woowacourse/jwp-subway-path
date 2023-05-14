package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DirectionTest {

    @CsvSource({"LEFT, RIGHT", "RIGHT, LEFT"})
    @ParameterizedTest(name = "{0}의 반대 방향은 {1}이다.")
    void 반대_방향을_반환한다(final Direction sut, final Direction result) {
        // expect
        assertThat(sut.flip()).isEqualTo(result);
    }
}
