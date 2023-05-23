package subway.dao.entity;

import java.util.Objects;

public class LineEntity {

    private final Long id;
    private final String name;
    private final Integer extraFare;

    public LineEntity(Long id, String name, Integer extraFare) {
        this.id = id;
        this.name = name;
        this.extraFare = extraFare;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getExtraFare() {
        return extraFare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LineEntity that = (LineEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
