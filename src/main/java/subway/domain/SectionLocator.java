package subway.domain;

import subway.entity.SectionEntity;

import java.util.*;

public class SectionLocator {

    private final Map<Long, List<SectionEntity>> graph;

    private SectionLocator(final Map<Long, List<SectionEntity>> graph) {
        this.graph = graph;
    }

    public static SectionLocator of(final List<SectionEntity> sectionDetails) {
        Map<Long, List<SectionEntity>> stationGraph = new HashMap<>();
        for (final SectionEntity sectionDetailEntity : sectionDetails) {
            stationGraph.computeIfAbsent(sectionDetailEntity.getUpStationId(), key -> new ArrayList<>()).add(sectionDetailEntity);
            stationGraph.computeIfAbsent(sectionDetailEntity.getDownStationId(), key -> new ArrayList<>()).add(sectionDetailEntity);
        }
        return new SectionLocator(stationGraph);
    }

    public Long findStartStation() {
        Long station = graph.keySet().iterator().next();

        final Queue<Long> queue = new LinkedList<>();
        queue.add(station);
        while (!queue.isEmpty()) {
            final Long poll = queue.poll();
            final List<SectionEntity> sectionDetailEntities = graph.get(poll);
            for (final SectionEntity sectionDetailEntity : sectionDetailEntities) {
                if (sectionDetailEntity.getDownStationId().equals(poll)) {
                    queue.add(sectionDetailEntity.getUpStationId());
                    station = sectionDetailEntity.getUpStationId();
                }
            }
        }
        return station;
    }

    public Long findEndStation() {
        Long station = graph.keySet().iterator().next();

        final Queue<Long> queue = new LinkedList<>();
        queue.add(station);
        while (!queue.isEmpty()) {
            final Long poll = queue.poll();
            final List<SectionEntity> sectionDetailEntities = graph.get(poll);
            for (final SectionEntity sectionDetailEntity : sectionDetailEntities) {
                if (sectionDetailEntity.getUpStationId().equals(poll)) {
                    queue.add(sectionDetailEntity.getDownStationId());
                    station = sectionDetailEntity.getDownStationId();
                }
            }
        }
        return station;
    }
}
