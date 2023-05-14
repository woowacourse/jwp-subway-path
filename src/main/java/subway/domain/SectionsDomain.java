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
        addSectionBy(newSection);
    }

    private void validate(final SectionDomain newSection) {
        if (isNotEmpty() && isBaseStationNotExist(newSection)) {
            throw new IllegalArgumentException("구간이 하나 이상 존재하는 노선에 새로운 구간을 등록할 때 기준이 되는 지하철역이 존재해야 합니다.");
        }
        if (isAlreadyExist(newSection)) {
            throw new IllegalArgumentException("노선에 이미 존재하는 구간을 등록할 수 없습니다.");
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

    private void addSectionBy(final SectionDomain newSection) {
        if (isInnerSectionBaseIsUpStation(newSection)) {
            return;
        }
        if (isInnerSectionBaseIsDownStation(newSection)) {
            return;
        }
        if (isOuterSectionBaseIsUpStation(newSection)) {
            return;
        }
        if (isOuterSectionBaseIsDownStation(newSection)) {
            return;
        }
        sections.add(newSection);
    }

    private boolean isInnerSectionBaseIsUpStation(final SectionDomain newSection) {
        final Optional<SectionDomain> maybeInnerSectionBaseIsUp
                = findSectionWithOldUpStation(newSection.getUpStation());

        if (maybeInnerSectionBaseIsUp.isPresent()) {
            addNewSectionAfterRemoveOldSection(newSection, maybeInnerSectionBaseIsUp.get());
            return true;
        }
        return false;
    }

    private Optional<SectionDomain> findSectionWithOldUpStation(final StationDomain station) {
        return sections.stream()
                .dropWhile(section -> !section.isSameUpStationBy(station))
                .findFirst();
    }

    private void addNewSectionAfterRemoveOldSection(final SectionDomain newSection, final SectionDomain oldSection) {
        final int oldSectionIndex = sections.indexOf(oldSection);
        sections.remove(oldSection);
        sections.addAll(oldSectionIndex, oldSection.separateBy(newSection));
    }

    private boolean isInnerSectionBaseIsDownStation(final SectionDomain newSection) {
        final Optional<SectionDomain> maybeInnerSectionBaseIsDown
                = findSectionWithOldDownStation(newSection.getDownStation());

        if (maybeInnerSectionBaseIsDown.isPresent()) {
            addNewSectionAfterRemoveOldSection(newSection, maybeInnerSectionBaseIsDown.get());
            return true;
        }
        return false;
    }

    private Optional<SectionDomain> findSectionWithOldDownStation(final StationDomain station) {
        return sections.stream()
                .dropWhile(section -> !section.isSameDownStationBy(station))
                .findFirst();
    }

    private boolean isOuterSectionBaseIsUpStation(final SectionDomain newSection) {
        final Optional<SectionDomain> maybeOuterSectionBaseIsUp
                = findSectionWithOldUpStation(newSection.getDownStation());

        if (maybeOuterSectionBaseIsUp.isPresent()) {
            addNewSectionAfterRemoveOldSection(newSection, maybeOuterSectionBaseIsUp.get());
            return true;
        }
        return false;
    }

    private boolean isOuterSectionBaseIsDownStation(final SectionDomain newSection) {
        final Optional<SectionDomain> maybeOuterSectionBaseIsDown
                = findSectionWithOldDownStation(newSection.getUpStation());

        if (maybeOuterSectionBaseIsDown.isPresent()) {
            addNewSectionAfterRemoveOldSection(newSection, maybeOuterSectionBaseIsDown.get());
            return true;
        }
        return false;
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
