package subway.config.fare;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;
import subway.domain.fare.discount.DiscountComposite;
import subway.domain.fare.discount.PassengerDiscountPolicy;
import subway.domain.fare.expense.DefaultExpensePolicy;
import subway.domain.fare.expense.ExpenseComposite;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class FarePolicyConfig {

    @Bean
    DiscountComposite discountComposite() {
        return new DiscountComposite(new ArrayList<>(List.of(
                passengerDiscountPolicy()
        )));
    }

    @Bean
    PassengerDiscountPolicy passengerDiscountPolicy() {
        return new PassengerDiscountPolicy();
    }

    @Bean
    @RequestScope
    ExpenseComposite expenseComposite() {
        return new ExpenseComposite(new ArrayList<>(List.of(
                defaultExpensePolicy()
        )));
    }

    @Bean
    DefaultExpensePolicy defaultExpensePolicy() {
        return new DefaultExpensePolicy();
    }
}
