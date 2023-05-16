package subway.domain.section;

import java.util.ArrayList;
import java.util.LinkedList;
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

    public Sections addSection(final Section section) {
        if (isEmpty()) {
            sections.add(section);
            return new Sections(sections);
        }

        if (isAddableOnFrontOfUpTerminal(section)) {
            final List<Section> updatedSections = new LinkedList<>(sections);
            updatedSections.add(0, section);
            return new Sections(updatedSections);
        }

        if (isAddableOnBackOfDownTerminal(section)) {
            final List<Section> updatedSections = new LinkedList<>(sections);
            updatedSections.add(sections.size(), section);
            return new Sections(updatedSections);
        }

        throw new IllegalArgumentException("기준역을 먼저 구간에 추가 후 시도해 주세요");

    }

    private boolean isAddableOnFrontOfUpTerminal(final Section section) {
        return sections.get(0).isAssemblableOnFront(section);
    }

    private boolean isAddableOnBackOfDownTerminal(final Section section) {
        return sections.get(sections.size() - 1).isAssemblableOnBack(section);
    }

    public boolean isEmpty() {
        return this.sections.isEmpty();
    }

    public List<Section> getSections() {
        return new ArrayList<>(sections);
    }
}
