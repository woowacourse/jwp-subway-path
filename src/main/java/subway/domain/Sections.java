package subway.domain;

import java.util.List;
import subway.domain.exception.BusinessException;

public class Sections {

    private final List<Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public void sort() {
        if (sections.isEmpty()) {
            return;
        }
        makeTop();
        tailBite(0);
    }

    private void makeTop() {
        for (final Section section : sections) {
            if (isTopSection(section)) {
                changeIndex(section, 0);
                break;
            }
        }
    }

    private void changeIndex(final Section section, int targetIndex) {
        final int currentIndex = sections.indexOf(section);
        if (currentIndex >= 0 && currentIndex != targetIndex) {
            sections.remove(currentIndex);
            if (targetIndex > currentIndex) {
                targetIndex--;
            }
            sections.add(targetIndex, section);
        }
    }

    private boolean isTopSection(final Section section) {
        final Station upStation = section.getUpStation();
        return sections.stream()
            .noneMatch(otherSection -> otherSection.getDownStation().equals(upStation));
    }

    private void tailBite(int index) {
        final Section current = sections.get(index);
        if (isBottomSection(current)) {
            return;
        }
        final Station currentDownStation = current.getDownStation();
        for (final Section section : sections) {
            if (section.getUpStation().equals(currentDownStation)) {
                changeIndex(section, ++index);
                tailBite(index);
            }
        }
    }

    private boolean isBottomSection(final Section section) {
        final Station downStation = section.getDownStation();
        return sections.stream()
            .noneMatch(otherSection -> otherSection.getUpStation().equals(downStation));
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public void add(final Section section) {
        sections.add(section);
    }

    public Section getSection(final int index) {
        return sections.get(index);
    }

    public Section getFirstSection() {
        if (isEmpty()) {
            throw new BusinessException("빈 구간 목록입니다.");
        }
        return sections.get(0);
    }
}
