package subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import subway.exception.GlobalException;

public class Sections {
    private static final Station EMPTY_STATION = new Station("빈 역 ㅋㅋ");
    private final List<Section> sections;
    private Station upStation;
    private Station downStation;

    public Sections(final List<Section> sections, final Station upStation, final Station downStation) {
        validateDuplication(sections);
        this.sections = new ArrayList<>(sections);
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public void remove(Station station) {
        //존재하지 않는 역
        List<Section> sectionsContainStation = sections.stream()
                .filter(it -> it.hasStation(station))
                .collect(Collectors.toList());

        if (sectionsContainStation.isEmpty()) {
            throw new GlobalException("존재하지 않는 구간입니다.");
        }

        // 역이 단 두 개일때, 모두 삭제된다.
        if (sections.size() == 1) {
            sections.clear();
            upStation = EMPTY_STATION;
            downStation = EMPTY_STATION;
            return;
        }

        if (sectionsContainStation.size() == 1) {
            Section findSection = sectionsContainStation.get(0);
            sections.remove(findSection);
            if (findSection.isSameStartStation(station)) {
                upStation = findSection.getEndStation();
                return;
            }
            downStation = findSection.getStartStation();
            return;
        }

        int totalDistance = sectionsContainStation.stream()
                .map(Section::getDistance)
                .mapToInt(Distance::getDistance)
                .sum();

        Section firstSection = sectionsContainStation.get(0);
        Section secondSection = sectionsContainStation.get(1);

        Station startStation = firstSection.getStartStation();
        Station endStation = secondSection.getEndStation();

        if (secondSection.isSameEndStation(station) && firstSection.isSameStartStation(station)) {
            startStation = secondSection.getStartStation();
            endStation = firstSection.getEndStation();
        }

        Section newSection = new Section(startStation, endStation, new Distance(totalDistance));

        sections.add(newSection);
        sections.remove(firstSection);
        sections.remove(secondSection);

    }

    public void add(Section newSection) {
        if (sections.contains(newSection)) {
            throw new GlobalException("이미 존재하는 구간입니다.");
        }

        validateConnection(newSection);

        if (newSection.isSameEndStation(upStation)) {
            sections.add(newSection);
            upStation = newSection.getStartStation();
            return;
        }

        if (newSection.isSameStartStation(downStation)) {
            sections.add(newSection);
            downStation = newSection.getEndStation();
            return;
        }

        Section findSection = findForAddByDistance(newSection);

        if (findSection.isSameStartStation(newSection)) {
            Distance subtractedDistance = findSection.subtractDistance(newSection);
            Section devidedSection = new Section(newSection.getEndStation(), findSection.getEndStation(),
                    subtractedDistance);

            sections.remove(findSection);
            sections.add(newSection);
            sections.add(devidedSection);
            return;
        }

        Distance subtractedDistance = findSection.subtractDistance(newSection);
        Section devidedSection = new Section(findSection.getStartStation(), newSection.getStartStation(),
                subtractedDistance);

        sections.remove(findSection);
        sections.add(newSection);
        sections.add(devidedSection);
    }

    private Section findForAddByDistance(final Section newSection) {
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
        throw new GlobalException("구간 길이로 인해 연결할 수 없습니다.");
    }

    private void validateDuplication(final List<Section> sections) {
        long distinctSize = sections.stream()
                .distinct()
                .count();

        if (sections.isEmpty() || sections.size() != distinctSize) {
            throw new GlobalException("구간은 중복될 수 없습니다.");
        }
    }

    private void validateConnection(final Section section) {
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

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }
}
