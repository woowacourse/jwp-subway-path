package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Sections {

    private final List<Section> sections = new ArrayList<>();

    public static Sections from(final List<Section> newSections) {
        final Sections sections = new Sections();
        newSections.forEach(sections::addSection);
        return sections;
    }

    public void addSection(final Section newSection) {
        validate(newSection);
        addSectionBy(newSection);
    }

    private void validate(final Section newSection) {
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

    private boolean isBaseStationNotExist(final Section newSection) {
        return sections.stream().noneMatch(section -> section.isBaseStationExist(newSection));
    }

    private boolean isAlreadyExist(final Section newSection) {
        return sections.contains(newSection);
    }

    private void addSectionBy(final Section newSection) {
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

    private boolean isInnerSectionBaseIsUpStation(final Section newSection) {
        final Optional<Section> maybeInnerSectionBaseIsUp
                = findSectionWithOldUpStation(newSection.getUpStation());

        if (maybeInnerSectionBaseIsUp.isPresent()) {
            addNewSectionAfterRemoveOldSection(newSection, maybeInnerSectionBaseIsUp.get());
            return true;
        }
        return false;
    }

    private Optional<Section> findSectionWithOldUpStation(final Station station) {
        return sections.stream()
                .dropWhile(section -> !section.isSameUpStationBy(station))
                .findFirst();
    }

    private void addNewSectionAfterRemoveOldSection(final Section newSection, final Section oldSection) {
        final int oldSectionIndex = sections.indexOf(oldSection);
        sections.remove(oldSection);
        sections.addAll(oldSectionIndex, oldSection.separateBy(newSection));
    }

    private boolean isInnerSectionBaseIsDownStation(final Section newSection) {
        final Optional<Section> maybeInnerSectionBaseIsDown
                = findSectionWithOldDownStation(newSection.getDownStation());

        if (maybeInnerSectionBaseIsDown.isPresent()) {
            addNewSectionAfterRemoveOldSection(newSection, maybeInnerSectionBaseIsDown.get());
            return true;
        }
        return false;
    }

    private Optional<Section> findSectionWithOldDownStation(final Station station) {
        return sections.stream()
                .dropWhile(section -> !section.isSameDownStationBy(station))
                .findFirst();
    }

    private boolean isOuterSectionBaseIsUpStation(final Section newSection) {
        final Optional<Section> maybeOuterSectionBaseIsUp
                = findSectionWithOldUpStation(newSection.getDownStation());

        if (maybeOuterSectionBaseIsUp.isPresent()) {
            addNewSectionAfterRemoveOldSection(newSection, maybeOuterSectionBaseIsUp.get());
            return true;
        }
        return false;
    }

    private boolean isOuterSectionBaseIsDownStation(final Section newSection) {
        final Optional<Section> maybeOuterSectionBaseIsDown
                = findSectionWithOldDownStation(newSection.getUpStation());

        if (maybeOuterSectionBaseIsDown.isPresent()) {
            addNewSectionAfterRemoveOldSection(newSection, maybeOuterSectionBaseIsDown.get());
            return true;
        }
        return false;
    }

    public void removeStation(final Station removeStation) {
        final List<Section> removeSections = collectRemoveSections(removeStation);
        if (isRemoveSectionBetween(removeSections)) {
            collapseLeftRightSection(removeSections);
        }
        if (isRemoveSectionIsStart(removeSections)) {
            removeFirstSection(removeSections);
        }

        sections.removeAll(removeSections);
    }

    private List<Section> collectRemoveSections(final Station removeStation) {
        return sections.stream()
                .filter(section -> section.isSameUpStationBy(removeStation) || section.isSameDownStationBy(removeStation))
                .collect(Collectors.toList());
    }

    private boolean isRemoveSectionBetween(final List<Section> removeSections) {
        return removeSections.size() == 2;
    }

    private void collapseLeftRightSection(final List<Section> removeSections) {
        final Section leftSection = removeSections.get(0);
        final Section rightSection = removeSections.get(1);
        final Section newSection = new Section(
                sumSectionsDistance(removeSections),
                leftSection.getStart(),
                leftSection.getUpStation(),
                rightSection.getDownStation());

        sections.add(sections.indexOf(leftSection), newSection);
    }

    private boolean isRemoveSectionIsStart(final List<Section> removeSections) {
        return removeSections.size() == 1 && sections.size() > 1;
    }

    private void removeFirstSection(final List<Section> removeSections) {
        final Section firstSection = removeSections.get(0);
        final Section secondSection = sections.get(1);
        final Section newSection = new Section(
                secondSection.getDistance(),
                firstSection.getStart(),
                secondSection.getUpStation(),
                secondSection.getDownStation());

        sections.remove(secondSection);
        sections.add(0, newSection);
    }

    private Distance sumSectionsDistance(final List<Section> sections) {
        final Integer sum = sections.stream()
                .map(Section::getDistance)
                .map(Distance::getValue)
                .reduce(Integer::sum)
                .orElseThrow(() -> new IllegalStateException("구간의 거리 합을 구하던 도중 오류가 발생하였습니다."));

        return new Distance(sum);
    }

    public List<Station> collectAllStations() {
        final List<Station> stations = new ArrayList<>();

        stations.addAll(sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList()));

        stations.addAll(sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList()));

        return stations.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Section> getSections() {
        return new ArrayList<>(sections);
    }
}
