package subway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.persistence.entity.LineEntity;

@Getter
@AllArgsConstructor
public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;

    public static LineResponse of(LineEntity line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor());
    }
}
