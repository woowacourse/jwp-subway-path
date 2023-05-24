package subway.domain.line;

import java.util.Objects;
import java.util.regex.Pattern;
import org.springframework.util.Assert;

public class LineColor {
    private static final Pattern COLOR_REGEX = Pattern.compile("^bg-[a-z]+-([1-9]00)$");
    private static final int MAX_LINE_COLOR_LENGTH = 15;
    private final String color;

    public LineColor(String color) {
        validateBlank(color);
        validateLength(color);
        validateColorFormat(color);
        this.color = color;
    }

    private void validateBlank(String color) {
        Assert.hasText(color, "노선의 색은 빈 값이 될 수 없습니다.");
    }

    private void validateLength(String color) {
        if (color.length() >= MAX_LINE_COLOR_LENGTH) {
            throw new IllegalArgumentException("노선의 색은 " + MAX_LINE_COLOR_LENGTH + "글자를 초과할 수 없습니다.");
        }
    }

    private void validateColorFormat(String color) {
        if (!COLOR_REGEX.matcher(color).matches()) {
            throw new IllegalArgumentException("노선의 색은 bg-(소문자로 된 색 단어)-(1~9로 시작하는 100 단위의 수) 여야 합니다.");
        }
    }

    public String getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LineColor)) {
            return false;
        }

        LineColor lineColor = (LineColor) o;

        return Objects.equals(color, lineColor.color);
    }

    @Override
    public int hashCode() {
        return color != null ? color.hashCode() : 0;
    }
}
