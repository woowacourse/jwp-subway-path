package subway.line.dto;

import lombok.*;
import subway.line.domain.Line;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class LineSaveRequest {
    private String name;
    private String color;
    
    public Line toEntity() {
        return new Line(name, color);
    }
}
