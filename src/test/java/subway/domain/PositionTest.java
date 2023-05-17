package subway.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.domain.Position.DOWN;
import static subway.domain.Position.MID;
import static subway.domain.Position.NONE;
import static subway.domain.Position.UP;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PositionTest {

    @ParameterizedTest
    @MethodSource("initialPositionData")
    void 같은_값을_매칭하면_같은_값으로_인식하는지_확인한다(final Position position, final Position other) {
        assertThat(other.matches(position)).isTrue();
    }

    private static Stream<Arguments> initialPositionData() {
        return Stream.of(
                Arguments.arguments(UP, UP),
                Arguments.arguments(MID, MID),
                Arguments.arguments(DOWN, DOWN),
                Arguments.arguments(NONE, NONE)
        );
    }
}
