package subway.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class AddStationRequest {

    private String addStationName;
    private String lineName;
    private String upstreamName;
    private String downstreamName;
    private int distanceToUpstream;

    public AddStationRequest(String addStationName, String lineName, String upstreamName, String downstreamName, int distanceToUpstream) {
        this.addStationName = addStationName;
        this.lineName = lineName;
        this.upstreamName = upstreamName;
        this.downstreamName = downstreamName;
        this.distanceToUpstream = distanceToUpstream;
    }
}
