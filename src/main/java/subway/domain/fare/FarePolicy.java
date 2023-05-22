package subway.domain.fare;

public interface FarePolicy {

    boolean supports(FarePolicyRelatedParameters parameters);

    int calculate(int fare, FarePolicyRelatedParameters parameters);
}
