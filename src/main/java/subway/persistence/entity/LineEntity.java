package subway.persistence.entity;

import java.util.Objects;

public class LineEntity {
    private final Long id;
    private final String name;
    private final String upwardTerminus;
    private final String downwardTerminus;

    public LineEntity(String name, String upwardTerminus, String downwardTerminus) {
        this(null, name, upwardTerminus, downwardTerminus);
    }

    public LineEntity(Long id, String name, String upwardTerminus, String downwardTerminus) {
        this.id = id;
        this.name = name;
        this.upwardTerminus = upwardTerminus;
        this.downwardTerminus = downwardTerminus;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUpwardTerminus() {
        return upwardTerminus;
    }

    public String getDownwardTerminus() {
        return downwardTerminus;
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
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
