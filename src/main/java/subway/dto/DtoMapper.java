package subway.dto;

import subway.Entity.LineEntity;
import subway.Entity.StationEntity;
import subway.dto.response.LineResponse;
import subway.dto.response.LineStationsResponse;
import subway.dto.response.PathResponse;
import subway.dto.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class DtoMapper {

    public static StationResponse convertToStationResponse(StationEntity stationEntity) {
        return new StationResponse(stationEntity.getId(), stationEntity.getName());
    }

    public static LineResponse convertToLineResponse(LineEntity lineEntity) {
        return new LineResponse(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor());
    }

    public static List<StationResponse> convertToStationResponses(List<StationEntity> stationEntities) {
        return stationEntities.stream()
                .map(DtoMapper::convertToStationResponse)
                .collect(Collectors.toList());
    }

    public static LineStationsResponse convertToLineStationsResponse(
            LineEntity lineEntity, List<StationEntity> stationEntities
    ) {
        return new LineStationsResponse(
                convertToLineResponse(lineEntity),
                convertToStationResponses(stationEntities)
        );
    }

    public static PathResponse convertToPathResponse(List<StationEntity> stationEntities, int distance, int fare) {
        List<StationResponse> stationResponses = convertToStationResponses(stationEntities);
        return new PathResponse(stationResponses, distance, fare);
    }

}
