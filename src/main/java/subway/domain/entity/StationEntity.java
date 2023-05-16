package subway.domain.entity;

import subway.domain.vo.Name;

import java.util.Objects;

public class StationEntity {

    private final Long id;
    private Name name;

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

    public void updateName(final String name) {
        this.name = Name.from(name);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final StationEntity that = (StationEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
