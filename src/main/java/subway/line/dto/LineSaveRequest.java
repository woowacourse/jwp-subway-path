package subway.line.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import subway.line.domain.Line;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class LineSaveRequest {
    private final String name;
    private final String color;
    
    public Line toEntity() {
        return new Line(name, color);
    }
}
