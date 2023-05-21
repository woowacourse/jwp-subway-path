package subway.line.dto;

import subway.line.domain.Line;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public class AddLineRequest {
    @NotBlank(message = "노선의 이름은 null 또는 빈값일 수 없습니다.")
    private String name;
    
    @NotBlank(message = "노선의 색상은 null 또는 빈값일 수 없습니다.")
    private String color;
    
    @NotNull(message = "노선의 추가 요금은 null일 수 없습니다.")
    @PositiveOrZero(message = "노선의 추가 요금은 음수일 수 없습니다.")
    private Long extraCharge;
    
    public AddLineRequest() {}
    
    public AddLineRequest(final String name, final String color, final Long extraCharge) {
        this.name = name;
        this.color = color;
        this.extraCharge = extraCharge;
    }
    
    public Line toEntity() {
        return new Line(name, color, extraCharge);
    }
    
    public String getName() {
        return name;
    }
    
    public String getColor() {
        return color;
    }
    
    public Long getExtraCharge() {
        return extraCharge;
    }
    
    @Override
    public String toString() {
        return "AddLineRequest{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", extraCharge=" + extraCharge +
                '}';
    }
}
