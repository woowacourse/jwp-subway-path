package subway.domain;

import java.util.Objects;

public class StationDomain {

    private Long id;
    private String name;

    public StationDomain(final String name) {
        this(null, name);
    }

    public StationDomain(final Long id, final String name) {
        validate(name);
        this.id = id;
        this.name = name;
    }

    private void validate(final String name) {
        if (Objects.isNull(name) || name.isBlank()) {
            throw new IllegalArgumentException("지하철 역 이름은 null 또는 공백일 수 없습니다.");
        }
        if (name.length() > 10) {
            throw new IllegalArgumentException("이름은 1~10글자여야합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final StationDomain that = (StationDomain) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "StationDomain{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
