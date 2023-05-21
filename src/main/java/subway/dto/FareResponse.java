package subway.dto;

public class FareResponse {

    private Integer fare;

    public FareResponse() {
    }

    public FareResponse(final Integer fare) {
        this.fare = fare;
    }

    public Integer getFare() {
        return fare;
    }
}
