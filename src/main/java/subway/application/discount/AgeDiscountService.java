package subway.application.discount;

import java.util.List;
import org.springframework.stereotype.Service;
import subway.domain.discount.Age;
import subway.domain.price.Price;

@Service
public class AgeDiscountService {
    private final List<AgeDiscountPolicy> ageDiscountPolicies;

    public AgeDiscountService(List<AgeDiscountPolicy> ageDiscountPolicies) {
        this.ageDiscountPolicies = ageDiscountPolicies;
    }

    public Price discount(Price price, Age age) {
        for (AgeDiscountPolicy ageDiscountPolicy : ageDiscountPolicies) {
            price = price.minus(ageDiscountPolicy.getDiscountPrice(price, age));
        }
        return price;
    }
}
