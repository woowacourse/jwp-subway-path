package subway.domain.section;

import subway.domain.section.general.NearbyStations;
import subway.domain.station.Station;

public abstract class Section {

    private final Long id;
    private final NearbyStations nearbyStations;

    public Section(final Long id, final NearbyStations nearbyStations) {
        this.id = id;
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

    public Long getId() {
        return id;
    }

    public NearbyStations getNearbyStations() {
        return nearbyStations;
    }
}
