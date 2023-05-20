package subway.domain.line;

import java.util.Objects;

public class LineName {

    private final String lineName;

    private LineName(String lineName) {
        this.lineName = lineName;
    }

    public static LineName from(String lineName) {
        return new LineName(lineName);
    }

    public String getLineName() {
        return lineName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineName other = (LineName) o;
        return Objects.equals(lineName, other.lineName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineName);
    }
}
