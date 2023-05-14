package subway.domain.Sections;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import subway.domain.Fixture;
import subway.domain.Section;

class TerminalSectionsTest {

	@Test
	@DisplayName("상행 종점에 새로운 구간 추가 시, 리스트로 감싸서 반환한다")
	void givenAddUpLineTerminalNewSection_thenReturnNewSectionList() {
		//given
		final Section addSection = new Section(2L, Fixture.NEW_DEPARTURE, Fixture.잠실역, Fixture.NEW_DISTANCE);
		final StationAddable sections = SectionsFactory.createForAdd(Fixture.LINE_NUMBER_2, addSection);

		//when
		final List<Section> actual = sections.addStation(addSection);

		//then
		assertThat(actual).hasSize(1);
		assertThat(actual.get(0)).isEqualTo(addSection);
	}

	@Test
	@DisplayName("하행 종점에 새로운 구간 추가 시, 리스트로 감싸서 반환한다")
	void givenAddDownLineNewSection_thenReturnNewSectionList() {
		//given
		final Section addSection = new Section(2L, Fixture.선릉역, Fixture.NEW_ARRIVAL, Fixture.NEW_DISTANCE);
		final StationAddable sections = SectionsFactory.createForAdd(Fixture.LINE_NUMBER_2, addSection);

		//when
		final List<Section> actual = sections.addStation(addSection);

		//then
		assertThat(actual).hasSize(1);
		assertThat(actual.get(0)).isEqualTo(addSection);
	}
}
