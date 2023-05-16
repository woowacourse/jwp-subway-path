package subway.dao.entity;

import subway.domain.Station;

public class StationEntity {

    private Long id;
    private String name;

    public StationEntity(String name) {
        this(null, name);
    }

    public StationEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Station toDomain() {
        return new Station(id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
