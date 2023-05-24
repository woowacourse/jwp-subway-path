package subway.dto;

public class LineRequest {
    private String name;
    private String color;
    private int additionalFare;

    public LineRequest() {
    }

    public LineRequest(String name, String color, int additionalFare) {
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

    public int getAdditionalFare() {
        return additionalFare;
    }
}
