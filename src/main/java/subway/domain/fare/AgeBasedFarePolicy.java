package subway.domain.fare;

import java.util.List;

public class AgeBasedFarePolicy implements FarePolicy {

    private final static AgeRange CHILD_AGE = new AgeRange(6, 12, 50);
    private final static AgeRange TEEN_AGE = new AgeRange(13, 18, 20);
    private final static List<AgeRange> TARGET_RANGES = List.of(CHILD_AGE, TEEN_AGE);

    @Override
    public boolean supports(FarePolicyRelatedParameters parameters) {
        return parameters.getOptionalAge().isPresent();
    }

    @Override
    public int calculate(int fare, FarePolicyRelatedParameters parameters) {
        int age = getAge(parameters);
        for (AgeRange ageRange : TARGET_RANGES) {
            if (ageRange.isInRange(age)) {
                fare -= ageRange.getDiscountAmount(fare);
            }
        }
        return fare;
    }

    private int getAge(FarePolicyRelatedParameters parameters) {
        return parameters.getOptionalAge()
            .orElseThrow(IllegalStateException::new);
    }
}
