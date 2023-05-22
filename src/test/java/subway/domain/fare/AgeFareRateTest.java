package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.common.exception.SubwayIllegalArgumentException;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AgeFareRateTest {

    @Test
    void _19세_이상이면_성인이다() {
        // when
        AgeFareRate ageFareRate = AgeFareRate.from(19);

        // then
        assertThat(ageFareRate).isEqualTo(AgeFareRate.ADULT);
    }

    @ParameterizedTest
    @ValueSource(ints = {13, 18})
    void _13세_이상_18세_이하이면_청소년이다(int age) {
        // when
        AgeFareRate ageFareRate = AgeFareRate.from(age);

        // then
        assertThat(ageFareRate).isEqualTo(AgeFareRate.TEENAGER);
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 12})
    void _6세_이상_12세_이하이면_어린이다(int age) {
        // when
        AgeFareRate ageFareRate = AgeFareRate.from(age);

        // then
        assertThat(ageFareRate).isEqualTo(AgeFareRate.CHILD);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 5})
    void _0세_이상_5세_이하이면_영유아다(int age) {
        // when
        AgeFareRate ageFareRate = AgeFareRate.from(age);

        // then
        assertThat(ageFareRate).isEqualTo(AgeFareRate.INFANT);
    }

    @Test
    void _0세보다_작으면_예외() {
        // when them
        assertThatThrownBy(() -> AgeFareRate.from(-1))
                .isInstanceOf(SubwayIllegalArgumentException.class)
                .hasMessage("요금 책정이 불가한 나이입니다.");
    }

    @Test
    void null이_들어오면_성인() {
        // when
        AgeFareRate ageFareRate = AgeFareRate.from(null);

        // then
        assertThat(ageFareRate).isEqualTo(AgeFareRate.ADULT);
    }
}
