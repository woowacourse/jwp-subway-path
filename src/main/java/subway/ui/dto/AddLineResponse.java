package subway.ui.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.line.Line;
import subway.ui.line.dto.in.InterStationResponse;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddLineResponse {

    private Long id;
    private String name;
    private String color;
    private InterStationResponse interStation;

    public static AddLineResponse from(final Line line) {
        return new AddLineResponse(line.getId(), line.getName().getValue(), line.getColor().getValue(),
            InterStationResponse.from(line.getFirstInterStation()));
    }
}
