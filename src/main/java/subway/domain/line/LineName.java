package subway.domain.line;

import java.util.Objects;
import org.springframework.util.Assert;

public class LineName {
    private static final int MAX_LINE_NAME_LENGTH = 15;
    private final String name;

    public LineName(String name) {
        validateBlank(name);
        validateLength(name);
        this.name = name;
    }

    private void validateBlank(String name) {
        Assert.hasText(name, "노선의 이름은 빈 값이 될 수 없습니다.");
    }

    private void validateLength(String name) {
        if (name.length() >= MAX_LINE_NAME_LENGTH) {
            throw new IllegalArgumentException("노선의 이름은 " + MAX_LINE_NAME_LENGTH + "글자를 초과할 수 없습니다.");
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LineName)) {
            return false;
        }

        LineName lineName = (LineName) o;

        return Objects.equals(name, lineName.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
