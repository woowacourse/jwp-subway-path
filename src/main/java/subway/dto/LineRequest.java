package subway.dto;

public class LineRequest {
    private final String name;
    private final String color;
    private final Integer additionalFare;


    public LineRequest(String name, String color, Integer additionalFare) {
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Integer getAdditionalFare() {
        return additionalFare;
    }
}
