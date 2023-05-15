package subway.domain.section;

import java.util.ArrayList;
import java.util.List;

public class Sections {

    private final List<Section> sections;

    public Sections(final List<Section> sections) {
        validateSections(sections);
        this.sections = sections;
    }

    public static Sections emptySections() {
        return new Sections(new ArrayList<>());
    }

    private void validateSections(final List<Section> sections) {
        if (sections == null) {
            throw new IllegalArgumentException("노선에 구간들은 없을 수 없습니다.");
        }
    }

    public List<Section> getSections() {
        return new ArrayList<>(sections);
    }
}
