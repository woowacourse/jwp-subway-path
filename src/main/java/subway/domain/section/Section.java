package subway.domain.section;

import subway.domain.section.general.NearbyStations;
import subway.domain.station.Station;

public abstract class Section {

    private final NearbyStations nearbyStations;

    public Section(final NearbyStations nearbyStations) {
        this.nearbyStations = nearbyStations;
    }

    public boolean isSameDownStationName(String downStationName) {
        return nearbyStations.getDownStation().isSameStationName(downStationName);
    }

    public boolean isSameUpStationName(String upStationName) {
        return nearbyStations.getUpStation().isSameStationName(upStationName);
    }

    public abstract boolean isSameLineId(Long lineId);

    public abstract int getDistance();

    public abstract boolean isTransferSection();

    public Station getUpStation() {
        return nearbyStations.getUpStation();
    }

    public Station getDownStation() {
        return nearbyStations.getDownStation();
    }

    public Long getUpStationId() {
        return nearbyStations.getUpStation().getId();
    }

    public Long getDownStationId() {
        return nearbyStations.getDownStation().getId();
    }

    public NearbyStations getNearbyStations() {
        return nearbyStations;
    }

    @Override
    public String toString() {
        return "Section{" +
                "nearbyStations=" + nearbyStations +
                '}';
    }
}
