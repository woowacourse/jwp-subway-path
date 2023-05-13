package subway.domain;

import subway.exception.DuplicateStationInLineException;
import subway.exception.SectionNotFoundException;
import subway.exception.StationNotFoundException;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Line {

    private final LineName name;
    private final LinkedList<AbstractSection> sections;

    public Line(String name, List<MiddleSection> sections) {
        this.name = new LineName(name);
        this.sections = new LinkedList<>(sections);
        addTerminalSections();
    }

    public Line(Line otherLine) {
        this(otherLine.getName(), otherLine.getSections());
    }

    private void addTerminalSections() {
        sections.addFirst(new UpstreamTerminalSection(getUpstreamTerminal()));
        sections.addLast(new DownstreamTerminalSection(getDownstreamTerminal()));
    }

    private Station getUpstreamTerminal() {
        return sections.get(0).getUpstream();
    }

    private Station getDownstreamTerminal() {
        return sections.get(sections.size() - 1).getDownstream();
    }

    public void addStation(Station stationToAdd, Station upstream, Station downstream, int distanceToUpstream) {
        validateStationNotExist(stationToAdd);

        final AbstractSection correspondingSection = findCorrespondingSection(upstream, downstream);
        final List<AbstractSection> sectionsToAdd = correspondingSection.insertInTheMiddle(stationToAdd, distanceToUpstream);
        addStation(correspondingSection, sectionsToAdd);
    }

    private void validateStationNotExist(Station stationToAdd) {
        if (isStationExist(stationToAdd)) {
            throw new DuplicateStationInLineException("노선에 이미 존재하는 역입니다.");
        }
    }

    private AbstractSection findCorrespondingSection(Station upstream, Station downstream) {
        return sections.stream()
                       .filter(section -> section.isCorrespondingSection(upstream, downstream))
                       .findAny()
                       .orElseThrow(() -> new SectionNotFoundException("노선에 해당하는 구간이 존재하지 않습니다."));
    }

    private void addStation(AbstractSection sectionToDelete, List<AbstractSection> sectionsToAdd) {
        final int indexToAdd = sections.indexOf(sectionToDelete);
        sections.add(indexToAdd, sectionsToAdd.get(1));
        sections.add(indexToAdd, sectionsToAdd.get(0));

        sections.remove(sectionToDelete);
    }

    public void deleteStation(Station stationToDelete) {
        validateStationExist(stationToDelete);

        List<AbstractSection> sectionsToMerge = findSectionsToMerge(stationToDelete);
        mergeSections(sectionsToMerge);
    }

    private void validateStationExist(Station stationToDelete) {
        if (!isStationExist(stationToDelete)) {
            throw new StationNotFoundException("노선에 존재하지 않는 역입니다.");
        }
    }

    private List<AbstractSection> findSectionsToMerge(Station stationToDelete) {
        if (sections.getLast().contains(stationToDelete)) {
            return List.of(sections.getLast(), sections.get(sections.size() - 2));
        }
        return sections.stream()
                       .filter(section -> section.contains(stationToDelete))
                       .collect(Collectors.toList());
    }

    private void mergeSections(List<AbstractSection> sectionsToMerge) {
        final AbstractSection section = sectionsToMerge.get(0);
        final AbstractSection sectionToMerge = sectionsToMerge.get(1);
        final AbstractSection mergedSection = section.merge(sectionToMerge);

        sections.add(sections.indexOf(section), mergedSection);

        sections.remove(section);
        sections.remove(sectionToMerge);
    }

    private boolean isStationExist(Station station) {
        return sections.stream()
                       .anyMatch(section -> section.contains(station));
    }

    public String getName() {
        return name.getName();
    }

    public List<MiddleSection> getSections() {
        return sections.subList(1, sections.size() - 1)
                       .stream()
                       .map(section -> (MiddleSection) section)
                       .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(name, line.name) && Objects.equals(sections, line.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, sections);
    }

    @Override
    public String toString() {
        return "Line{" +
                "name=" + name +
                ", sections=" + sections +
                '}';
    }
}
