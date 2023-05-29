package subway.domain.fare;

import java.util.List;

import subway.domain.fare.distance.DistanceBasedFarePolicy;
import subway.domain.line.Distance;

public class Fare {

	private final List<FarePolicy> farePolicies;

	public Fare(final Distance distance) {
		this.farePolicies = List.of(new DistanceBasedFarePolicy(distance));
	}

	public int calculate() {

		return farePolicies.stream()
			.reduce(1250, (currentFare, farePolicy) -> farePolicy.calculate(currentFare), (fare1, fare2) -> fare2);

	}

}
