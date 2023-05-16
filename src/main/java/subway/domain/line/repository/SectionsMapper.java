package subway.domain.line.repository;

import org.springframework.stereotype.Component;
import subway.domain.section.entity.SectionDetailEntity;
import subway.domain.station.entity.StationEntity;

import java.util.*;

@Component
public class SectionsMapper {

    public List<StationEntity> sectionsToStations(
            List<SectionDetailEntity> sectionDetailEntityEntities
    ){
        Map<StationEntity, List<SectionDetailEntity>> stationGraph = sectionsToMap(sectionDetailEntityEntities);
        StationEntity firstStation = findFirstStation(stationGraph);
        return stationGraphToSortedList(stationGraph, firstStation);
    }

    private Map<StationEntity, List<SectionDetailEntity>> sectionsToMap(final List<SectionDetailEntity> sectionDetailEntities) {
        Map<StationEntity, List<SectionDetailEntity>> stationGraph = new HashMap<>();
        for (final SectionDetailEntity sectionDetailEntity : sectionDetailEntities) {
            stationGraph.computeIfAbsent(sectionDetailEntity.getUpStation(), key -> new ArrayList<>()).add(sectionDetailEntity);
            stationGraph.computeIfAbsent(sectionDetailEntity.getDownStation(), key -> new ArrayList<>()).add(sectionDetailEntity);
        }
        return stationGraph;
    }

    private StationEntity findFirstStation(Map<StationEntity, List<SectionDetailEntity>> stationGraph) {
        StationEntity firstStation = stationGraph.keySet().iterator().next();

        final Queue<StationEntity> queue = new LinkedList<>();
        queue.add(firstStation);
        while (!queue.isEmpty()) {
            final StationEntity poll = queue.poll();
            final List<SectionDetailEntity> sectionDetailEntities = stationGraph.get(poll);
            for (final SectionDetailEntity sectionDetailEntity : sectionDetailEntities) {
                if (sectionDetailEntity.getDownStation().equals(poll)) {
                    queue.add(sectionDetailEntity.getUpStation());
                    firstStation = sectionDetailEntity.getUpStation();
                }
            }
        }
        return firstStation;
    }

    private List<StationEntity> stationGraphToSortedList(Map<StationEntity, List<SectionDetailEntity>> stationGraph, StationEntity firstStation) {
        final List<StationEntity> result = new ArrayList<>();
        final Queue<StationEntity> queue = new LinkedList<>();

        queue.add(firstStation);
        result.add(firstStation);
        while (!queue.isEmpty()) {
            final StationEntity stationEntity = queue.poll();
            final List<SectionDetailEntity> sectionDetailEntities = stationGraph.get(stationEntity);

            for (final SectionDetailEntity sectionDetailEntity : sectionDetailEntities) {
                if (sectionDetailEntity.getUpStation().equals(stationEntity)) {
                    StationEntity downStationEntity = sectionDetailEntity.getDownStation();
                    queue.add(downStationEntity);
                    result.add(downStationEntity);
                }
            }
        }
        return result;
    }
}
