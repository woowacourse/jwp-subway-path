package subway.entity;

import java.util.Objects;

public class LineEntity {

    private final Long id;
    private final String name;

    public LineEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineEntity that = (LineEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "LineEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public static class Builder {

        private Long id;
        private String name;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public LineEntity build() {
            return new LineEntity(id, name);
        }
    }
}
