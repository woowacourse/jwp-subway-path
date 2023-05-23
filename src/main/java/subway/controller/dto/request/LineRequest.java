package subway.controller.dto.request;

public class LineRequest {

    private String name;
    private String color;
    private Long charge;

    private LineRequest() {
    }

    public LineRequest(final String name, final String color, final Long charge) {
        this.name = name;
        this.color = color;
        this.charge = charge;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getCharge() {
        return charge;
    }
}
