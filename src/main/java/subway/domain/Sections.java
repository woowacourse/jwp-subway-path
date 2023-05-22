package subway.domain;

import subway.exception.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Sections {
    private static final int HAVE_ONLY_ONE_SECTION = 1;
    private final List<Section> sections;

    public Sections(List<Section> sections) {
        validateDuplication(sections);
        this.sections = new ArrayList<>(sections);
    }

    private void validateDuplication(List<Section> sections) {
        if (sections.isEmpty()) {
            return;
        }

        long distinctSize = sections.stream()
                .distinct()
                .count();

        if (sections.size() != distinctSize) {
            throw new DuplicateSectionException();
        }
    }

    public void remove(Station station) {

        List<Section> sectionsContainStation = sections.stream()
                .filter(it -> it.hasStation(station))
                .collect(Collectors.toList());

        if (sectionsContainStation.isEmpty()) {
            throw new InvalidSectionException();
        }

        if (sections.size() == HAVE_ONLY_ONE_SECTION) {
            sections.clear();
            return;
        }

        // 종점이 삭제될 때
        if (removeOutsideStation(sectionsContainStation)) {
            return;
        }

        //중간 지점이 삭제될 때
        Section firstSection = sectionsContainStation.get(0);
        Section secondSection = sectionsContainStation.get(1);

        Section newSection = makeNewSection(station, firstSection, secondSection);

        sections.add(newSection);
        sections.remove(firstSection);
        sections.remove(secondSection);
    }

    private boolean removeOutsideStation(List<Section> sectionsContainStation) {
        if (sectionsContainStation.size() == HAVE_ONLY_ONE_SECTION) {
            Section findSection = sectionsContainStation.get(0);
            sections.remove(findSection);
            return true;
        }
        return false;
    }

    private Section makeNewSection(Station station, Section firstSection, Section secondSection) {
        Station startStation = firstSection.getStartStation();
        Station endStation = secondSection.getEndStation();

        if (secondSection.isSameEndStation(station) && firstSection.isSameStartStation(station)) {
            startStation = secondSection.getStartStation();
            endStation = firstSection.getEndStation();
        }

        return new Section(startStation, endStation, firstSection.sumDistance(secondSection));
    }

    public void add(Section newSection) {
        if (sections.isEmpty()) {
            sections.add(newSection);
            return;
        }
        if (sections.contains(newSection)) {
            throw new DuplicateSectionAddException();
        }
        validateConnection(newSection);

        if (addOutsideStation(newSection)) {
            return;
        }
        addBetweenStation(newSection);
    }

    private void addBetweenStation(Section newSection) {
        Section findSection = findForAddByDistance(newSection);
        Distance subtractedDistance = findSection.subtractDistance(newSection);
        sections.remove(findSection);
        sections.add(newSection);

        if (findSection.isSameStartStation(newSection.getStartStation())) {
            Section devidedSection = new Section(newSection.getEndStation(), findSection.getEndStation(),
                    subtractedDistance);
            sections.add(devidedSection);
        } else if (findSection.isSameEndStation(newSection.getEndStation())) {
            Section devidedSection = new Section(findSection.getStartStation(), newSection.getStartStation(),
                    subtractedDistance);
            sections.add(devidedSection);
        }
    }

    private boolean addOutsideStation(Section newSection) {
        if (newSection.isSameEndStation(getUpStation()) || newSection.isSameStartStation(getDownStation())) {
            sections.add(newSection);
            return true;
        }
        return false;
    }

    private void validateConnection(Section section) {
        boolean isPresentSameStartStation = sections.stream()
                .anyMatch(it -> it.isSameStartStation(section.getStartStation()) || it.isSameStartStation(section.getEndStation()));

        boolean isPresentSameEndStation = sections.stream()
                .anyMatch(it -> it.isSameEndStation(section.getStartStation()) || it.isSameEndStation(section.getEndStation()));

        if (isPresentSameStartStation && isPresentSameEndStation) {
            throw new NotConnectSectionException("이미 연결되어 있는 구간입니다.");
        }

        if (!isPresentSameStartStation && !isPresentSameEndStation) {
            throw new NotConnectSectionException("연결되어 있지 않은 구간입니다.");
        }
    }

    private Section findForAddByDistance(Section newSection) {
        return sections.stream()
                .filter(it -> it.isSameStartStation(newSection.getStartStation()) || it.isSameEndStation(newSection.getEndStation()))
                .filter(it -> it.isGreaterThanOtherDistance(newSection))
                .findAny()
                .orElseThrow(() -> new NotConnectSectionException("구간 길이로 인해 연결할 수 없습니다."));
    }

    public List<Station> getSortedStations() {
        if (sections.isEmpty()) {
            return new ArrayList<>();
        }
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
                .orElseThrow(() -> new NotExistSectionException("해당 역을 출발 역으로 갖는 구간이 없습니다."));
    }

    public Station getUpStation() {
        return sections.stream()
                .map(Section::getStartStation)
                .filter(it -> sections.stream()
                        .noneMatch(section -> !section.getStartStation().equals(it) && section.getEndStation().equals(it)))
                .findFirst()
                .orElseThrow(() -> new NotExistSectionException("상행 종점이 존재하지 않습니다."));
    }

    public Station getDownStation() {
        return sections.stream()
                .map(Section::getEndStation)
                .filter(it -> sections.stream()
                        .noneMatch(section -> !section.getEndStation().equals(it) && section.getStartStation().equals(it)))
                .findFirst()
                .orElseThrow(() -> new NotExistSectionException("하행 종점이 존재하지 않습니다."));
    }

    public List<Section> getSections() {
        return sections;
    }
}
