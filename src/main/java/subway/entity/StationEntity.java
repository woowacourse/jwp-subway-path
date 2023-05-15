package subway.entity;

public class StationEntity {

    private Long lineId;
    private Long id;
    private String name;

    public StationEntity(Long lineId, String name) {
        this.lineId = lineId;
        this.id = null;
        this.name = name;
    }

    public StationEntity(Long lineId, Long id, String name) {
        this.lineId = lineId;
        this.id = id;
        this.name = name;
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
