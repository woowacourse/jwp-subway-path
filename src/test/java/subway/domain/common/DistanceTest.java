package subway.domain.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import subway.exception.DistanceLessThatOneException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class DistanceTest {

	@ParameterizedTest
	@ValueSource(longs = {-1, 0})
	@DisplayName("거리가 1보다 작으면 예외를 발생시킨다")
	void exception_whenDistanceInvalid(final long givenDistance) {
		// then
		assertThatThrownBy(() -> new Distance(givenDistance))
			.isInstanceOf(DistanceLessThatOneException.class);
	}

	@Test
	@DisplayName("거리가 생성 테스트")
	void createDistance() {
		// given
		long requestDistance = 3;

		// when
		Distance distance = new Distance(requestDistance);

		// then
		assertThat(distance.getDistance()).isEqualTo(requestDistance);
	}

	@Test
	@DisplayName("새 요청거리가 현재 거리보다 큰면 true를 반환한다")
	void check_input_is_longer_than_now_distance() {
		// given
		long originDistance = 3;
		long newDistance = 5;
		Distance distance = new Distance(originDistance);

		// when
		boolean result = distance.isSameOrUnder(newDistance);

		// then
		assertThat(result).isTrue();
	}
}
