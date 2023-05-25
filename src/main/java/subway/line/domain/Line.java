package subway.line.domain;

import subway.line.exception.DuplicateStationInLineException;
import subway.line.exception.SectionNotFoundException;
import subway.station.domain.Station;
import subway.station.exception.StationNotFoundException;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Line {

    private final Long id;
    private final LineMetadata lineMetadata;
    private final LinkedList<AbstractSection> sections;

    public Line(Long id, String name, int additionalFare, List<MiddleSection> sections) {
        this.id = id;
        this.lineMetadata = new LineMetadata(name, additionalFare);
        this.sections = new LinkedList<>(sections);
        addTerminalSections();
    }

    public Line(String name, int additionalFare, List<MiddleSection> sections) {
        this(null, name, additionalFare, sections);
    }

    public Line(Line otherLine) {
        this(otherLine.getId(), otherLine.getName(), otherLine.getAdditionalFare(), otherLine.getSections());
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

    public void addStation(Station stationToAdd, long upstreamId, long downstreamId, int distanceToUpstream) {
        validateStationNotExist(stationToAdd);

        final AbstractSection correspondingSection = findCorrespondingSection(upstreamId, downstreamId);
        final List<AbstractSection> sectionsToAdd = correspondingSection.insertInTheMiddle(stationToAdd, distanceToUpstream);
        addStation(correspondingSection, sectionsToAdd);
    }

    private void validateStationNotExist(Station stationToAdd) {
        if (isStationExist(stationToAdd)) {
            throw new DuplicateStationInLineException("노선에 이미 존재하는 역입니다.");
        }
    }

    private AbstractSection findCorrespondingSection(long upstreamId, long downstreamId) {
        return sections.stream()
                       .filter(section -> section.isCorrespondingSection(upstreamId, downstreamId))
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

        if (areOnlyTwoStationsInLine()) {
            sections.removeIf(section -> section.getClass() == MiddleSection.class);
            return;
        }
        List<AbstractSection> sectionsToMerge = findSectionsToMerge(stationToDelete);
        mergeSections(sectionsToMerge);
    }

    private boolean areOnlyTwoStationsInLine() {
        return getSections().size() == 1;
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

    public boolean isLineEmpty() {
        return getSections().size() == 0;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return lineMetadata.getName();
    }

    public int getAdditionalFare() {
        return lineMetadata.getAdditionalFare();
    }

    public LineMetadata getLineInfo() {
        return lineMetadata;
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
        if (Objects.isNull(id) || Objects.isNull(line.id)) {
            return Objects.equals(lineMetadata, line.lineMetadata) && Objects.equals(sections, line.sections);
        }
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        if (Objects.isNull(id)) {
            return Objects.hash(lineMetadata, sections);
        }
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", lineMetadata=" + lineMetadata +
                ", sections=" + sections +
                '}';
    }
}
