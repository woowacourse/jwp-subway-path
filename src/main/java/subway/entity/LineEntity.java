package subway.entity;

public class LineEntity {

    private final Long id;
    private final String name;
    private final String color;
    private final int extraCharge;
    private final Long headStation;

    public LineEntity(Long id, String name, String color, int extraCharge, Long headStation) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraCharge = extraCharge;
        this.headStation = headStation;
    }

    public LineEntity(String name, String color, int extraCharge, Long headStation) {
        this(0L, name, color, extraCharge, headStation);
    }

    public LineEntity(String name, String color, int extraCharge) {
        this(0L, name, color, extraCharge,null);
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public int getExtraCharge() {
        return extraCharge;
    }

    public Long getHeadStation() {
        return headStation;
    }
}
