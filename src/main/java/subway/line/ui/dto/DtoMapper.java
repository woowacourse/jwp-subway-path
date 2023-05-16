package subway.line.ui.dto;

import subway.line.application.dto.LineCreationDto;
import subway.line.application.dto.StationAdditionToLineDto;
import subway.line.application.dto.StationDeletionFromLineDto;

class DtoMapper {

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
