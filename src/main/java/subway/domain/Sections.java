package subway.domain;

import static subway.domain.ChangeSectionStatus.FOR_EDGE_SECTION;
import static subway.domain.ChangeSectionStatus.FOR_MIDDLE_SECTION;
import static subway.domain.ChangeSections.makeChangeSectionsForUpdateEdgeSectionsByStatus;
import static subway.domain.ChangeSections.makeChangeSectionsForUpdateSectionsByStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import subway.exception.section.AlreadyConnectedSectionException;
import subway.exception.section.DisconnectedSectionException;
import subway.exception.section.DuplicateSectionException;
import subway.exception.section.InvalidAddSectionLengthException;
import subway.exception.section.NotFoundSectionException;
import subway.exception.station.NotFoundStationException;

public class Sections {
    private final List<Section> sections;

    public Sections(List<Section> sections) {
        validateDuplication(sections);
        this.sections = new ArrayList<>(sections);
    }

    public ChangeSections remove(Station station) {
        List<Section> sectionsContainStation = findSectionsContainingStation(station);

        if (isOnlyOneSection(sectionsContainStation)) {
            Section findSection = sectionsContainStation.get(0);
            sections.remove(findSection);
            return makeChangeSectionsForUpdateEdgeSectionsByStatus(
                    FOR_EDGE_SECTION,
                    findSection
            );
        }

        int totalDistance = calculateTotalDistance(sectionsContainStation);

        Section firstSection = sectionsContainStation.get(0);
        Section secondSection = sectionsContainStation.get(1);

        Station startStation = firstSection.getStartStation();
        Station endStation = secondSection.getEndStation();

        if (secondSection.isSameEndStation(station) && firstSection.isSameStartStation(station)) {
            startStation = secondSection.getStartStation();
            endStation = firstSection.getEndStation();
        }

        if (firstSection.isSameStartStation(startStation)) {
            Section changedSection = Section.builder()
                    .startStation(startStation)
                    .endStation(endStation)
                    .distance(new Distance(totalDistance))
                    .build();

            sections.add(changedSection);
            sections.remove(firstSection);
            sections.remove(secondSection);
            return ChangeSections.makeChangeSectionsForUpdateSectionsByStatus(
                    FOR_MIDDLE_SECTION,
                    firstSection,
                    secondSection
            );
        }

        Section changedSection = Section.builder()
                .startStation(startStation)
                .endStation(endStation)
                .distance(new Distance(totalDistance))
                .build();

        sections.add(changedSection);
        sections.remove(firstSection);
        sections.remove(secondSection);
        return ChangeSections.makeChangeSectionsForUpdateSectionsByStatus(
                FOR_MIDDLE_SECTION,
                secondSection,
                firstSection
        );
    }

    private boolean isOnlyOneSection(final List<Section> sectionsContainStation) {
        return sectionsContainStation.size() == 1;
    }

    private List<Section> findSectionsContainingStation(Station station) {
        List<Section> sectionsContainStation = sections.stream()
                .filter(it -> it.hasStation(station))
                .collect(Collectors.toList());

        if (sectionsContainStation.isEmpty()) {
            throw new NotFoundSectionException("존재하지 않는 구간입니다.");
        }

        return sectionsContainStation;
    }

    private int calculateTotalDistance(List<Section> sections) {
        return sections.stream()
                .map(Section::getDistance)
                .mapToInt(Distance::getDistance)
                .sum();
    }

