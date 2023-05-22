package subway.application.discount;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.domain.discount.Age;
import subway.domain.price.Price;

class AgeDiscountServiceTest {

    AgeDiscountService ageDiscountService;

    @BeforeEach
    void setUp() {
        ageDiscountService = new AgeDiscountService(List.of(
                new KidsDiscountPolicy(),
                new TeenagersDiscountPolicy()
        ));
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 12})
    @DisplayName("나이가 6살 이상 13살 미만이면 어린이 할인 요금이 적용되어야 한다.")
    void discount_kids(int input) {
        // given
        Price price = Price.from(1000);
        Age age = new Age(input);

        // when
        Price discountedPrice = ageDiscountService.discount(price, age);

        // then
        assertThat(discountedPrice.getAmount())
                .isEqualTo(675);
    }

    @ParameterizedTest
    @ValueSource(ints = {13, 18})
    @DisplayName("나이가 13살 이상 19살 미만이면 청소년 할인 요금이 적용되어야 한다.")
    void discount_teenagers(int input) {
        // given
        Price price = Price.from(1000);
        Age age = new Age(input);

        // when
        Price discountedPrice = ageDiscountService.discount(price, age);

        // then
        assertThat(discountedPrice.getAmount())
                .isEqualTo(870);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 19})
    @DisplayName("나이가 할인 정책에 포함되지 않으면 그대로 반환해야 한다.")
    void discount_notInCondition(int input) {
        // given
        Price price = Price.from(1000);
        Age age = new Age(input);

        // when
        Price discountedPrice = ageDiscountService.discount(price, age);

        // then
        assertThat(discountedPrice)
                .isEqualTo(price);
    }
}
