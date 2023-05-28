package subway.domain.fare;

import java.util.List;
import org.springframework.stereotype.Component;
import subway.domain.route.Path;

@Component
public class FarePolicies {
    private final BaseFarePolicy baseFarePolicy;
    private final List<ExtraFarePolicy> extraFarePolicies;

    public FarePolicies(final BaseFarePolicy baseFarePolicy, final List<ExtraFarePolicy> extraFarePolicies) {
        this.baseFarePolicy = baseFarePolicy;
        this.extraFarePolicies = extraFarePolicies;
    }

    public Fare calculateFare(final Path path) {
        Fare baseFare = baseFarePolicy.getBaseFare();
        int extraFare = extraFarePolicies.stream()
                .mapToInt(extraFarePolicy -> extraFarePolicy.calculateExtraFare(path).getValue())
                .sum();

        return baseFare.add(new Fare(extraFare));
    }
}
