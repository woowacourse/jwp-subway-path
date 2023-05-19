package subway.dto;

public class LineRequest {

    private String name;
    private String color;
    private Integer additionalFee;

    LineRequest() {
    }

    public LineRequest(String name, String color, Integer additionalFee) {
        this.name = name;
        this.color = color;
        this.additionalFee = additionalFee;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Integer getAdditionalFee() {
        return additionalFee;
    }
}
