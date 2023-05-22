package subway.domain.farePolicy;

import subway.application.FarePolicy;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.general.Money;
import subway.dto.PassengerDto;

import java.util.List;

public class FarePolicyChain {

    FarePolicy[] farePolicies;
    int pos = 0;
    int policyCount;

    public FarePolicyChain(FarePolicy[] farePolicies) {
        this.farePolicies = farePolicies;
        this.policyCount = farePolicies.length;
    }

    public Money applyPolicy(Money money, List<Sections> allSections, List<Section> path, PassengerDto passengerDto) {
        Money fare = money;
        if (pos < policyCount) {
            fare = farePolicies[pos++].applyPolicies(fare, allSections, path, passengerDto, this);
        }
        return fare;
    }
}
