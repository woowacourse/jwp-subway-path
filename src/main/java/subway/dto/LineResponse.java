package subway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.entity.LineEntity;

@Getter
@AllArgsConstructor
public class LineResponse {
    private Long id;
    private String name;
    private String color;

    public static LineResponse of(LineEntity line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor());
    }
}
