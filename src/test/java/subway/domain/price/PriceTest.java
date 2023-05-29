package subway.domain.price;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.price.Price;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PriceTest {

    @Test
    void 금액은_0원_이상이다() {
        assertThatThrownBy(() -> Price.from(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("금액은 0원 이상이어야 합니다.");
    }

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
