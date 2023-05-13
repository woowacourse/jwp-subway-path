package subway.domain.line;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import subway.domain.interstation.InterStation;
import subway.domain.station.Station;
import subway.exception.BusinessException;

@Getter
@RequiredArgsConstructor
public class Line {

    private final Long id;
    private final String name;
    private final String color;
    private final List<InterStation> interStations;

    public Line(final String name, final String color) {
        this(null, name, color);
    }

    public Line(final Long id, final String name, final String color) {
        this(id, name, color, new ArrayList<>());
    }

    public void addInitialStation(final Station first, final Station second, final long distance) {
        validateEmpty();
        interStations.add(new InterStation(first, second, distance));
    }

    private void validateEmpty() {
        if (!interStations.isEmpty()) {
            throw new BusinessException("비어있지 않습니다");
        }
    }

    public void addStationEnd(final Station exist, final Station station, final long distance) {
        validateNotEmpty();
        final InterStation firstInterStation = interStations.get(0);
        final Station firstStation = firstInterStation.getUpStation();
        if (firstStation.equals(exist)) {
            interStations.add(0, new InterStation(station, firstStation, distance));
            return;
        }
        final InterStation lastInterStation = interStations.get(interStations.size() - 1);
        final Station lastStation = lastInterStation.getDownStation();
        if (lastStation.equals(exist)) {
            interStations.add(new InterStation(exist, station, distance));
            return;
        }
        throw new BusinessException("존재하지 않는 역입니다");
    }

    public void addStationBetween(final Station first, final Station second, final Station between,
                                  final long distance) {
        validateNotEmpty();
        final InterStation targetInterStation = interStations.stream()
            .filter(it -> it.getUpStation().equals(first))
            .filter(it -> it.getDownStation().equals(second))
            .findAny()
            .orElseThrow(() -> new BusinessException("존재하지 않는 구간입니다"));
        final int targetIndex = interStations.indexOf(targetInterStation);
        final Station firstStation = targetInterStation.getUpStation();
        final Station secondStation = targetInterStation.getDownStation();
        interStations.remove(targetIndex);
        interStations.add(targetIndex,
            new InterStation(between, secondStation, targetInterStation.getDistance().getValue() - distance));
        interStations.add(targetIndex, new InterStation(firstStation, between, distance));
    }

    private void validateNotEmpty() {
        if (interStations.isEmpty()) {
            throw new BusinessException("비어있는 라인입니다");
        }
    }

    public InterStation getFirstInterStation() {
        return interStations.get(0);
    }

    public void deleteStation(final Station existStation) {
        if (interStations.get(0).getUpStation().equals(existStation)) {
            interStations.remove(0);
            return;
        }
        if (interStations.get(interStations.size() - 1).getDownStation().equals(existStation)) {
            interStations.remove(interStations.size() - 1);
            return;
        }
        final Optional<InterStation> optionalInterStation = interStations.stream()
            .filter(it -> it.contains(existStation))
            .findFirst();
        if (optionalInterStation.isEmpty()) {
            return;
        }
        final InterStation interStation = optionalInterStation.get();
        final int index = interStations.indexOf(interStation);
        final InterStation nextInterStation = interStations.get(index + 1);
        interStations.remove(index);
        interStations.remove(index + 1);
        final long distance = interStation.getDistance().getValue() + nextInterStation.getDistance().getValue();
        if (distance <= 0) {
            throw new BusinessException("구간의 길이가 0보다 작습니다");
        }
        interStations.add(index, new InterStation(interStation.getUpStation(), nextInterStation.getDownStation(),
            distance));
    }

    public boolean isEmpty() {
        return interStations.isEmpty();
    }

    public List<Station> getStations() {
        final List<Station> stations = new ArrayList<>();
        for (final InterStation interStation : interStations) {
            stations.add(interStation.getUpStation());
        }
        stations.add(interStations.get(interStations.size() - 1).getDownStation());
        return stations;
    }
}
