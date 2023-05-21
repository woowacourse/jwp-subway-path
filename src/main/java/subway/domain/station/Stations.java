package subway.domain.station;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import subway.domain.line.Line;
import subway.domain.section.general.NearbyStations;
import subway.domain.section.transfer.TransferSection;
import subway.exception.StationNotFoundException;

public class Stations {

    private static final int MIN_TRANSFER_STATION_COUNT = 2;
    private static final int ALL_SAME_LINE_COUNT = 1;

    private final List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = new ArrayList<>(stations);
    }

    public StationDirection determineStationDirectionBy(String upStationName, String downStationName) {
        Optional<Station> findUpStation = findStationByStationName(upStationName);
        Optional<Station> findDownStation = findStationByStationName(downStationName);

        validateStationStatus(findUpStation, findDownStation);

        if (findUpStation.isEmpty()) {
            return StationDirection.UP;
        }
        return StationDirection.DOWN;
    }

    public Optional<Station> findStationByStationName(String stationName) {
        return stations.stream()
                .filter(station -> station.getName().equals(stationName))
                .findFirst();
    }

    private void validateStationStatus(Optional<Station> requestUpStation, Optional<Station> requestDownStation) {
        if (requestUpStation.isEmpty() && requestDownStation.isEmpty()) {
            throw new StationNotFoundException("두개의 역 모두가 존재하지 않습니다.");
        }

        if (requestUpStation.isPresent() && requestDownStation.isPresent()) {
            throw new IllegalArgumentException("두개의 역이 이미 모두 존재합니다.");
        }
    }

    public Line getLineWhenAllSameLine() {
        long lineCount = stations.stream()
                .map(Station::getLine)
                .map(Line::getName)
                .distinct()
                .count();
        if (lineCount > ALL_SAME_LINE_COUNT) {
            throw new IllegalStateException("역 목록에서 가져올 노선이 여러 개가 존재합니다.");
        }
        Station station = stations.get(0);
        return station.getLine();
    }

    public List<TransferSection> getTransferSections() {
        List<Station> transferStations = stations.stream()
                .filter(this::isTransferStation)
                .sorted(Comparator.comparing(Station::getName))
                .collect(Collectors.toUnmodifiableList());

        List<TransferSection> transferSections = new ArrayList<>();
        for (int generateIdx = 0; generateIdx < transferStations.size() - 1; generateIdx += 2) {
            Station upStation = transferStations.get(generateIdx);
            Station downStation = transferStations.get(generateIdx + 1);
            NearbyStations nearbyStations = NearbyStations.createByUpStationAndDownStation(upStation, downStation);
            transferSections.add(new TransferSection(nearbyStations));
        }
        return transferSections;
    }

    private boolean isTransferStation(Station targetStation) {
        long count = stations.stream()
                .filter(station -> station.isSameStationName(targetStation.getName()))
                .count();
        return count >= MIN_TRANSFER_STATION_COUNT;
    }
}
