package subway.mapper;

import subway.domain.Line;
import subway.dto.request.LineRequest;
import subway.dto.response.LineResponse;
import subway.dto.response.StationResponse;
import subway.entity.LineEntity;

import java.util.List;
import java.util.stream.Collectors;

public class LineMapper {

    private LineMapper() {
    }

    public static LineResponse toResponse(Line line) {
        List<StationResponse> stations = line.findOrderedStation().stream()
                .map(StationMapper::toResponse)
                .collect(Collectors.toList());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getCost(), stations);
    }

    public static Line toLine(LineRequest request) {
        return new Line(request.getName(), request.getColor(), request.getCost());
    }

    public static LineEntity toEntity(Line line) {
        return new LineEntity(line.getId(), line.getName(), line.getColor(), line.getCost());
    }
}
