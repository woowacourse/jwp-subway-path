package subway.domain.Sections;

import static org.assertj.core.api.Assertions.*;
import static subway.domain.Fixture.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import subway.domain.Section;

class FilledSectionsTest {

	@Test
	void sortingTest() {
		// given
		final List<Section> expectedSections = LINE_NUMBER_2;
		final List<Section> sections = new ArrayList<>(expectedSections);

		Collections.shuffle(sections);

		// when
		final Sections result = SectionsFactory.create(sections);

		// then
		assertThat(result instanceof FilledSections).isTrue();
		assertThat(result.getSections()).isEqualTo(expectedSections);
	}

}
