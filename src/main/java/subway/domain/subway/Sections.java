package subway.domain.subway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import subway.domain.exception.DomainException;
import subway.domain.exception.ExceptionType;

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
        Map<Long, Section> sourceIdsToSections = new HashMap<>();
        Map<Long, Section> targetIdsToSections = new HashMap<>();

        for (Section section : unorderedSections) {
            sourceIdsToSections.put(section.getSourceStationId(), section);
            targetIdsToSections.put(section.getTargetStationId(), section);
        }

        Long upLineLastStopId = getUpLineLastStopId(sourceIdsToSections, targetIdsToSections);
        return lineUpSections(unorderedSections, sourceIdsToSections, upLineLastStopId);
    }

    private Long getUpLineLastStopId(Map<Long, Section> sourceIdsToSections, Map<Long, Section> targetIdsToSections) {
        Set<Long> sourceIds = new HashSet<>(sourceIdsToSections.keySet());
        Set<Long> targetIds = new HashSet<>(targetIdsToSections.keySet());

        sourceIds.removeAll(targetIds);

        return sourceIds.stream()
            .findFirst()
            .orElseThrow(() -> new DomainException(ExceptionType.NO_SOURCE));
    }

    private LinkedList<Section> lineUpSections(List<Section> unorderedSections, Map<Long, Section> sourceIdsToSections,
        Long upLineLastStopId) {
        LinkedList<Section> linedUpSections = new LinkedList<>();

        Section persistSection = sourceIdsToSections.get(upLineLastStopId);

        while (linedUpSections.size() != unorderedSections.size()) {
            linedUpSections.add(persistSection);
            persistSection = sourceIdsToSections.get(persistSection.getTargetStationId());
        }

        return linedUpSections;
    }

    public Long findFirstStationId() {
        return sections.get(0).getSourceStationId();
    }

    public Long findLastStationId() {
        int lastStationIndex = sections.size() - 1;
        return sections.get(lastStationIndex).getTargetStationId();
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
            .collect(Collectors.toUnmodifiableList());
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
