package subway.domain;

import static org.assertj.core.api.Assertions.*;
import static subway.domain.Fixture.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import subway.domain.line.Section;
import subway.domain.line.Sections;

class SectionsTest {

	@Test
	void emptySectionsTest() {
		final List<Section> expectedSections = Collections.emptyList();

		final Sections sections = new Sections(expectedSections);

		assertThat(sections.getSections()).isEqualTo(expectedSections);
	}

	@Test
	void sortSectionTest() {
		// given
		final List<Section> expectedSections = LINE_NUMBER_2;
		final List<Section> sections = new ArrayList<>(expectedSections);

		Collections.shuffle(sections);

		// when
		final Sections result = new Sections(sections);

		// then
		assertThat(result.getSections()).isEqualTo(expectedSections);
	}

}
