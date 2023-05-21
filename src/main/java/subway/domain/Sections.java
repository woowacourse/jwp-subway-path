package subway.domain;

import static java.util.stream.Collectors.toList;

import subway.exception.IllegalAddSectionException;
import subway.exception.IllegalRemoveSectionException;
import subway.exception.SectionNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Sections {

    private static final int EMPTY_SECTION = 0;
    private static final int ONLY_ONE_SECTION = 1;
    private static final int ONLY_ONE_SECTION_INDEX = 0;

    private final List<Section> sections;
    private final TopDownStationArranger topDownStationArranger = new TopDownStationArranger();

    public Sections(final List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public Section add(final Section newSection) {
        validate(newSection);
        validateForkRoad(newSection);

        sections.add(newSection);

        return newSection;
    }

    public void addTwoSections(final Section upSection, final Section downSection) {
        validateTwoSection(upSection, downSection);

        Section originSection = findSectionBy(upSection, downSection);

        if (canAddTwoSections(upSection, downSection, originSection)) {
            sections.remove(originSection);
            sections.add(upSection);
            sections.add(downSection);
            return;
        }
        throw new IllegalAddSectionException();
    }

    private void validate(final Section section) {
        validateDuplicate(section);
        validateReverse(section);
        validateSameDirection(section);
    }

    private void validateDuplicate(final Section section) {
        if (sections.contains(section)) {
            throw new IllegalAddSectionException("이미 존재하는 Section입니다.");
        }
    }

    private void validateReverse(final Section otherSection) {
        boolean isExistReverseSection = sections.stream()
                .anyMatch(section -> section.isReverseDirection(otherSection));

        if (isExistReverseSection) {
            throw new IllegalAddSectionException("이미 방향만 반대인 Section이 존재합니다.");
        }
    }

    private void validateSameDirection(final Section otherSection) {
        boolean isSameDirection = sections.stream()
                .anyMatch(section -> section.isSameDirection(otherSection) && section.isSameLine(otherSection));

        if (isSameDirection) {
            throw new IllegalAddSectionException("이미 같은 방향인 Section이 존재합니다.");
        }
    }

    private void validateForkRoad(final Section newSection) {
        boolean isForkRoad = sections.stream()
                .anyMatch(section -> section.isForkRoad(newSection));

        if (isForkRoad) {
            throw new IllegalAddSectionException("역은 갈림길이 될 수 없습니다.");
        }
    }

    private void validateTwoSection(final Section upSection, final Section downSection) {
        validate(upSection);
        validate(downSection);
        validateSameLine(upSection, downSection);
    }

    private void validateSameLine(final Section upSection, final Section downSection) {
        if (!upSection.isSameLine(downSection)) {
            throw new IllegalAddSectionException("두 Section의 호선이 다릅니다.");
        }
    }

    private boolean canAddTwoSections(final Section upSection, final Section downSection, final Section originSection) {
        return (isAllSameLine(upSection, downSection, originSection)) && (originSection.isSameDistance(upSection, downSection));
    }

    private boolean isAllSameLine(final Section upSection, final Section downSection, final Section originSection) {
        return originSection.isSameLine(upSection) && originSection.isSameLine(downSection) && upSection.isSameLine(downSection);
    }

    private Section findSectionBy(final Section upSection, final Section downSection) {
        Station startStation = upSection.getUpStation();
        Station endStation = downSection.getDownStation();

        Optional<Section> foundSection = sections.stream()
                .filter(section -> section.isSameUpStation(startStation)
                        && section.isSameDownStation(endStation))
                .findFirst();

        return foundSection.orElseThrow(() -> new IllegalAddSectionException("연결되어 있는 Section이 존재하지 않습니다."));
    }

    public List<Station> allStations() {
        return topDownStationArranger.arrange(this.sections);
    }

    public void removeStation(final Station targetStation) {
        List<Section> filteredSections = sections.stream()
                .filter(section -> section.hasSameStation(targetStation))
                .collect(toList());
        int size = filteredSections.size();

        if (size == EMPTY_SECTION) {
            throw new IllegalRemoveSectionException("역이 포함되어 있지 않습니다.");
        }
        if (size == ONLY_ONE_SECTION) {
            removeOneSection(filteredSections);
            return;
        }
        removeTwoSections(targetStation);
    }

    private void removeOneSection(final List<Section> filteredSections) {
        Section section = filteredSections.get(ONLY_ONE_SECTION_INDEX);
        sections.remove(section);
    }

    private void removeTwoSections(final Station targetStation) {
        Section upSection = findUpSection(targetStation);
        Section downSection = findDownSection(targetStation);
        Section newSection = Section.combineSection(upSection, downSection);

        sections.remove(upSection);
        sections.remove(downSection);
        sections.add(newSection);
    }

    private Section findUpSection(final Station downStation) {
        Optional<Section> foundSection = sections.stream()
                .filter(section -> section.isSameDownStation(downStation))
                .findFirst();

        return foundSection.orElseThrow(SectionNotFoundException::new);
    }

    private Section findDownSection(final Station upStation) {
        Optional<Section> foundSection = sections.stream()
                .filter(section -> section.isSameUpStation(upStation))
                .findFirst();

        return foundSection.orElseThrow(SectionNotFoundException::new);
    }

    public List<Section> getSections() {
        return List.copyOf(sections);
    }
}
