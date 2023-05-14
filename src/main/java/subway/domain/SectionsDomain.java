package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SectionsDomain {

    private final List<SectionDomain> sections = new ArrayList<>();

    public static SectionsDomain from(final List<SectionDomain> newSections) {
        final SectionsDomain sections = new SectionsDomain();
        newSections.forEach(sections::addSection);
        return sections;
    }

    public void addSection(final SectionDomain newSection) {
        validate(newSection);
        this.sections.add(newSection);
    }

    private void validate(final SectionDomain newSection) {
        if (isNotEmpty() && isBaseStationNotExist(newSection)) {
            throw new IllegalArgumentException("구간이 하나 이상 존재하는 노선에 새로운 구간을 등록할 때 기준이 되는 지하철역이 존재해야 합니다.");
        }
        if (isAlreadyExist(newSection)) {
            throw new IllegalArgumentException("노선에 이미 존재하는 구간을 등록할 수 없습니다.");
        }
        if (isDistanceRangeNotOk(newSection)) {
            throw new IllegalArgumentException("기존 구간 사이에 들어갈 새로운 구간의 길이가 더 크거나 같을 수 없습니다.");
        }
    }

    private boolean isNotEmpty() {
        return !sections.isEmpty();
    }

    private boolean isBaseStationNotExist(final SectionDomain newSection) {
        return sections.stream().noneMatch(section -> section.isBaseStationExist(newSection));
    }

    private boolean isAlreadyExist(final SectionDomain newSection) {
        return sections.contains(newSection);
    }

    private boolean isDistanceRangeNotOk(final SectionDomain newSection) {
        if (isDistanceRangeOverSectionWithOldUpStation(newSection)) {
            return true;
        }
        return isDistanceRangeOverSectionWithOldDownStation(newSection);
    }

    private boolean isDistanceRangeOverSectionWithOldUpStation(final SectionDomain newSection) {
        final Optional<SectionDomain> maybeSectionWithOldUpStation
                = findSectionWithOldUpStation(newSection.getUpStation());

        if (maybeSectionWithOldUpStation.isPresent()) {
            final SectionDomain sectionWithOldUpStation = maybeSectionWithOldUpStation.get();

            return sectionWithOldUpStation.isDistanceLessThan(newSection);
        }
        return false;
    }

    private Optional<SectionDomain> findSectionWithOldUpStation(final StationDomain station) {
        return sections.stream()
                .dropWhile(section -> !section.isSameUpStationBy(station))
                .findFirst();
    }

    private boolean isDistanceRangeOverSectionWithOldDownStation(final SectionDomain newSection) {
        final Optional<SectionDomain> maybeSectionWithOldDownStation
                = findSectionWithOldDownStation(newSection.getDownStation());

        if (maybeSectionWithOldDownStation.isPresent()) {
            final SectionDomain findSection = maybeSectionWithOldDownStation.get();

            return findSection.isDistanceLessThan(newSection);
        }
        return false;
    }

    private Optional<SectionDomain> findSectionWithOldDownStation(final StationDomain station) {
        return sections.stream()
                .dropWhile(section -> !section.isSameDownStationBy(station))
                .findFirst();
    }

    public List<StationDomain> collectAllStations() {
        final List<StationDomain> stations = new ArrayList<>();

        stations.addAll(sections.stream()
                .map(SectionDomain::getUpStation)
                .collect(Collectors.toList()));

        stations.addAll(sections.stream()
                .map(SectionDomain::getDownStation)
                .collect(Collectors.toList()));

        return stations;
    }

    public List<SectionDomain> getSections() {
        return new ArrayList<>(sections);
    }
}
