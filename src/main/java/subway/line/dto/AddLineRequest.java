package subway.line.dto;

import subway.line.domain.Line;

import javax.validation.constraints.NotBlank;

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
    public String toString() {
        return "AddLineRequest{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
