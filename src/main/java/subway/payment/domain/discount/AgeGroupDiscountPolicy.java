package subway.payment.domain.discount;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import subway.path.domain.AgeGroup;
import subway.path.domain.DiscountPolicy;
import subway.payment.exception.PaymentException;

@Component
public class AgeGroupDiscountPolicy implements DiscountPolicy {

    private final Map<AgeGroup, DiscountInfo> discountByAgeGroupMapping = new LinkedHashMap<>();

    public AgeGroupDiscountPolicy() {
        discountByAgeGroupMapping.put(AgeGroup.TODDLER, new DiscountInfo(0, 100));
        discountByAgeGroupMapping.put(AgeGroup.KIDS, new DiscountInfo(350, 50));
        discountByAgeGroupMapping.put(AgeGroup.TEENAGERS, new DiscountInfo(350, 20));
        discountByAgeGroupMapping.put(AgeGroup.ADULTS, new DiscountInfo(0, 0));
        discountByAgeGroupMapping.put(AgeGroup.SENIOR, new DiscountInfo(0, 100));
    }

    @Override
    public DiscountResult discount(final int fee) {
        final DiscountResult discountResult = new DiscountResult();
        for (final AgeGroup ageGroup : discountByAgeGroupMapping.keySet()) {
            discountResult.add(
                    ageGroup.info(),
                    discountedFee(fee, discountByAgeGroupMapping.get(ageGroup))
            );
        }
        return discountResult;
    }

    private int discountedFee(final int fee, final DiscountInfo discountInfo) {
        final double discountedFee = (fee - discountInfo.takeOffAmount) * discountPercent(discountInfo);
        validateFeeIsNonNegative(discountedFee);
        return (int) discountedFee;
    }

    private double discountPercent(final DiscountInfo discountInfo) {
        return (100.0 - discountInfo.discountPercent) / 100;
    }

    private void validateFeeIsNonNegative(final double discountedFee) {
        if (discountedFee < 0) {
            throw new PaymentException("요금 계산 결과가 음수입니다");
        }
    }

    public static class DiscountInfo {
        private final int takeOffAmount;
        private final int discountPercent;

        public DiscountInfo(final int takeOffAmount, final int discountPercent) {
            this.takeOffAmount = takeOffAmount;
            this.discountPercent = discountPercent;
        }
    }
}
