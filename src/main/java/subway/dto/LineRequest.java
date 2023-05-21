package subway.dto;

public class LineRequest {

    private String name;
    private String color;
    private int extraCharge;
    private String upStation;
    private String downStation;
    private int distance;

    public LineRequest() {
    }

    public LineRequest(String name, String color, int extraCharge, String upStation,
        String downStation,
        int distance) {
        this.name = name;
        this.color = color;
        this.extraCharge = extraCharge;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getExtraCharge() {
        return extraCharge;
    }

    public String getUpStation() {
        return upStation;
    }

    public String getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }
}
