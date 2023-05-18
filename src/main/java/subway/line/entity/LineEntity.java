package subway.line.entity;

import java.util.Objects;

public class LineEntity {

    private final Long id;
    private final String name;
    private final Integer additionalFare;

    public LineEntity(Long id, String name, int additionalFare) {
        this.id = id;
        this.name = name;
        this.additionalFare = additionalFare;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAdditionalFare() {
        return additionalFare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineEntity that = (LineEntity) o;
        return additionalFare == that.additionalFare && Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, additionalFare);
    }

    @Override
    public String toString() {
        return "LineEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", additionalFare=" + additionalFare +
                '}';
    }

    public static class Builder {

        private Long id;
        private String name;
        private Integer additionalFare;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder additionalFare(int additionalFare) {
            this.additionalFare = additionalFare;
            return this;
        }

        public LineEntity build() {
            return new LineEntity(id, name, additionalFare);
        }
    }
}
