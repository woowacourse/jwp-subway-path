package subway.dto;

import subway.domain.vo.Section;

public class SectionResponse {
    private Long id;
    private String departure;
    private String arrival;
    private int distance;
    private SectionResponse(){

    }
    public SectionResponse(final Long id, final String departure, final String arrival, final int distance) {
        this.id = id;
        this.departure = departure;
        this.arrival = arrival;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public String getDeparture() {
        return departure;
    }

    public String getArrival() {
        return arrival;
    }

    public int getDistance() {
        return distance;
    }
}
