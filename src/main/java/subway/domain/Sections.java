package subway.domain;

import java.util.List;
import subway.domain.exception.BusinessException;

public class Sections {

    private final List<Section> value;

    public Sections(final List<Section> value) {
        this.value = value;
    }

    public void sort() {
        if (value.isEmpty()) {
            return;
        }
        makeTop();
        tailBite(0);
    }

    private void makeTop() {
        for (final Section section : value) {
            if (isTopSection(section)) {
                changeIndex(section, 0);
                break;
            }
        }
    }

    private void changeIndex(final Section section, int targetIndex) {
        final int currentIndex = value.indexOf(section);
        if (currentIndex >= 0 && currentIndex != targetIndex) {
            value.remove(currentIndex);
            if (targetIndex > currentIndex) {
                targetIndex--;
            }
            value.add(targetIndex, section);
        }
    }

    private boolean isTopSection(final Section section) {
        final Station upStation = section.getUpStation();
        return value.stream()
            .noneMatch(otherSection -> otherSection.getDownStation().equals(upStation));
    }

    private void tailBite(int index) {
        final Section current = value.get(index);
        if (isBottomSection(current)) {
            return;
        }
        final Station currentDownStation = current.getDownStation();
        for (final Section section : value) {
            if (section.getUpStation().equals(currentDownStation)) {
                changeIndex(section, ++index);
                tailBite(index);
            }
        }
    }

    private boolean isBottomSection(final Section section) {
        final Station downStation = section.getDownStation();
        return value.stream()
            .noneMatch(otherSection -> otherSection.getUpStation().equals(downStation));
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }

    public void add(final Section section) {
        value.add(section);
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

    public int getStationsSize() {
        if (size() == 0) {
            return 0;
        }
        return size() + 1;
    }

    public Section getSection(final int index) {
        return value.get(index);
    }

    public Station getTopStation() {
        if (isEmpty()) {
            throw new BusinessException("빈 구간 목록입니다.");
        }
        return value.get(0).getUpStation();
    }

    public Station getBottomStation() {
        if (isEmpty()) {
            throw new BusinessException("빈 구간 목록입니다.");
        }
        return value.get(value.size() - 1).getDownStation();
    }
}
