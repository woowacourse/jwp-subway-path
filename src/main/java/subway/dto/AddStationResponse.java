package subway.dto;

public class AddStationResponse {

    private Long sectionId;
    private String departureStation;
    private String arrivalStation;
    private int distance;

    private AddStationResponse(){
    }
    public AddStationResponse(final Long sectionId, final AddStationRequest addStationRequest) {
        this(sectionId, addStationRequest.getDepartureStation(), addStationRequest.getArrivalStation(),
                addStationRequest.getDistance());
    }

    public AddStationResponse(final Long sectionId, final String departureStation, final String arrivalStation,
                              final int distance) {
        this.sectionId = sectionId;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.distance = distance;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public String getDepartureStation() {
        return departureStation;
    }

    public String getArrivalStation() {
        return arrivalStation;
    }

    public int getDistance() {
        return distance;
    }
}
