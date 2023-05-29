package subway.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import subway.domain.fare.Fare;
import subway.domain.line.Distance;

class FareTest {

	@Test
	@DisplayName("10km 미만일때는 기본 요금이 부과된다.")
	void calculateDefaultDistance() {
		// given
		final Distance distance = new Distance(9);

		// when
		final Fare fare = new Fare(distance);
		final int actual = fare.calculate();

		// then
		assertThat(actual).isEqualTo(1250);
	}

	@ParameterizedTest
	@CsvSource(value = {"12,1350", "16,1450"})
	@DisplayName("10km 이상, 50km 이하일때는 5km 마다 추가 요금이 부과된다.")
	void calculateFirstDistanceLimit(int distanceValue, int feeValue) {
		// given
		final Distance distance = new Distance(distanceValue);

		// when
		final Fare fare = new Fare(distance);
		final int actual = fare.calculate();

		// then
		assertThat(actual).isEqualTo(feeValue);
	}

	@Test
	@DisplayName("50km 초과일때는 8km 마다 추가 요금이 부과된다.")
	void calculateSecondDistanceLimit() {
		// given
		final Distance distance = new Distance(58);

		// when
		final Fare fare = new Fare(distance);
		final int actual = fare.calculate();

		// then
		assertThat(actual).isEqualTo(2150);
	}
}
