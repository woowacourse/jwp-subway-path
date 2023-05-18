package subway.dto.request;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class LineRequest {

    @NotNull("이름은 비어있을 수 없습니다.")
    private String name;
    @NotNull("색상은 비어있을 수 없습니다.")
    private String color;

    public LineRequest() {
    }

    public LineRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LineRequest that = (LineRequest) o;
        return Objects.equals(name, that.name) && Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color);
    }
}
