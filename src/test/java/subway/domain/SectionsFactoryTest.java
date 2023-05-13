package subway.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

class SectionsFactoryTest {

	@Test
	void sortingTest() {
		// given
		final List<Section> sections = createSections();
		final List<Section> expectedSections = new ArrayList<>(sections);

		Collections.shuffle(sections);

		// when
		final Sections result = SectionsFactory.create(sections);

		// then
		assertThat(result.getSections()).isEqualTo(expectedSections);
	}

	private List<Section> createSections() {
		final List<String> stationNames = List.of("잠실역", "잠실새내역", "종합운동장역", "삼성역", "선릉역");

		return IntStream.range(0, stationNames.size() - 1)
			.mapToObj(i -> new Section(1L,
				new Station(1L, stationNames.get(i)),
				new Station(1L, stationNames.get(i + 1)),
				new Distance(1)))
			.collect(Collectors.toList());
	}
}
