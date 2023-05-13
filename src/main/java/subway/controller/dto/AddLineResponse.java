package subway.controller.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.line.Line;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddLineResponse {

    private Long id;
    private String name;
    private String color;
    private InterStationResponse interStation;

    public static AddLineResponse from(final Line line) {
        return new AddLineResponse(line.getId(), line.getName(), line.getColor(),
                InterStationResponse.from(line.getFirstInterStation()));
    }
}
