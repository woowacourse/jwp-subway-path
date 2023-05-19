package subway.line.dto;

import subway.line.domain.Line;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class AddLineRequest {
    @NotBlank(message = "노선의 이름은 null 또는 빈값일 수 없습니다.")
    private String name;
    
    @NotBlank(message = "노선의 색상은 null 또는 빈값일 수 없습니다.")
    private String color;
    
    public AddLineRequest() {}
    
    public AddLineRequest(final String name, final String color) {
        this.name = name;
        this.color = color;
    }
    
    public Line toEntity() {
        return new Line(name, color);
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
        final AddLineRequest that = (AddLineRequest) o;
        return Objects.equals(name, that.name) && Objects.equals(color, that.color);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, color);
    }
    
    @Override
    public String toString() {
        return "AddLineRequest{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
