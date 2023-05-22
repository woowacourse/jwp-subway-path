package subway.domain.subway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.fixture.SectionsFixture.createSections;

class SubwayTest {

	@Test
	@DisplayName("노선의 역을 순서대로 정렬한다")
	void sortStations() {
		// given
		Sections sections = createSections();
		Subway subway = Subway.from(sections);

		// when
		List<Station> orderedStations = subway.getSortedStations(sections);

		// then
		assertAll(
			() -> assertThat(orderedStations.size()).isEqualTo(3),
			() -> assertThat(orderedStations.get(0).getName()).isEqualTo("잠실역"),
			() -> assertThat(orderedStations.get(1).getName()).isEqualTo("잠실새내역"),
			() -> assertThat(orderedStations.get(2).getName()).isEqualTo("선릉역")
		);
	}
}
