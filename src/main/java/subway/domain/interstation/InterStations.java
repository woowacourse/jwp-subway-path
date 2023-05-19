package subway.domain.interstation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import subway.domain.interstation.add.AddInterStationPolicy;
import subway.domain.interstation.add.AddInterStationStrategy;
import subway.domain.interstation.exception.InterStationsException;
import subway.domain.interstation.remove.RemoveInterStationPolicy;
import subway.domain.interstation.remove.RemoveInterStationStrategy;

public class InterStations {

    private final List<InterStation> interStations;

    public InterStations(final List<InterStation> interStations) {
        validate(interStations);
        this.interStations = sort(interStations);
    }

    public static InterStations of(final Long upStationId, final Long downStationId, final long distance) {
        return new InterStations(List.of(new InterStation(upStationId, downStationId, distance)));
    }

    private void validate(final List<InterStation> interStations) {
        if (interStations == null || interStations.isEmpty()) {
            throw new InterStationsException("구간이 비어있습니다.");
        }
        if (interStations.stream().distinct().count() != interStations.size()) {
            throw new InterStationsException("구간이 중복되었습니다.");
        }
    }

    private List<InterStation> sort(final List<InterStation> interStations) {
        final long firstStation = findFirstStation(interStations);

        final Map<Long, InterStation> upStationToInterStation = interStations.stream()
                .collect(Collectors.toMap(InterStation::getUpStationId, interStation -> interStation));

        final List<InterStation> sortedInterStations = new ArrayList<>();
        long currentStation = firstStation;
        while (upStationToInterStation.containsKey(currentStation)) {
            final InterStation interStation = upStationToInterStation.get(currentStation);
            sortedInterStations.add(interStation);
            currentStation = interStation.getDownStationId();
        }

        return sortedInterStations;
    }

    private Long findFirstStation(final List<InterStation> interStations) {
        final Set<Long> downStations = findDownStations(interStations);
        final Set<Long> upStations = findUpStations(interStations);
        upStations.removeAll(downStations);
        if (upStations.size() != 1) {
            throw new InterStationsException("구간이 연결되어있지 않습니다.");
        }
        return upStations.stream().findFirst().get();
    }

    private Set<Long> findDownStations(final List<InterStation> interStations) {
        return interStations.stream()
                .map(InterStation::getDownStationId)
                .collect(Collectors.toSet());
    }

    private Set<Long> findUpStations(final List<InterStation> interStations) {
        return interStations.stream()
                .map(InterStation::getUpStationId)
                .collect(Collectors.toSet());
    }

    public void add(final Long upStationId, final Long downStationId, final Long newStationId, final long distance) {
        final AddInterStationStrategy strategy = AddInterStationPolicy.of(interStations, upStationId, downStationId,
                newStationId);
        strategy.addInterStation(interStations, upStationId, downStationId, newStationId, distance);
    }

    public void remove(final long stationId) {
        final RemoveInterStationStrategy removeStrategy = RemoveInterStationPolicy.of(interStations, stationId);
        removeStrategy.removeInterStation(interStations, stationId);
    }

    public boolean isEmpty() {
        return interStations.isEmpty();
    }

    public List<Long> getAllStations() {
        final List<Long> stations = new ArrayList<>();
        stations.add(interStations.get(0).getUpStationId());
        for (final InterStation interStation : interStations) {
            stations.add(interStation.getDownStationId());
        }
        return stations;
    }

    public List<InterStation> getInterStations() {
        return interStations;
    }
}
