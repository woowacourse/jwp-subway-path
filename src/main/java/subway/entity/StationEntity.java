package subway.entity;

public class StationEntity {
    private Long id;
    private String name;

    public StationEntity() {
    }

    public StationEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public StationEntity(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
