package subway.dto;

import lombok.Getter;

@Getter
public class AddLineRequest {

    private String name;
    private String upstreamName;
    private String downstreamName;
    private int distance;

    public AddLineRequest(String name, String upstreamName, String downstreamName, int distance) {
        this.name = name;
        this.upstreamName = upstreamName;
        this.downstreamName = downstreamName;
        this.distance = distance;
    }
}
