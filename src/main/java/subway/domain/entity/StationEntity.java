package subway.domain.entity;

import subway.domain.vo.Name;

public class StationEntity {

    private final Long id;
    private final Name name;

    private StationEntity(final Long id, final Name name) {
        this.id = id;
        this.name = name;
    }

    public static StationEntity of(final String name) {
        return new StationEntity(null, Name.from(name));
    }

    public static StationEntity of(final Long id, final String name) {
        return new StationEntity(id, Name.from(name));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

}
