package subway.dto;

import subway.entity.LineEntity;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private int extraCharge;

    public LineResponse() {
    }

    public LineResponse(Long id, String name, String color, int extraCharge) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraCharge = extraCharge;
    }

    public static LineResponse of(LineEntity entity) {
        return new LineResponse(entity.getId(), entity.getName(), entity.getColor(),
            entity.getExtraCharge());
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

    public int getExtraCharge() {
        return extraCharge;
    }
}
