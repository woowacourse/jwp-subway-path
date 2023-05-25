package subway.domain.state;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import subway.domain.Fixture;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.command.Result;
import subway.domain.command.SectionOperation;

class TerminalSectionsTest {

	@Test
	@DisplayName("상행 종점에 새로운 구간 추가 시, 리스트로 감싸서 반환한다")
	void givenAddUpLineTerminalNewSection_thenReturnNewSectionList() {
		//given
		final Section addSection = new Section(2L, Fixture.NEW_DEPARTURE, Fixture.잠실역, Fixture.NEW_DISTANCE);
		final Sections sections = new Sections(Fixture.LINE_NUMBER_2);

		//when
		final Result result = sections.addStation(addSection);
		final List<SectionOperation> actual = result.getSectionOperations();

		//then
		assertThat(actual).hasSize(1);
		assertThat(getSections(actual)).contains(addSection);
	}

	@Test
	@DisplayName("하행 종점에 새로운 구간 추가 시, 리스트로 감싸서 반환한다")
	void givenAddDownLineNewSection_thenReturnNewSectionList() {
		//given
		final Section addSection = new Section(2L, Fixture.선릉역, Fixture.NEW_ARRIVAL, Fixture.NEW_DISTANCE);
		final Sections sections = new Sections(Fixture.LINE_NUMBER_2);

		//when
		final Result result = sections.addStation(addSection);
		final List<SectionOperation> actual = result.getSectionOperations();

		//then
		assertThat(actual).hasSize(1);
		assertThat(getSections(actual)).contains(addSection);
	}

	@Test
	@DisplayName("기존 역 제거 시, 역이 포함된 구간을 리스트로 감싸서 반환한다.")
	void removeStationTest() {
		// given
		final Section removeSection = Fixture.NEW_SECTION;
		final Sections sections = new Sections(List.of(removeSection));

		// when
		final Result result = sections.removeStation();
		final List<SectionOperation> actual = result.getSectionOperations();

		// then
		assertThat(actual).hasSize(1);
		assertThat(getSections(actual)).contains(removeSection);
	}

	private List<Section> getSections(final List<SectionOperation> actual) {
		return actual.stream()
			.map(SectionOperation::getSection)
			.collect(Collectors.toList());
	}
}
