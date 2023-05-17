package subway.dto;

public class FareResponse {
    private final String type;
    private final Integer fare;

    public FareResponse(String type, Integer fare) {
        this.type = type;
        this.fare = fare;
    }

    public String getType() {
        return type;
    }

    public Integer getFare() {
        return fare;
    }
}
