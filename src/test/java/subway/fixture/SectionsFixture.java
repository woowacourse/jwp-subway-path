package subway.fixture;

import subway.domain.subway.Section;
import subway.domain.subway.Sections;

import java.util.ArrayList;
import java.util.List;

import static subway.fixture.SectionFixture.createSection;
import static subway.fixture.SectionFixture.createSection2;

public class SectionsFixture {

	public static Sections createSections() {
		List<Section> sections = new ArrayList<>();
		sections.add(createSection());
		sections.add(createSection2());
		return new Sections(sections);
	}
}
