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

class MiddleSectionsTest {

	@Test
	@DisplayName("상행 경유에 새로운 구간 추가 시, 리스트로 감싸서 반환한다")
	void givenAddUpLineMiddleNewSection_thenReturnNewSectionList() {
		//given
		final Section addSection = new Section(2L, Fixture.잠실역, Fixture.NEW_ARRIVAL, Fixture.NEW_DISTANCE);
		final Sections sections = new Sections(Fixture.LINE_NUMBER_2);

		//when
		final Result result = sections.addStation(addSection);
		final List<SectionOperation> actual = result.getSectionOperations();

		//then
		assertThat(actual).hasSize(3);
		assertThat(getSections(actual)).contains(addSection);
	}

	@Test
	@DisplayName("하행 경유에 새로운 구간 추가 시, 리스트로 감싸서 반환한다")
	void givenAddDownLineMiddleNewSection_thenReturnNewSectionList() {
		//given
		final Section addSection = new Section(2L, Fixture.NEW_DEPARTURE, Fixture.선릉역, Fixture.NEW_DISTANCE);
		final Sections sections = new Sections(Fixture.LINE_NUMBER_2);

		//when
		final Result result = sections.addStation(addSection);
		final List<SectionOperation> actual = result.getSectionOperations();

		//then
		assertThat(actual).hasSize(3);
		assertThat(getSections(actual)).contains(addSection);
	}

	@Test
	@DisplayName("기존 역 제거 시, 제거할 구간 2개와 추가할 구간 1개가 포함된 리스트를 반환한다")
	void removeStationTest() {
		// given
		final List<Section> 상행_2호선 = List.of(Fixture.상행_경유_2호선, Fixture.상행_종점_2호선);

		final Sections sections = new Sections(상행_2호선);

		// when
		final Result result = sections.removeStation();
		final List<SectionOperation> actual = result.getSectionOperations();

		// then
		assertThat(actual).hasSize(3);
		assertThat(getSections(actual)).contains(Fixture.상행_경유_2호선, Fixture.상행_종점_2호선);
	}

	private List<Section> getSections(final List<SectionOperation> actual) {
		return actual.stream()
			.map(SectionOperation::getSection)
			.collect(Collectors.toList());
	}
}
