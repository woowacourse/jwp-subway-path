package subway.service.domain;

import java.util.List;

public class FarePolicies {

    private final List<FarePolicy> additionalFarePolicy;

    public FarePolicies(List<FarePolicy> additionalFarePolicy) {
        this.additionalFarePolicy = additionalFarePolicy;
    }

    public Integer getAdditionalFareByLineProperty(LineProperty lineProperty) {
        return additionalFarePolicy.stream()
                .filter(farePolicy -> farePolicy.getLineProperty().equals(lineProperty))
                .map(FarePolicy::getAdditionalFare)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당하는 노선의 추가 요금 정책이 없습니다."));
    }

    public List<FarePolicy> getAdditionalFarePolicy() {
        return additionalFarePolicy;
    }

    @Override
    public String toString() {
        return "FarePolicies{" +
                "additionalFarePolicy=" + additionalFarePolicy +
                '}';
    }

}
