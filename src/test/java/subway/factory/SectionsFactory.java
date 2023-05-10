package subway.factory;

import subway.domain.Sections;

import java.util.List;

import static subway.factory.SectionFactory.createSection;
import static subway.factory.SectionFactory.createSection2;

public class SectionsFactory {

    public static Sections createSections() {
        return new Sections(List.of(createSection(), createSection2()));
    }
}
