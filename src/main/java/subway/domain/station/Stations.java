package subway.domain.station;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import subway.domain.section.general.NearbyStations;
import subway.domain.section.transfer.TransferSection;

public class Stations {

    private static final int MIN_TRANSFER_STATION_COUNT = 2;

    private final List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = new ArrayList<>(stations);
    }

    public Optional<Station> findStationByStationName(String stationName) {
        return stations.stream()
                .filter(station -> station.getName().equals(stationName))
                .findFirst();
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
