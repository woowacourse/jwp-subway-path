package subway.application.line.port.in;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LineUpdateRequestDto {

    private Long id;
    private String name;
    private String color;
}
