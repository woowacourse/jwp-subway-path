package subway.domain.fare;

import subway.domain.Sections;

public class LineBasedFarePolicy implements FarePolicy {

    @Override
    public boolean supports(FarePolicyRelatedParameters parameters) {
        return true;
    }

    @Override
    public int calculate(int fare, FarePolicyRelatedParameters parameters) {
        Sections sections = parameters.getSections();
        return fare + sections.getMostExpensiveAdditionalCharge();
    }
}
