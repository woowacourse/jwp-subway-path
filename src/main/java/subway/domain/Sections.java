package subway.domain;

import subway.exception.GlobalException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Sections {
    private final List<Section> sections;

    public Sections(List<Section> sections) {
        validateDuplication(sections);
        this.sections = new ArrayList<>(sections);
    }

    private void validateDuplication(List<Section> sections) {
        long distinctSize = sections.stream()
                .distinct()
                .count();

        if (sections.isEmpty()) {
            return;
        }

        if (sections.size() != distinctSize) {
            throw new GlobalException("구간은 중복될 수 없습니다.");
        }
    }

    public void remove(Station station) {
        List<Section> sectionsContainStation = getSectionsContainsStation(station);

        if (sections.size() == 1) {
            sections.clear();
            return;
        }

        if (sectionsContainStation.size() == 1) {
            Section findSection = sectionsContainStation.get(0);
            sections.remove(findSection);
            return;
        }

        update(station, sectionsContainStation.get(0), sectionsContainStation.get(1));
    }

    private List<Section> getSectionsContainsStation(Station station) {
        List<Section> sectionsContainStation = sections.stream()
                .filter(it -> it.hasStation(station))
                .collect(Collectors.toList());

        if (sectionsContainStation.isEmpty()) {
            throw new GlobalException("존재하지 않는 구간입니다.");
        }

        return sectionsContainStation;
    }

    private void update(Station station, Section firstSection, Section secondSection) {
        Section newSection = createSectionExceptStation(station, firstSection, secondSection);

        sections.add(newSection);
        sections.remove(firstSection);
        sections.remove(secondSection);
    }

    private Section createSectionExceptStation(Station station, Section firstSection, Section secondSection) {
        Distance distance = firstSection.addDistance(secondSection);
        Station startStation = firstSection.getStartStation();
        Station endStation = secondSection.getEndStation();

        if (secondSection.isSameEndStation(station) && firstSection.isSameStartStation(station)) {
            startStation = secondSection.getStartStation();
            endStation = firstSection.getEndStation();
        }

        return new Section(startStation, endStation, distance);
    }

    public void add(Section newSection) {
        if (sections.isEmpty()) {
            sections.add(newSection);
            return;
        }
        validateForAdd(newSection);

        Station upStation = getUpStation();
        Station downStation = getDownStation();
        if (newSection.isSameEndStation(upStation) || newSection.isSameStartStation(downStation)) {
            sections.add(newSection);
            return;
        }

        Section findSection = findSectionForAdd(newSection);
        separateSection(findSection, newSection);
    }

    private void validateForAdd(Section newSection) {
        if (sections.contains(newSection)) {
            throw new GlobalException("이미 존재하는 구간입니다.");
        }

        validateConnection(newSection);
    }

    private void validateConnection(Section section) {
        boolean isPresentSameStartStation = sections.stream()
                .anyMatch(it -> it.isSameStartStation(section.getStartStation()) ||
                        it.isSameStartStation(section.getEndStation()));

        boolean isPresentSameEndStation = sections.stream()
                .anyMatch(it -> it.isSameEndStation(section.getStartStation()) ||
                        it.isSameEndStation(section.getEndStation()));

        if (isPresentSameStartStation && isPresentSameEndStation) {
            throw new GlobalException("이미 연결되어 있는 구간입니다.");
        }

        if (!isPresentSameStartStation && !isPresentSameEndStation) {
            throw new GlobalException("연결되어 있지 않은 구간입니다.");
        }
    }

    private Section findSectionForAdd(Section newSection) {
        Section findSection = sections.stream()
                .filter(newSection::isSameStartStation)
                .findFirst()
                .orElseGet(() -> sections.stream()
                        .filter(newSection::isSameEndStation)
                        .findFirst()
                        .orElseThrow(() -> new GlobalException("연결하려는 구간에 역이 존재하지 않습니다.")));

        if (findSection.isGreaterThanOtherDistance(newSection)) {
            return findSection;
        }

        throw new GlobalException("구간 길이로 인해 연결할 수 없습니다.");
    }

    private Section createSectionBetweenSections(Section firstSection, Section secondSection) {
        Distance subtractedDistance = firstSection.subtractDistance(secondSection);

        if (firstSection.isSameStartStation(secondSection)) {
            return new Section(secondSection.getEndStation(), firstSection.getEndStation(),
                    subtractedDistance);
        }

        return new Section(firstSection.getStartStation(), secondSection.getStartStation(),
                subtractedDistance);
    }

    private void separateSection(Section originSection, Section newSection) {
        Section devidedSection = createSectionBetweenSections(originSection, newSection);

        createSectionBetweenSections(originSection, newSection);

        sections.remove(originSection);
        sections.add(newSection);
        sections.add(devidedSection);
    }

    public List<Station> getOrderedStations() {
        List<Station> sortedStations = new ArrayList<>();

        Station startStation = getUpStation();
        sortedStations.add(startStation);
        while (sortedStations.size() <= sections.size()) {
            Section section = getSectionHavingSameStartStation(startStation);
            sortedStations.add(section.getEndStation());
            startStation = section.getEndStation();
        }
        return sortedStations;
    }

    private Section getSectionHavingSameStartStation(Station startStation) {
        return sections.stream()
                .filter(it -> it.isSameStartStation(startStation))
                .findFirst()
                .orElseThrow(() -> new GlobalException("해당 역을 출발 역으로 갖는 구간이 없습니다."));
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
        throw new GlobalException("하행 종점이 존재하지 않습니다.");
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
        throw new GlobalException("상행 종점이 존재하지 않습니다.");
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }
}
