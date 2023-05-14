package subway.line.dto;

import lombok.*;
import subway.line.domain.Line;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class LineSaveRequest {
    @NotBlank(message = "노선의 이름은 null 또는 빈값일 수 없습니다.")
    private String name;
    
    @NotBlank(message = "노선의 색상은 null 또는 빈값일 수 없습니다.")
    private String color;
    
    public Line toEntity() {
        return new Line(name, color);
    }
}
