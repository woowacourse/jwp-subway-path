package subway.domain;

import subway.exception.DuplicateStationInLineException;
import subway.exception.SectionNotFoundException;
import subway.exception.StationNotFoundException;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Line {

    private final LineName name;
    private final LinkedList<Section> sections;

    public Line(LineName name, List<Section> sections) {
        this.name = name;
        this.sections = new LinkedList<>(sections);
        addEmptyEndpoints(sections);
    }

    public Line(LineName name, Section section) {
        this.name = name;
        this.sections = new LinkedList<>();
        sections.add(section);
        addEmptyEndpoints(sections);
    }

    public Line(Line otherLine) {
        this(otherLine.getName(), otherLine.getSections());
    }

    private void addEmptyEndpoints(List<Section> sections) {
        Station upstreamEmptyEndpoint = sections.get(0).getUpstream();
        Station downstreamEmptyEndpoint = sections.get(sections.size() - 1).getDownstream();

        this.sections.addFirst(new Section(Station.getEmptyEndpoint(), upstreamEmptyEndpoint, Integer.MAX_VALUE));
        this.sections.addLast(new Section(downstreamEmptyEndpoint, Station.getEmptyEndpoint(), Integer.MAX_VALUE));
    }

    public List<Section> addStation(Station newStation, Station upstream, Station downstream, int distanceToUpstream) {
        validateDuplicateStations(newStation);

        Section correspondingSection = findCorrespondingSection(upstream, downstream);
        List<Section> sectionsToAdd = correspondingSection.insertInTheMiddle(newStation, distanceToUpstream);
        addSections(correspondingSection, sectionsToAdd);

        return sectionsToAdd;
    }

    private void validateDuplicateStations(Station newStation) {
        if (hasStation(newStation)) {
            throw new DuplicateStationInLineException("노선에 이미 존재하는 역입니다.");
        }
    }

    private Section findCorrespondingSection(Station upstream, Station downstream) {
        return sections.stream()
                .filter(section -> section.isCorrespondingSection(upstream, downstream))
                .findAny()
                .orElseThrow(() -> new SectionNotFoundException("노선에 해당하는 구간이 존재하지 않습니다."));
    }

    private void addSections(Section correspondingSection, List<Section> sectionsToAdd) {
        sections.add(sections.indexOf(correspondingSection), sectionsToAdd.get(1));
        sections.add(sections.indexOf(sectionsToAdd.get(1)), sectionsToAdd.get(0));

        sections.remove(correspondingSection);
    }

    public void deleteStation(Station stationToDelete) {
        validateStationExist(stationToDelete);
        List<Section> sectionsToMerge = findSectionsToMerge(stationToDelete);
        Section mergedSection = sectionsToMerge.get(0).merge(sectionsToMerge.get(1));

        deleteSections(sectionsToMerge, mergedSection);
    }

    private void validateStationExist(Station stationToDelete) {
        if (!hasStation(stationToDelete)) {
            throw new StationNotFoundException("노선에 존재하지 않는 역입니다.");
        }
    }

    private boolean hasStation(Station newStation) {
        return sections.stream()
                .anyMatch(section -> section.contains(newStation));
    }

    private List<Section> findSectionsToMerge(Station stationToDelete) {
        return sections.stream()
                .filter(section -> section.contains(stationToDelete))
                .collect(Collectors.toList());
    }

    private void deleteSections(List<Section> sectionsToMerge, Section mergedSection) {
        sections.add(sections.indexOf(sectionsToMerge.get(0)), mergedSection);

        sections.remove(sectionsToMerge.get(0));
        sections.remove(sectionsToMerge.get(1));
    }

    public LineName getName() {
        return name;
    }

    public List<Section> getSections() {
        return new LinkedList<>(sections.subList(1, sections.size() - 1));
    }

    public List<String> getStationNamesInOrder() {
        return sections.subList(0, sections.size() - 1).stream()
                .map(section -> section.getDownstream().getName())
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Line{" +
                "name=" + name +
                ", sections=" + sections +
                '}';
    }
}