    public ChangeSections add(Section newSection) {
        if (sections.isEmpty()) {
            sections.add(newSection);
            return makeChangeSectionsForUpdateEdgeSectionsByStatus(
                    FOR_EDGE_SECTION,
                    newSection);
        }

        if (sections.contains(newSection)) {
            throw new DuplicateSectionException("이미 존재하는 구간입니다.");
        }

        validateConnection(newSection);
        Station upStation = getUpStation();
        Station downStation = getDownStation();
        if (sections.isEmpty() || newSection.isSameEndStation(upStation) || newSection.isSameStartStation(
                downStation)) {
            sections.add(newSection);
            return makeChangeSectionsForUpdateEdgeSectionsByStatus(
                    FOR_EDGE_SECTION,
                    newSection
            );
        }

        Section findSection = findForAddByDistance(newSection);

        if (findSection.isSameStartStation(newSection)) {
            Distance subtractedDistance = findSection.subtractDistance(newSection);
            Section devidedSection = Section.builder(findSection)
                    .startStation(newSection.getEndStation())
                    .distance(subtractedDistance)
                    .build();

            findSection = Section.builder(findSection)
                    .endStation(newSection.getEndStation())
                    .distance(newSection.getDistance())
                    .build();

            sections.add(devidedSection);
            return makeChangeSectionsForUpdateSectionsByStatus(
                    FOR_MIDDLE_SECTION,
                    findSection,
                    newSection
            );
        }

        Distance subtractedDistance = findSection.subtractDistance(newSection);
        Section devidedSection = Section.builder(findSection)
                .endStation(newSection.getStartStation())
                .distance(subtractedDistance)
                .build();

        findSection = Section.builder(findSection)
                .startStation(newSection.getStartStation())
                .distance(newSection.getDistance())
                .build();

        sections.add(devidedSection);
        return makeChangeSectionsForUpdateSectionsByStatus(
                FOR_MIDDLE_SECTION,
                findSection,
                newSection
        );
    }

    private Section findForAddByDistance(Section newSection) {
        Optional<Section> findSameStartStationSection = sections.stream()
                .filter(newSection::isSameStartStation)
                .findFirst();

        if (findSameStartStationSection.isPresent()) {
            Section findSection = findSameStartStationSection.get();
            if (findSection.isGreaterThanOtherDistance(newSection)) {
                return findSection;
            }
        }

        Optional<Section> findSameEndStationSection = sections.stream()
                .filter(newSection::isSameEndStation)
                .findFirst();

        if (findSameEndStationSection.isPresent()) {
            Section findSection = findSameEndStationSection.get();
            if (findSection.isGreaterThanOtherDistance(newSection)) {
                return findSection;
            }
        }
        throw new InvalidAddSectionLengthException("구간 길이로 인해 연결할 수 없습니다.");
    }

    private void validateDuplication(List<Section> sections) {
        long distinctSize = sections.stream()
                .distinct()
                .count();

        if (sections.isEmpty()) {
            return;
        }

        if (sections.size() != distinctSize) {
            throw new DuplicateSectionException("이미 존재하는 구간입니다.");
        }
    }

    private void validateConnection(Section section) {
        boolean isPresentSameStartStation = sections.stream()
                .anyMatch(it -> it.isSameStartStation(section.getStartStation()) ||
                        it.isSameStartStation(section.getEndStation()));

        boolean isPresentSameEndStation = sections.stream()
                .anyMatch(it -> it.isSameEndStation(section.getStartStation()) ||
                        it.isSameEndStation(section.getEndStation()));

        if (isPresentSameStartStation && isPresentSameEndStation) {
            throw new AlreadyConnectedSectionException("이미 연결되어 있는 구간입니다.");
        }

        if (!isPresentSameStartStation && !isPresentSameEndStation) {
            throw new DisconnectedSectionException("연결되어 있지 않은 구간입니다.");
        }
    }

    public Station getDownStation() {
        for (Section savedSection : sections) {
            int count = 0;
            for (Section section : sections) {
                if (section.equals(savedSection)) {
                    continue;
                }
                if (section.getStartStation().equals(savedSection.getEndStation())) {
                    count++;
                    break;
                }
            }
            if (count == 0) {
                return savedSection.getEndStation();
            }
        }
        throw new NotFoundStationException("하행 종점이 존재하지 않습니다.");
    }

    public Station getUpStation() {
        for (Section savedSection : sections) {
            int count = 0;
            for (Section section : sections) {
                if (section.equals(savedSection)) {
                    continue;
                }
                if (section.getEndStation().equals(savedSection.getStartStation())) {
                    count++;
                    break;
                }
            }
            if (count == 0) {
                return savedSection.getStartStation();
            }
        }
        throw new NotFoundStationException("상행 종점이 존재하지 않습니다.");
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

}
