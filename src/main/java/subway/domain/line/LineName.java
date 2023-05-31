package subway.domain.line;

import subway.exception.IllegalLineNameException;

import java.util.Objects;

public class LineName {

    public static final int MAX_LINE_NAME_LENGTH = 10;
    private final String lineName;

    public LineName(String lineName) {
        validate(lineName);
        this.lineName = lineName;
    }

    private void validate(String lineName) {
        if (lineName.length() > MAX_LINE_NAME_LENGTH) {
            throw new IllegalLineNameException();
        }
    }

    public String getLineName() {
        return lineName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LineName lineName1 = (LineName) o;
        return Objects.equals(lineName, lineName1.lineName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineName);
    }

    @Override
    public String toString() {
        return "LineName{" +
                "lineName='" + lineName + '\'' +
                '}';
    }
}
