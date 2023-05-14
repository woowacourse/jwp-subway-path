package subway.domain.Sections;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import subway.domain.Fixture;
import subway.domain.Section;

class EmptySectionsTest {

	@Test
	@DisplayName("새로운 구간을 추가 시, 리스트로 감싸서 반환한다")
	void givenNewSection_thenReturnNewSectionList() {
		//given
		final Sections sections = SectionsFactory.createForFind(Collections.emptyList());

		//when
		final EmptySections emptySections = (EmptySections)sections;
		final List<Section> actual = emptySections.addStation(Fixture.NEW_SECTION);

		//then
		assertThat(actual).hasSize(1);
		assertThat(actual.get(0)).isEqualTo(Fixture.NEW_SECTION);
	}
}
