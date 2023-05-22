package subway.dto;

import subway.domain.station.Station;

import java.util.List;

public class StationInitResponse {

    private static final int UPBOUND_STATION = 0;
    private static final int DOWNBOUND_STATION = 1;

    private final Long upboundStationId;
    private final String upboundStationName;
    private final Long downboundStationId;
    private final String downboundStationName;

    public StationInitResponse(final Long upboundStationId, final String upboundStationName, final Long downboundStationId, final String downboundStationName) {
        this.upboundStationId = upboundStationId;
        this.upboundStationName = upboundStationName;
        this.downboundStationId = downboundStationId;
        this.downboundStationName = downboundStationName;
    }

    public StationInitResponse(final List<Station> stations) {
        this(stations.get(UPBOUND_STATION).getId(), stations.get(UPBOUND_STATION).getName(), stations.get(DOWNBOUND_STATION).getId(), stations.get(DOWNBOUND_STATION).getName());
    }

    public Long getUpboundStationId() {
        return upboundStationId;
    }

    public String getUpboundStationName() {
        return upboundStationName;
    }

    public Long getDownboundStationId() {
        return downboundStationId;
    }

    public String getDownboundStationName() {
        return downboundStationName;
    }
}
