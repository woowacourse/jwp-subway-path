package subway.dao.entity;

import java.util.Objects;

public class LineEntity {
    private final Long id;
    private final String name;
    private final String color;
    private final long extraFee;

    public LineEntity(Long id, String name, String color, long extraFee) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFee = extraFee;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public long getExtraFee() {
        return extraFee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LineEntity)) {
            return false;
        }

        LineEntity that = (LineEntity) o;

        if (extraFee != that.extraFee) {
            return false;
        }
        if (!Objects.equals(id, that.id)) {
            return false;
        }
        if (!Objects.equals(name, that.name)) {
            return false;
        }
        return Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (int) (extraFee ^ (extraFee >>> 32));
        return result;
    }
}
