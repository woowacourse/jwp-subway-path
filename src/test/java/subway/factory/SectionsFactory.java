package subway.factory;

import subway.domain.subway.Section;
import subway.domain.subway.Sections;

import java.util.ArrayList;
import java.util.List;

import static subway.factory.SectionFactory.createSection;
import static subway.factory.SectionFactory.createSection2;

public class SectionsFactory {

    public static Sections createSections() {
        List<Section> sections = new ArrayList<>();
        sections.add(createSection());
        sections.add(createSection2());
        return new Sections(sections);
    }
}
