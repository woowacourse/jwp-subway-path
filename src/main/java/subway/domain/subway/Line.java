package subway.domain.subway;

public class Line {
    private Long id;
    private String name;
    private String color;
    private int additionalFare;

    public Line() {
    }

    public Line(String name, String color, int additionalFare) {
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
    }

    public Line(Long id, String name, String color, int additionalFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
    }

    public Long getId() {
        return id;
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
