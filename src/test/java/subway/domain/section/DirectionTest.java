package subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "SpellCheckingInspection"})
class DirectionTest {

    @ParameterizedTest(name = "{0}일 때 {1}을 반환한다.")
    @CsvSource(value = {"UP:DOWN", "DOWN:UP"}, delimiter = ':')
    void reverse_메소드는_호출하면_자신과_반대의_값을_반환한다(final Direction direction, final Direction expected) {
        final Direction actual = direction.reverse();

        assertThat(actual).isSameAs(expected);
    }

    @ParameterizedTest(name = "{0}일 때 {1}을 전달하면 {2}을 반환한다.")
    @CsvSource(value = {"UP:DOWN:false", "DOWN:DOWN:true"}, delimiter = ':')
    void matches_메소드는_방향을_전달하면_동일한_방향인지_여부를_반환한다(
            final Direction sourceDirection,
            final Direction targetDirection,
            final boolean expected) {
        final boolean actual = sourceDirection.matches(targetDirection);

        assertThat(actual).isSameAs(expected);
    }
}
