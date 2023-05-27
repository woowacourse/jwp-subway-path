package subway.domain.subway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.InvalidSectionDistanceException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static subway.fixture.SectionFixture.createSection;

class SectionTest {

	@Test
	@DisplayName("역이 존재하는지 확인하는 테스트")
	void hasStation() {
		// given
		Section section = createSection();

		// when
		boolean result = section.hasStation(new Station("강남역"));

		// then
		assertThat(result).isFalse();
	}

	@Test
	@DisplayName("요청한 역 사이의 거리가 현재 거리보다 같거나 길면 예외가 발생한다")
	void exception_whenInvalidDistance() {
		// given
		Section section = createSection();
		long requestDistance = 10L;

		// then
		assertThatThrownBy(() -> section.validateSectionDistance(requestDistance))
			.isInstanceOf(InvalidSectionDistanceException.class);
	}
}
