package subway.domain.interstation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.ToString;
import subway.domain.interstation.exception.InterStationsException;

/**
 * 구간들은 상행선부터, 하행선까지 순서대로 정렬되어있다.
 */
@Getter
@ToString
public class InterStations {

    private List<InterStation> interStations;

    public InterStations(final List<InterStation> interStations) {
        validate(interStations);
        this.interStations = sort(interStations);
    }

    public static InterStations of(final Long upStationId, final Long downStationId, final long distance) {
        return new InterStations(List.of(new InterStation(upStationId, downStationId, distance)));
    }

    /**
     * 상행선부터 하행선까지 순서대로 구간을 정렬한다
     */
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

    private void validate(final List<InterStation> interStations) {
        if (interStations == null || interStations.isEmpty()) {
            throw new InterStationsException("구간이 비어있습니다.");
        }
        if (interStations.stream().distinct().count() != interStations.size()) {
            throw new InterStationsException("구간이 중복되었습니다.");
        }
    }

    public void add(final InterStation interStation) {
        interStations.add(interStation);
        validate(interStations);
        interStations = sort(interStations);
    }

    public void remove(final long stationId) {
        if (isStartStation(stationId)) {
            interStations.remove(0);
            return;
        }
        if (isEndStation(stationId)) {
            interStations.remove(interStations.size() - 1);
            return;
        }
        removeMiddle(stationId);
    }

    private boolean isStartStation(final long stationId) {
        return interStations.get(0).getUpStationId().equals(stationId);
    }

    private boolean isEndStation(final long stationId) {
        return interStations.get(interStations.size() - 1).getDownStationId().equals(stationId);
    }

    private void removeMiddle(final long stationId) {
        final int index = findDownStationIndex(stationId);
        final InterStation downInterStation = interStations.remove(index + 1);
        final InterStation upInterStation = interStations.remove(index);
        interStations.add(index, new InterStation(upInterStation.getUpStationId(),
            downInterStation.getDownStationId(),
            upInterStation.getDistance().add(downInterStation.getDistance())));
    }

    private int findDownStationIndex(final long stationId) {
        for (int i = 0; i < interStations.size(); i++) {
            if (interStations.get(i).getDownStationId().equals(stationId)) {
                return i;
            }
        }
        throw new InterStationsException("역이 존재하지 않습니다.");
    }

    public boolean isEmpty() {
        return interStations.isEmpty();
    }

    public InterStation getFirstInterStation() {
        return interStations.get(0);
    }

    public List<Long> getAllStations() {
        final List<Long> stations = new ArrayList<>();
        stations.add(interStations.get(0).getUpStationId());
        for (final InterStation interStation : interStations) {
            stations.add(interStation.getDownStationId());
        }
        return stations;
    }
}
