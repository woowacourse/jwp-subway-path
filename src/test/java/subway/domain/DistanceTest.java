package subway.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import subway.domain.line.Distance;
import subway.error.exception.SectionDistanceException;

class DistanceTest {

	@ParameterizedTest
	@ValueSource(ints = {0, -1, -10})
	@DisplayName("양의 정수가 아닌 값을 입력하면 예외가 발생한다.")
	void validateTest(final int input) {
		assertThatThrownBy(() -> new Distance(input))
			.isInstanceOf(SectionDistanceException.class);
	}

	@Test
	@DisplayName("두 거리의 합인 Distance 객체를 반환한다.")
	void addDistanceTest() {
		//given
		final int baseNumber = 10;
		final int comparisonNumber = 9;

		final Distance distance = new Distance(baseNumber);
		final Distance comparison = new Distance(comparisonNumber);

		//when
		final Distance actual = distance.addDistance(comparison);

		//then
		assertThat(actual.getValue()).isEqualTo(baseNumber + comparisonNumber);
	}

	@Nested
	@DisplayName("두 거리의 차를 구할 때, 거리의 차가")
	class subtractDistance {

		@Test
		@DisplayName("양의 정수면 Distance 객체를 반환한다.")
		void whenSubtractPositive_ThenReturnDistance() {
			//given
			final int baseNumber = 10;
			final int comparisonNumber = 9;

			final Distance distance = new Distance(baseNumber);
			final Distance comparison = new Distance(comparisonNumber);

			//when
			final Distance result = distance.subtractDistance(comparison);

			//then
			assertThat(result.getValue()).isEqualTo(baseNumber - comparisonNumber);
		}

		@ParameterizedTest
		@ValueSource(ints = {10, 11})
		@DisplayName("0이거나 음의 정수이면 예외를 던진한다.")
		void whenSubtractNegativeOrZero_ThrowException(final int input) {
			//given
			final Distance distance = new Distance(10);
			final Distance comparison = new Distance(input);

			//when
			assertThatThrownBy(() -> distance.subtractDistance(comparison))
				.isInstanceOf(SectionDistanceException.class);
		}
	}
}
