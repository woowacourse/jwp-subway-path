package subway.line.adapter.output.persistence;

import java.util.Objects;

public class LineEntity {
    private final Long id;
    private final String name;
    private final String color;
    private final Long extraCharge;
    
    public LineEntity(final String name, final String color, final Long extraCharge) {
        this(null, name, color, extraCharge);
    }
    
    public LineEntity(final Long id, final String name, final String color, final Long extraCharge) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraCharge = extraCharge;
    }
    
    public Long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getColor() {
        return color;
    }
    
    public Long getExtraCharge() {
        return extraCharge;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final LineEntity that = (LineEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(color, that.color) && Objects.equals(extraCharge, that.extraCharge);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, extraCharge);
    }
    
    @Override
    public String toString() {
        return "LineEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", extraCharge=" + extraCharge +
                '}';
    }
}
