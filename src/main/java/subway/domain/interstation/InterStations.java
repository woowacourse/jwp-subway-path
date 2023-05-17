package subway.domain.interstation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    private final List<InterStation> interStations;

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

    public void add(final Long upStationId, final Long downStationId, final Long newStationId, final long distance) {
        validate(upStationId, downStationId, newStationId);
        if (downStationId == null) {
            addLast(new InterStation(upStationId, newStationId, distance));
            return;
        }
        if (upStationId == null) {
            addFirst(new InterStation(newStationId, downStationId, distance));
            return;
        }
        addMiddle(upStationId, downStationId, newStationId, distance);
    }

    private void validate(final Long upStationId,
                          final Long downStationId,
                          final Long newStationId) {
        validateEmptyInterStation(upStationId, downStationId);
        validateDuplicateInterStation(upStationId, downStationId, newStationId);

    }

    private void validateDuplicateInterStation(final Long upStationId,
                                               final Long downStationId,
                                               final Long newStationId) {
        interStations.stream()
            .filter(it -> it.getUpStationId().equals(upStationId) && it.getDownStationId().equals(newStationId))
            .findFirst()
            .ifPresent(it -> {
                throw new InterStationsException("구간이 중복되었습니다.");
            });
        interStations.stream()
            .filter(it -> it.getUpStationId().equals(downStationId) && it.getDownStationId().equals(newStationId))
            .findFirst()
            .ifPresent(it -> {
                throw new InterStationsException("구간이 중복되었습니다.");
            });
    }

    private void validateEmptyInterStation(final Long upStationId, final Long downStationId) {
        if (upStationId == null && downStationId == null) {
            throw new InterStationsException("구간이 비어있습니다.");
        }
    }

    private void addMiddle(final Long upStationId,
                           final Long downStationId,
                           final Long newStationId,
                           final long distance) {
        final int index = findUpStationIndex(upStationId);
        final InterStation removedInterStation = interStations.remove(index);
        interStations.add(index, new InterStation(upStationId, newStationId, distance));
        interStations.add(index + 1,
            new InterStation(newStationId, downStationId, removedInterStation.getDistance().minus(distance)));
    }

    private void addFirst(final InterStation interStation) {
        if (!Objects.equals(interStation.getDownStationId(), interStations.get(0).getUpStationId())) {
            throw new InterStationsException("구간이 연결되어있지 않습니다.");
        }
        interStations.add(0, new InterStation(interStation.getDownStationId(),
            interStation.getUpStationId(),
            interStation.getDistance()));
    }

    private void addLast(final InterStation interStation) {
        if (!Objects.equals(interStation.getUpStationId(),
            interStations.get(interStations.size() - 1).getDownStationId())) {
            throw new InterStationsException("구간이 연결되어있지 않습니다.");
        }
        interStations.add(interStation);
    }

    private int findUpStationIndex(final Long upStationId) {
        for (int i = 0; i < interStations.size(); i++) {
            if (interStations.get(i).getUpStationId().equals(upStationId)) {
                return i;
            }
        }
        throw new InterStationsException("구간이 연결되어있지 않습니다.");
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

    public List<Long> getAllStations() {
        final List<Long> stations = new ArrayList<>();
        stations.add(interStations.get(0).getUpStationId());
        for (final InterStation interStation : interStations) {
            stations.add(interStation.getDownStationId());
        }
        return stations;
    }
}
