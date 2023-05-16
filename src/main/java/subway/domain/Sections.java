package subway.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import subway.exception.DomainException;
import subway.exception.ExceptionType;

public class Sections {
    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = lineUp(sections);
    }

    public boolean hasNoSection() {
        return sections.isEmpty();
    }

    private LinkedList<Section> lineUp(List<Section> unorderedSections) {
        if (unorderedSections.isEmpty()) {
            return new LinkedList<>();
        }

        return getOrderedSections(unorderedSections);
    }

    private LinkedList<Section> getOrderedSections(List<Section> unorderedSections) {
        Map<Long, Section> sourceStations = new HashMap<>();
        Map<Long, Section> targetStations = new HashMap<>();

        for (Section section : unorderedSections) {
            sourceStations.put(section.getSourceStationId(), section);
            targetStations.put(section.getTargetStationId(), section);
        }

        Section source = findSource(sourceStations, targetStations);

        return lineUpSections(unorderedSections, sourceStations, source);
    }

    private Section findSource(Map<Long, Section> sourceStations, Map<Long, Section> targetStations) {
        HashSet<Section> sections = new HashSet<>(sourceStations.values());
        sections.removeAll(new HashSet<>(targetStations.values()));
        return sections.stream().findFirst().orElseThrow(() -> new DomainException(ExceptionType.NO_SOURCE));
    }

    private LinkedList<Section> lineUpSections(List<Section> unorderedSections, Map<Long, Section> sourceStations,
        Section source) {
        LinkedList<Section> linedUpSections = new LinkedList<>();

        while (linedUpSections.size() != unorderedSections.size()) {
            linedUpSections.add(source);
            source = sourceStations.get(source.getTargetStationId());
        }

        return linedUpSections;
    }

    public Long findFirstStationId() {
        return sections.get(0).getSourceStationId();
    }

    public Long findLastStationId() {
        return sections.get(sections.size() - 1).getTargetStationId();
    }

    public Section findSection(Long sourceStationId, Long targetStationId) {
        return sections.stream()
            .filter(section -> section.containsTheseStations(sourceStationId, targetStationId))
            .findFirst()
            .orElseThrow(() -> new DomainException(ExceptionType.NON_EXISTENT_SECTION));
    }

    public List<Section> findSectionsIncludeStation(Long stationId) {
        return sections.stream()
            .filter(section -> section.includeStation(stationId))
            .collect(Collectors.toList());
    }

    public boolean hasStation(Long stationId) {
        return sections.stream().anyMatch(section -> section.includeStation(stationId));
    }

    public List<Long> findOrderedStationIds() {
        List<Long> stationIds = new ArrayList<>();
        for (Section section : sections) {
            stationIds.add(section.getSourceStationId());
        }
        if (stationIds.isEmpty()) {
            return stationIds;
        }

        int lastStationIndex = sections.size() - 1;
        stationIds.add(sections.get(lastStationIndex).getTargetStationId());
        return stationIds;
    }
}
