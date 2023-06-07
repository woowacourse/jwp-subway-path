package subway.line.domain.interstation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import subway.line.domain.interstation.add.AddInterStationPolicy;
import subway.line.domain.interstation.exception.InterStationsException;
import subway.line.domain.interstation.remove.RemoveInterStationPolicy;

public class InterStations {

    private final List<InterStation> interStations;

    public InterStations(List<InterStation> interStations) {
        validate(interStations);
        this.interStations = sort(interStations);
    }

    public static InterStations of(Long upStationId, Long downStationId, long distance) {
        return new InterStations(List.of(new InterStation(upStationId, downStationId, distance)));
    }

    private void validate(List<InterStation> interStations) {
        if (interStations == null || interStations.isEmpty()) {
            throw new InterStationsException("구간이 비어있습니다.");
        }
        if (interStations.stream().distinct().count() != interStations.size()) {
            throw new InterStationsException("구간이 중복되었습니다.");
        }
    }

    private List<InterStation> sort(List<InterStation> interStations) {
        long firstStation = findFirstStation(interStations);

        Map<Long, InterStation> upStationToInterStation = interStations.stream()
                .collect(Collectors.toMap(InterStation::getUpStationId, interStation -> interStation));

        return toSortedStations(firstStation, upStationToInterStation);
    }

    private List<InterStation> toSortedStations(long firstStation,
            Map<Long, InterStation> upStationToInterStation) {
        List<InterStation> sortedInterStations = new ArrayList<>();
        long currentStation = firstStation;
        while (upStationToInterStation.containsKey(currentStation)) {
            InterStation interStation = upStationToInterStation.get(currentStation);
            sortedInterStations.add(interStation);
            currentStation = interStation.getDownStationId();
        }
        return sortedInterStations;
    }

    private Long findFirstStation(List<InterStation> interStations) {
        Set<Long> downStations = findDownStations(interStations);
        Set<Long> upStations = findUpStations(interStations);
        upStations.removeAll(downStations);
        if (upStations.size() != 1) {
            throw new InterStationsException("구간이 연결되어있지 않습니다.");
        }
        return upStations.stream().findFirst().get();
    }

    private Set<Long> findDownStations(List<InterStation> interStations) {
        return interStations.stream()
                .map(InterStation::getDownStationId)
                .collect(Collectors.toSet());
    }

    private Set<Long> findUpStations(List<InterStation> interStations) {
        return interStations.stream()
                .map(InterStation::getUpStationId)
                .collect(Collectors.toSet());
    }

    public void add(Long upStationId, Long downStationId, Long newStationId, long distance) {
        AddInterStationPolicy policy = AddInterStationPolicy.of(interStations,
                upStationId,
                downStationId,
                newStationId);
        policy.addInterStation(interStations, upStationId, downStationId, newStationId, distance);
    }

    public void remove(long stationId) {
        RemoveInterStationPolicy removeStrategy = RemoveInterStationPolicy.of(interStations, stationId);
        removeStrategy.removeInterStation(interStations, stationId);
    }

    public boolean isEmpty() {
        return interStations.isEmpty();
    }

    public List<Long> getAllStations() {
        List<Long> stations = new ArrayList<>();
        stations.add(interStations.get(0).getUpStationId());
        for (InterStation interStation : interStations) {
            stations.add(interStation.getDownStationId());
        }
        return stations;
    }

    public List<InterStation> getInterStations() {
        return interStations;
    }
}
