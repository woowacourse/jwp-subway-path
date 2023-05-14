package subway.ui.dto;

import subway.application.dto.LineCreationDto;
import subway.application.dto.StationAdditionToLineDto;
import subway.application.dto.StationDeletionFromLineDto;

public class DtoMapper {

    private DtoMapper() {
    }

    public static LineCreationDto toLineCreationDto(LineCreationRequest lineCreationRequest) {
        return new LineCreationDto(
                lineCreationRequest.getLineName(),
                lineCreationRequest.getUpstreamName(),
                lineCreationRequest.getDownstreamName(),
                lineCreationRequest.getDistance()
        );
    }

    public static StationAdditionToLineDto toStationAdditionToLineDto(long lineId, StationAdditionRequest stationAdditionRequest) {
        return new StationAdditionToLineDto(
                lineId,
                stationAdditionRequest.getStationName(),
                stationAdditionRequest.getUpstreamName(),
                stationAdditionRequest.getDownstreamName(),
                stationAdditionRequest.getDistanceToUpstream()
        );
    }

    public static StationDeletionFromLineDto toStationRemovalFromLineDto(long lineId, long stationId) {
        return new StationDeletionFromLineDto(lineId, stationId);
    }
}
