package subway.domain.line.state;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import subway.domain.Fixture;
import subway.domain.line.Section;
import subway.domain.line.Sections;
import subway.domain.line.Station;
import subway.domain.line.command.Result;
import subway.domain.line.command.SectionOperation;
import subway.error.exception.StationNotFoundException;

class EmptySectionsTest {

	@Test
	@DisplayName("새로운 역 추가 시, 리스트로 감싸서 반환한다")
	void addStationTest() {
		// given
		final Section addSection = Fixture.NEW_SECTION;
		final Sections sections = new Sections(Collections.emptyList());

		//when
		final Result result = sections.addStation(addSection);
		final List<SectionOperation> actual = result.getSectionOperations();

		// then
		assertThat(actual).hasSize(1);
		assertThat(actual.get(0).getSection()).isEqualTo(addSection);
	}

	@Test
	@DisplayName("기존 역 제거 시, 역이 포함된 구간이 없어서 예외가 발생한다.")
	void removeStationTest() {
		// given
		final Sections sections = new Sections(Collections.emptyList());
		final Station 선릉역 = Fixture.선릉역;

		// when & then
		assertThatThrownBy(() -> sections.removeStation(선릉역))
			.isInstanceOf(StationNotFoundException.class);
	}

}
