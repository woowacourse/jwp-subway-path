package subway.domain.Sections;

import static org.assertj.core.api.Assertions.*;
import static subway.domain.Fixture.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import subway.domain.Section;

class SectionsFactoryTest {

	@Nested
	@DisplayName("입력된 List<Section>의 사이즈가 ")
	class CreateTest {

		@Test
		@DisplayName("0이면, EmptySections 객체를 반환한다")
		void givenEmptyList_thenReturnEmptySections() {
			//given
			final List<Section> givenSections = Collections.emptyList();

			//when
			final Sections result = SectionsFactory.create(givenSections);

			//then
			assertThat(result instanceof EmptySections).isTrue();
			assertThat(result.getSections()).hasSize(0);
		}

		@Test
		@DisplayName("0이 아니면, FilledSections 객체를 반환한다")
		void givenFilledList_thenReturnFilledSections() {
			//given
			final List<Section> givenSections = LINE_NUMBER_2;

			//when
			final Sections result = SectionsFactory.create(givenSections);

			//then
			assertThat(result instanceof FilledSections).isTrue();
			assertThat(result.getSections()).hasSize(givenSections.size());
		}

	}

}
