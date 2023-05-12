package subway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StationRequest {
    @JsonProperty("name")
    private String name;
    @JsonProperty("create_type")
    private String createType;
    @JsonProperty("line")
    private String line;
    @JsonProperty("up_endpoint")
    private boolean upEndpoint;
    @JsonProperty("down_endpoint")
    private boolean downEndpoint;
    @JsonProperty("previous_station")
    private String previousStation;
    @JsonProperty("previous_distance")
    private Integer previousDistance;
    @JsonProperty("next_station")
    private String nextStation;
    @JsonProperty("next_distance")
    private Integer nextDistance;

    public StationRequest(String name, String createType, String line, boolean upEndpoint, boolean downEndpoint, String previousStation, String nextStation, Integer previousDistance, Integer nextDistance) {
        this.name = name;
        this.createType = createType;
        this.line = line;
        this.upEndpoint = upEndpoint;
        this.downEndpoint = downEndpoint;
        this.previousStation = previousStation;
        this.nextStation = nextStation;
        this.previousDistance = previousDistance;
        this.nextDistance = nextDistance;
    }

//    public StationRequest(String name, CreateType createType, String line, boolean upEndpoint, boolean downEndpoint, String previousStation, String nextStation, Integer previousDistance, Integer nextDistance) {
//        this.name = name;
//        this.createType = createType;
//        this.line = line;
//        this.upEndpoint = upEndpoint;
//        this.downEndpoint = downEndpoint;
//        this.previousStation = previousStation;
//        this.nextStation = nextStation;
//        this.previousDistance = previousDistance;
//        this.nextDistance = nextDistance;
//    }

    public String getName() {
        return name;
    }

    public String getCreateType() {
        return createType;
    }

    public String getLine() {
        return line;
    }

    public boolean isUpEndpoint() {
        return upEndpoint;
    }

    public boolean isDownEndpoint() {
        return downEndpoint;
    }

    public String getPreviousStation() {
        return previousStation;
    }

    public String getNextStation() {
        return nextStation;
    }

    public Integer getPreviousDistance() {
        return previousDistance;
    }

    public Integer getNextDistance() {
        return nextDistance;
    }
}
