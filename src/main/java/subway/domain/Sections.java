package subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import subway.domain.exception.BusinessException;

public class Sections {

    private List<Section> value;

    public Sections(final List<Section> value) {
        this.value = value;
        sort();
    }

    public void sort() {
        if (value.isEmpty()) {
            return;
        }
        final Section topSection = findTopSection();
        final Section bottomSection = findBottomSection();
        Section currentSection = topSection;
        final List<Section> newValue = new ArrayList<>();
        newValue.add(topSection);
        while (!currentSection.equals(bottomSection)) {
            final Section nextSection = findNextSection(currentSection);
            newValue.add(nextSection);
            currentSection = nextSection;
        }
        value = newValue;
    }

    private Section findTopSection() {
        for (final Section section : value) {
            if (isTopSection(section)) {
                return section;
            }
        }
        throw new BusinessException("존재하지 않는 섹션입니다.");
    }

    private Section findBottomSection() {
        for (final Section section : value) {
            if (isBottomSection(section)) {
                return section;
            }
        }
        throw new BusinessException("존재하지 않는 섹션입니다.");
    }

    private Section findNextSection(final Section currentSection) {
        for (final Section section : value) {
            if (currentSection.getDownStation().equals(section.getUpStation())) {
                return section;
            }
        }
        throw new BusinessException("존재하지 않는 섹션입니다.");
    }

    private boolean isTopSection(final Section section) {
        final Station upStation = section.getUpStation();
        return value.stream()
            .noneMatch(otherSection -> otherSection.getDownStation().equals(upStation));
    }

    private boolean isBottomSection(final Section section) {
        final Station downStation = section.getDownStation();
        return value.stream()
            .noneMatch(otherSection -> otherSection.getUpStation().equals(downStation));
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }

    public int size() {
        return value.size();
    }

    public void addTop(final Section section) {
        value.add(0, section);
    }

    public void addBottom(final Section section) {
        value.add(section);
    }

    public Section findSection(final Station upStation, final Station downStation) {
        return value.stream()
            .filter(section -> section.bothStationsEquals(upStation, downStation))
            .findAny()
            .orElseThrow(() -> new BusinessException("찾을 수 없습니다."));
    }

    public void remove(final Section section) {
        value.remove(section);
    }

    public void addAll(final Section... sections) {
        Collections.addAll(value, sections);
        sort();
    }

    public Section findSection(final int index) {
        return value.get(index);
    }

    public Station findTopStation() {
        if (isEmpty()) {
            throw new BusinessException("빈 구간 목록입니다.");
        }
        return value.get(0).getUpStation();
    }

    public Station findBottomStation() {
        if (isEmpty()) {
            throw new BusinessException("빈 구간 목록입니다.");
        }
        return value.get(value.size() - 1).getDownStation();
    }

    public Station findStation(final int index) {
        if (index == 0) {
            return findTopStation();
        }
        return value.get(index - 1).getDownStation();
    }

    public int getStationsSize() {
        if (size() == 0) {
            return 0;
        }
        return size() + 1;
    }
}
