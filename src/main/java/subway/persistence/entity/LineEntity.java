package subway.persistence.entity;

import java.util.Objects;

public class LineEntity {
    private final Long id;
    private final String name;
    private final Long upwardTerminusId;
    private final Long downwardTerminusId;

    public LineEntity(String name, Long upwardTerminusId, Long downwardTerminusId) {
        this(null, name, upwardTerminusId, downwardTerminusId);
    }

    public LineEntity(Long id, String name, Long upwardTerminusId, Long downwardTerminusId) {
        this.id = id;
        this.name = name;
        this.upwardTerminusId = upwardTerminusId;
        this.downwardTerminusId = downwardTerminusId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getUpwardTerminusId() {
        return upwardTerminusId;
    }

    public Long getDownwardTerminusId() {
        return downwardTerminusId;
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
