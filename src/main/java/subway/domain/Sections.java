package subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import subway.exception.BusinessException;

public class Sections {

    private static final String NOT_EXIST_SECTION_MESSAGE = "존재하지 않는 섹션입니다.";
    private static final String EMPTY_MESSAGE = "빈 섹션 목록입니다.";
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
        return value.stream()
            .filter(this::isTopSection)
            .findAny()
            .orElseThrow(() -> new BusinessException(NOT_EXIST_SECTION_MESSAGE));
    }

    private Section findBottomSection() {
        return value.stream()
            .filter(this::isBottomSection)
            .findAny()
            .orElseThrow(() -> new BusinessException(NOT_EXIST_SECTION_MESSAGE));
    }

    private Section findNextSection(final Section currentSection) {
        return value.stream()
            .filter(section -> currentSection.getDownStation().equals(section.getUpStation()))
            .findAny()
            .orElseThrow(() -> new BusinessException(NOT_EXIST_SECTION_MESSAGE));
    }

    private boolean isTopSection(final Section section) {
        final Station upStation = section.getUpStation();
        return value.stream()
            .noneMatch(it -> it.isDownStation(upStation));
    }

    private boolean isBottomSection(final Section section) {
        final Station downStation = section.getDownStation();
        return value.stream()
            .noneMatch(it -> it.isUpStation(downStation));
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

    public void remove(final Section section) {
        value.remove(section);
    }

    public void addAll(final Section... sections) {
        Collections.addAll(value, sections);
        sort();
    }

    public Section findSection(final Station upStation, final Station downStation) {
        return value.stream()
            .filter(section -> section.bothStationsEquals(upStation, downStation))
            .findAny()
            .orElseThrow(() -> new BusinessException(NOT_EXIST_SECTION_MESSAGE));
    }

    public Optional<Section> findSection(final int index) {
        if (index < 0 || index > value.size() - 1) {
            return Optional.empty();
        }
        return Optional.of(value.get(index));
    }

    public Optional<Section> findSectionByUpStation(final Station upStation) {
        return value.stream()
            .filter(section -> section.isDownStation(upStation))
            .findAny();
    }

    public Optional<Section> findSectionByDownStation(final Station downStation) {
        return value.stream()
            .filter(section -> section.isUpStation(downStation))
            .findAny();
    }

    public Station findTopStation() {
        if (isEmpty()) {
            throw new BusinessException(EMPTY_MESSAGE);
        }
        return value.get(0).getUpStation();
    }

    public Station findBottomStation() {
        if (isEmpty()) {
            throw new BusinessException(EMPTY_MESSAGE);
        }
        return value.get(value.size() - 1).getDownStation();
    }

    public Station findStation(final int index) {
        if (index == 0) {
            return findTopStation();
        }
        return value.get(index - 1).getDownStation();
    }
    
    public void remove(final Station station) {
        final Optional<Section> optionalUpSection = findSectionByUpStation(station);
        final Optional<Section> optionalDownSection = findSectionByDownStation(station);
        if (optionalUpSection.isEmpty() && optionalDownSection.isEmpty()) {
            throw new BusinessException("해당 호선에 존재하지 않는 역입니다.");
        }
        if (removeOneSection(optionalUpSection, optionalDownSection)) {
            return;
        }
        if (value.size() == 1) {
            value.clear();
            return;
        }
        removeAndReplace(optionalUpSection.get(), optionalDownSection.get());
        sort();
    }

    private void removeAndReplace(final Section upSection, final Section downSection) {
        removeAll(upSection, downSection);
        final Section newSection = new Section(upSection.getUpStation(), downSection.getDownStation(),
            upSection.getDistance().plus(downSection.getDistance()));
        value.add(newSection);
    }

    private boolean removeOneSection(final Optional<Section> optionalUpSection,
        final Optional<Section> optionalDownSection) {
        if (optionalUpSection.isPresent() && optionalDownSection.isEmpty()) {
            value.remove(optionalUpSection.get());
            return true;
        }
        if (optionalUpSection.isEmpty() && optionalDownSection.isPresent()) {
            value.remove(optionalDownSection.get());
            return true;
        }
        return false;
    }

    private void removeAll(final Section... sections) {
        for (final Section section : sections) {
            remove(section);
        }
    }

    public int getStationsSize() {
        if (size() == 0) {
            return 0;
        }
        return size() + 1;
    }

    public List<Section> getValue() {
        return List.copyOf(value);
    }
}
