package subway.domain.section;

import java.util.List;

public class ContainingSections {

    private final List<Section> sections;

    public ContainingSections(List<Section> sections) {
        this.sections = sections;
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public boolean isInsertSectionDownEndCase(Section newSection) {
        return sections.size() == 1 &&
                sections.get(0).getDownStation() == newSection.getUpStation();
    }

    public boolean isInsertSectionUpEndCase(Section newSection) {
        return sections.size() == 1 &&
                sections.get(0).getUpStation() == newSection.getDownStation();
    }

    public Section getTargetSection(Section newSection) {
        for (Section currentSection : sections) {
            if (currentSection.getUpStation() == newSection.getUpStation()) {
                return currentSection;
            }
            if (currentSection.getDownStation() == newSection.getDownStation()) {
                return currentSection;
            }
        }
        throw new IllegalArgumentException("현재 등록된 역 중에 하나를 포함해야합니다.");
    }
}
