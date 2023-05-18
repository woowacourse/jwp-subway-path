package subway.domain;

import java.util.Objects;

public class Line {
    
    private Long id;
    private String name;
    private String color;
    
    public Line() {
    }
    
    public Line(final String name, final String color) {
        this.name = name;
        this.color = color;
    }
    
    public Line(final Long id, final String name, final String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }
    
    public Long getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getColor() {
        return this.color;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.color);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Line line = (Line) o;
        return Objects.equals(this.id, line.id) && Objects.equals(this.name, line.name) && Objects.equals(this.color,
                line.color);
    }
}
