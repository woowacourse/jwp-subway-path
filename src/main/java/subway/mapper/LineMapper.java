package subway.mapper;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Line;
import subway.dto.response.LineResponse;
import subway.dto.response.StationResponse;

public class LineMapper {

    public static LineResponse toResponse(Line line) {
        List<StationResponse> stations = line.findOrderedStation().stream()
                .map(StationMapper::toResponse)
                .collect(Collectors.toList());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stations);
    }
}
