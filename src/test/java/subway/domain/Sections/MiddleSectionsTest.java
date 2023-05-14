package subway.domain.Sections;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import subway.domain.Fixture;
import subway.domain.Section;

class MiddleSectionsTest {

	@Test
	@DisplayName("상행 경유에 새로운 구간 추가 시, 리스트로 감싸서 반환한다")
	void givenAddUpLineMiddleNewSection_thenReturnNewSectionList() {
		//given
		final Section addSection = new Section(2L, Fixture.잠실역, Fixture.NEW_ARRIVAL, Fixture.NEW_DISTANCE);
		final Sections sections = SectionsFactory.create(Fixture.LINE_NUMBER_2, addSection);

		//when
		final MiddleSections middleSections = (MiddleSections)sections;
		final List<Section> actual = middleSections.addStation(addSection);

		//then
		assertThat(actual).hasSize(3);
		assertThat(actual.get(0)).isEqualTo(addSection);
	}

	@Test
	@DisplayName("하행 경유에 새로운 구간 추가 시, 리스트로 감싸서 반환한다")
	void givenAddDownLineMiddleNewSection_thenReturnNewSectionList() {
		//given
		final Section addSection = new Section(2L, Fixture.NEW_DEPARTURE, Fixture.선릉역, Fixture.NEW_DISTANCE);
		final Sections sections = SectionsFactory.create(Fixture.LINE_NUMBER_2, addSection);

		//when
		final MiddleSections middleSections = (MiddleSections)sections;
		final List<Section> actual = middleSections.addStation(addSection);

		//then
		assertThat(actual).hasSize(3);
		assertThat(actual.get(1)).isEqualTo(addSection);
	}
}
