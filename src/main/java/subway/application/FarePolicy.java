package subway.application;

import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.farePolicy.FarePolicyChain;
import subway.domain.general.Money;
import subway.dto.PassengerDto;

import java.util.List;

public interface FarePolicy {
    Money applyPolicies(Money money, List<Sections> allSections, List<Section> path, PassengerDto passengerDto, FarePolicyChain policyChain);

}
