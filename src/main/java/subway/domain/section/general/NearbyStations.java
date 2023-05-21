package subway.domain.section.general;

import java.util.ArrayList;
import java.util.List;

import subway.domain.station.Station;

public class NearbyStations {


    private static final int UP_STATION_INDEX = 0;
    private static final int DOWN_STATION_INDEX = 1;
    private final List<Station> nearbyStations;

    private NearbyStations(final List<Station> nearbyStations) {
        this.nearbyStations = new ArrayList<>(nearbyStations);
    }

    public static NearbyStations createByUpStationAndDownStation(Station upStation, Station downStation) {
        return new NearbyStations(List.of(upStation, downStation));
    }


    public Station getUpStation() {
        return nearbyStations.get(UP_STATION_INDEX);
    }

    public Station getDownStation() {
        return nearbyStations.get(DOWN_STATION_INDEX);
    }

    @Override
    public String toString() {
        return "NearbyStations{" +
                "nearbyStations=" + nearbyStations +
                '}';
    }
}
