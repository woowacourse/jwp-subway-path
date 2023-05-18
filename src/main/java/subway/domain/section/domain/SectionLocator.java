package subway.domain.section.domain;

import subway.domain.section.entity.SectionDetailEntity;
import subway.domain.station.entity.StationEntity;

import java.util.*;

public class SectionLocator {

    private final Map<StationEntity, List<SectionDetailEntity>> graph;

    private SectionLocator(final Map<StationEntity, List<SectionDetailEntity>> graph) {
        this.graph = graph;
    }

    public static SectionLocator of(final List<SectionDetailEntity> sectionDetails) {
        Map<StationEntity, List<SectionDetailEntity>> stationGraph = new HashMap<>();
        for (final SectionDetailEntity sectionDetailEntity : sectionDetails) {
            stationGraph.computeIfAbsent(sectionDetailEntity.getUpStation(), key -> new ArrayList<>()).add(sectionDetailEntity);
            stationGraph.computeIfAbsent(sectionDetailEntity.getDownStation(), key -> new ArrayList<>()).add(sectionDetailEntity);
        }
        return new SectionLocator(stationGraph);
    }

    public StationEntity findStartStation() {
        StationEntity station = graph.keySet().iterator().next();

        final Queue<StationEntity> queue = new LinkedList<>();
        queue.add(station);
        while (!queue.isEmpty()) {
            final StationEntity poll = queue.poll();
            final List<SectionDetailEntity> sectionDetailEntities = graph.get(poll);
            for (final SectionDetailEntity sectionDetailEntity : sectionDetailEntities) {
                if (sectionDetailEntity.getDownStation().equals(poll)) {
                    queue.add(sectionDetailEntity.getUpStation());
                    station = sectionDetailEntity.getUpStation();
                }
            }
        }
        return station;
    }

    public StationEntity findEndStation() {
        StationEntity station = graph.keySet().iterator().next();

        final Queue<StationEntity> queue = new LinkedList<>();
        queue.add(station);
        while (!queue.isEmpty()) {
            final StationEntity poll = queue.poll();
            final List<SectionDetailEntity> sectionDetailEntities = graph.get(poll);
            for (final SectionDetailEntity sectionDetailEntity : sectionDetailEntities) {
                if (sectionDetailEntity.getUpStation().equals(poll)) {
                    queue.add(sectionDetailEntity.getDownStation());
                    station = sectionDetailEntity.getDownStation();
                }
            }
        }
        return station;
    }
}
