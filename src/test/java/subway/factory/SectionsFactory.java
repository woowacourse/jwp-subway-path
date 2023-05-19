package subway.factory;

import static subway.factory.SectionFactory.createSection;
import static subway.factory.SectionFactory.createSection2;

import java.util.List;
import subway.domain.Sections;

public class SectionsFactory {

    public static Sections createSections() {
        return new Sections(List.of(createSection(), createSection2()));
    }
}
