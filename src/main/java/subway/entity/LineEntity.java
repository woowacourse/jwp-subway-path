package subway.entity;

import subway.domain.line.LineName;
import subway.domain.line.Surcharge;

import java.util.Objects;

public class LineEntity {

    private final Long id;
    private final LineName lineName;
    private final Surcharge surcharge;

    public LineEntity(Long id, String lineName, int surcharge) {
        this.id = id;
        this.lineName = new LineName(lineName);
        this.surcharge = new Surcharge(surcharge);
    }

    public Long getId() {
        return id;
    }

    public String getLineName() {
        return lineName.getLineName();
    }

    public int getSurcharge() {
        return surcharge.getSurcharge();
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

    @Override
    public String toString() {
        return "LineEntity{" +
                "id=" + id +
                ", lineName=" + lineName +
                ", surcharge=" + surcharge +
                '}';
    }
}
