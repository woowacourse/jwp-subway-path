package subway.domain.interstation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.ToString;
import subway.domain.interstation.exception.InterStationsException;
import subway.domain.station.Station;

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

    public static InterStations of(final Station upStation, final Station downStation, final long distance) {
        return new InterStations(List.of(new InterStation(upStation, downStation, distance)));
    }

    /**
     * 상행선부터 하행선까지 순서대로 구간을 정렬한다
     */
    private List<InterStation> sort(final List<InterStation> interStations) {
        final Station firstStation = findFirstStation(interStations);

        final Map<Station, InterStation> upStationToInterStation = interStations.stream()
            .collect(Collectors.toMap(InterStation::getUpStation, interStation -> interStation));

        final List<InterStation> sortedInterStations = new ArrayList<>();
        Station currentStation = firstStation;
        while (upStationToInterStation.containsKey(currentStation)) {
            final InterStation interStation = upStationToInterStation.get(currentStation);
            sortedInterStations.add(interStation);
            currentStation = interStation.getDownStation();
        }

        return sortedInterStations;
    }

    private Station findFirstStation(final List<InterStation> interStations) {
        final Set<Station> downStations = findDownStations(interStations);
        final Set<Station> upStations = findUpStations(interStations);
        upStations.removeAll(downStations);
        if (upStations.size() != 1) {
            throw new InterStationsException("구간이 연결되어있지 않습니다.");
        }
        return upStations.stream().findFirst().get();
    }

    private Set<Station> findDownStations(final List<InterStation> interStations) {
        return interStations.stream()
            .map(InterStation::getDownStation)
            .collect(Collectors.toSet());
    }

    private Set<Station> findUpStations(final List<InterStation> interStations) {
        return interStations.stream()
            .map(InterStation::getUpStation)
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

    public void remove(final Station station) {
        if (isStartStation(station)) {
            interStations.remove(0);
            return;
        }
        if (isEndStation(station)) {
            interStations.remove(interStations.size() - 1);
            return;
        }
        removeMiddle(station);
    }

    private boolean isStartStation(final Station station) {
        return interStations.get(0).getUpStation().equals(station);
    }

    private boolean isEndStation(final Station station) {
        return interStations.get(interStations.size() - 1).getDownStation().equals(station);
    }

    private void removeMiddle(final Station station) {
        final int index = findDownStationIndex(station);
        final InterStation downInterStation = interStations.remove(index + 1);
        final InterStation upInterStation = interStations.remove(index);
        interStations.add(index, new InterStation(upInterStation.getUpStation(),
            downInterStation.getDownStation(),
            upInterStation.getDistance().add(downInterStation.getDistance())));
    }

    private int findDownStationIndex(final Station station) {
        for (int i = 0; i < interStations.size(); i++) {
            if (interStations.get(i).getDownStation().equals(station)) {
                return i;
            }
        }
        throw new InterStationsException("역이 존재하지 않습니다.");
    }
}
