package subway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.Line;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LineResponse {

    private Long id;
    private String name;
    private String color;

    public static LineResponse of(final Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor());
    }
}
