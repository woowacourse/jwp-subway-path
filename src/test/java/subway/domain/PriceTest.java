package subway.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PriceTest {

    @Test
    void 요금에_더하기가_되는지_확인한다() {
        // given
        final Price price1 = Price.from(10);
        final Price price2 = Price.from(1);

        // when
        final Price actual = price1.plus(price2);

        // then
        assertThat(actual.getPrice()).isEqualTo(11);
    }
}
