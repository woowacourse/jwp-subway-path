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

    public Sections addTop(final Section section) {
        final List<Section> newValue = new ArrayList<>(value);
        newValue.add(0, section);
        return new Sections(newValue);
    }

    public Sections addBottom(final Section section) {
        final List<Section> newValue = new ArrayList<>(value);
        newValue.add(section);
        return new Sections(newValue);
    }

    public Sections remove(final Section section) {
        final List<Section> newValue = new ArrayList<>(value);
        newValue.remove(section);
        return new Sections(newValue);
    }
    
    public Sections addAll(final Section... sections) {
        final List<Section> newValue = new ArrayList<>(value);
        Collections.addAll(newValue, sections);
        return new Sections(newValue);
    }

    public Sections remove(final Station station) {
        if (value.size() == 1) {
            return new Sections(new ArrayList<>());
        }
        final Optional<Section> optionalUpSection = findSectionByUpStation(station);
        final Optional<Section> optionalDownSection = findSectionByDownStation(station);
        if (optionalUpSection.isEmpty() && optionalDownSection.isEmpty()) {
            throw new BusinessException("해당 호선에 존재하지 않는 역입니다.");
        }
        if (optionalUpSection.isPresent() && optionalDownSection.isEmpty()) {
            final List<Section> newValue = new ArrayList<>(value);
            newValue.remove(optionalUpSection.get());
            return new Sections(newValue);
        }
        if (optionalUpSection.isEmpty() && optionalDownSection.isPresent()) {
            final List<Section> newValue = new ArrayList<>(value);
            newValue.remove(optionalDownSection.get());
            return new Sections(newValue);
        }
        return removeAndReplace(optionalUpSection.get(), optionalDownSection.get());
    }

    private Sections removeAndReplace(final Section upSection, final Section downSection) {
        final Sections sections = removeAll(upSection, downSection);
        final Section newSection = new Section(upSection.getUpStation(), downSection.getDownStation(),
            upSection.getDistance().plus(downSection.getDistance()));
        return sections.addAll(newSection);
    }

    private Sections removeAll(final Section... sections) {
        final List<Section> newValue = new ArrayList<>(value);
        for (final Section section : sections) {
            newValue.remove(section);
        }
        return new Sections(newValue);
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }

    public int size() {
        return value.size();
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
