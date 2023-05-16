package subway.domain.lineDetail.domain;

import java.util.Objects;

public class LineDetail {

    private Long id;
    private String name;
    private String color;

    private LineDetail() {
    }

    public LineDetail(final Long id, final String name, final String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public LineDetail(final String name, final String color) {
        this.name = name;
        this.color = color;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineDetail lineDetail = (LineDetail) o;
        return Objects.equals(id, lineDetail.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
