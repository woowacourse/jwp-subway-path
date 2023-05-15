package subway.entity;

public class LineEntity {

    private final Long id;
    private final String name;
    private final String color;
    private final Long headStation;

    public LineEntity(Long id, String name, String color, Long headStation) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.headStation = headStation;
    }

    public LineEntity(String name, String color, Long headStation) {
        this.id = 0L;
        this.name = name;
        this.color = color;
        this.headStation = headStation;
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

    public Long getHeadStation() {
        return headStation;
    }

}
