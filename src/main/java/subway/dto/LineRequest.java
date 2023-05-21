package subway.dto;

public class LineRequest {
    private final String name;
    private final String color;
    private final Integer extraFee;

    private LineRequest() {
        this(null, null, null);
    }

    public LineRequest(String name, String color, Integer extraFee) {
        this.name = name;
        this.color = color;
        this.extraFee = extraFee;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Integer getExtraFee() {
        return extraFee;
    }
}
