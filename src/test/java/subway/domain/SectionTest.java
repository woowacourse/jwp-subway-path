package subway.domain;

import static org.assertj.core.api.Assertions.*;
import static subway.domain.Fixture.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import subway.domain.line.Distance;
import subway.domain.line.Section;
import subway.error.exception.SectionDistanceException;

public class SectionTest {

	@Nested
	class IsTerminalConnectedTest {

		@Test
		@DisplayName("기준 구간의 출발역이 추가 구간의 도착역과 같으면 참을 반환한다")
		void givenSectionDepartureEqualsToArrival_thenReturnTrue() {
			// given
			final Section addUpLineTerminalSection = new Section(5L, NEW_DEPARTURE, DEPARTURE, NEW_DISTANCE);

			// when
			final boolean actual = addUpLineTerminalSection.isTerminalConnected(UP_LINE_TERMINAL,
				DOWN_LINE_TERMINAL);

			// then
			assertThat(actual).isTrue();
		}

		@Test
		@DisplayName("기준 구간의 도착역이 추가 구간의 출발역과 같으면 참을 반환한다")
		void givenSectionArrivalEqualsToDeparture_thenReturnTrue() {
			// given
			final Section addDownLineTerminalSection = new Section(6L, ARRIVAL, NEW_ARRIVAL, NEW_DISTANCE);

			//when
			final boolean actual = addDownLineTerminalSection.isTerminalConnected(UP_LINE_TERMINAL,
				DOWN_LINE_TERMINAL);

			//then
			assertThat(actual).isTrue();
		}

		@Test
		@DisplayName("기준 구간이 연결되지 않으면 거짓을 반환한다.")
		void givenSectionNotConnected_thenReturnFalse() {
			//given
			final Section notAddableSection = new Section(7L, NEW_DEPARTURE, NEW_ARRIVAL, DISTANCE);

			//when
			final boolean actual = notAddableSection.isTerminalConnected(UP_LINE_TERMINAL, DOWN_LINE_TERMINAL);

			//then
			assertThat(actual).isFalse();
		}

	}

	@Nested
	class IsCrossConnectedTest {
		@Test
		@DisplayName("상행 종점의 출발역이 추가 구간의 도착역과 같고, 하행 종점의 도착역이 추가 구간의 출발역과 같으면 참을 반환한다")
		void givenCrossConnectedSection_thenReturnTrue() {
			// given
			final Section crossConnectSection = new Section(8L, ARRIVAL, DEPARTURE, NEW_DISTANCE);

			// when
			final boolean actual = crossConnectSection.isCrossConnected(UP_LINE_TERMINAL, DOWN_LINE_TERMINAL);

			// then
			assertThat(actual).isTrue();
		}
	}

	@Nested
	class IsConnectedTest {

		@Test
		@DisplayName("출발역이 같으면, 참을 반환한다.")
		void givenSameDeparture_thenReturnTrue() {
			// given
			final Section sameDepartureSection = new Section(9L, UP_LINE_MIDDLE_STATION, NEW_ARRIVAL, NEW_DISTANCE);

			// when
			final boolean actual = sameDepartureSection.isConnected(MIDDLE);

			// then
			assertThat(actual).isTrue();
		}

		@Test
		@DisplayName("도착역이 같으면, 참을 반환한다.")
		void givenSameArrival_thenReturnTrue() {
			// given
			final Section sameArrivalSection = new Section(10L, NEW_DEPARTURE, DOWN_LINE_MIDDLE_STATION, NEW_DISTANCE);

			// when
			final boolean actual = sameArrivalSection.isConnected(MIDDLE);

			// then
			assertThat(actual).isTrue();
		}

		@Test
		@DisplayName("출발역과 도착역 모두 다르면, 거짓을 반환한다.")
		void givenDifferentDepartureAndArrival_thenReturnFalse() {
			//when
			final boolean actual = NEW_SECTION.isConnected(MIDDLE);

			//then
			assertThat(actual).isFalse();
		}
	}

	@Nested
	class IsSameDepartureTest {

		@Test
		@DisplayName("같은 출발역을 입력하면 참을 반환한다.")
		void givenSameDeparture_thenReturnTrue() {
			// given
			final Section sameDepartureSection = new Section(9L, UP_LINE_MIDDLE_STATION, NEW_ARRIVAL, NEW_DISTANCE);

			// when
			final boolean actual = sameDepartureSection.isSameDeparture(
				MIDDLE);

			// then
			assertThat(actual).isTrue();
		}

		@Test
		@DisplayName("다른 출발역을 입력하면 거짓을 반환한다.")
		void givenDifferentDeparture_thenReturnFalse() {
			// given
			final Section sameArrivalSection = new Section(10L, NEW_DEPARTURE, DOWN_LINE_MIDDLE_STATION, NEW_DISTANCE);

			// when
			final boolean actual = sameArrivalSection.isSameDeparture(MIDDLE);

			// then
			assertThat(actual).isFalse();
		}

	}

	@Test
	@DisplayName("두 거리의 합인 Distance 객체를 반환한다")
	void addDistanceTest() {
		// given
		final Distance shorterDistance = new Distance(5);
		final Section comparison = new Section(2L, DEPARTURE, ARRIVAL, shorterDistance);

		// when
		final Distance actual = NEW_SECTION.addDistance(comparison);

		//then
		assertThat(actual).isEqualTo(new Distance(15));
	}

	@Nested
	@DisplayName("두 거리의 차를 구할 때, 거리의 차가")
	class SubtractDistanceTest {

		@Test
		@DisplayName("양의 정수면 Distance 객체를 반환한다.")
		void whenSubtractPositive_ThenReturnDistance() {
			// given
			final Distance shorterDistance = new Distance(5);
			final Section comparison = new Section(2L, DEPARTURE, ARRIVAL, shorterDistance);

			// when
			final Distance actual = NEW_SECTION.subtractDistance(comparison);

			// then
			assertThat(actual).isEqualTo(new Distance(5));
		}

		@ParameterizedTest
		@ValueSource(ints = {10, 11})
		@DisplayName("0이거나 음의 정수이면 예외를 던진한다.")
		void whenSubtractNegativeOrZero_ThrowException(final int input) {
			// given
			final Distance longerDistance = new Distance(input);
			final Section comparison = new Section(3L, DEPARTURE, ARRIVAL, longerDistance);

			// when & then
			assertThatThrownBy(() -> NEW_SECTION.subtractDistance(comparison))
				.isInstanceOf(SectionDistanceException.class);
		}
	}

}
