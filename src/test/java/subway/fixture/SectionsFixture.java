package subway.fixture;

import subway.domain.Section;
import subway.domain.Sections;

import java.util.List;

public abstract class SectionsFixture {

    public static Sections 구간_목록(final List<Section> sections) {
        return Sections.from(sections);
    }

    public static Sections 구간_목록() {
        return new Sections();
    }
}
