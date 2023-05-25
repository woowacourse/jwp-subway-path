package subway.line.ui;

import subway.line.application.dto.LineCreationDto;
import subway.line.application.dto.StationAdditionToLineDto;
import subway.line.application.dto.StationDeletionFromLineDto;
import subway.line.ui.dto.LineCreationRequest;
import subway.line.ui.dto.StationAdditionRequest;

class DtoMapper {

    private DtoMapper() {
    }

    public static LineCreationDto toLineCreationDto(LineCreationRequest lineCreationRequest) {
        return new LineCreationDto(
                lineCreationRequest.getLineName(),
                lineCreationRequest.getUpstreamName(),
                lineCreationRequest.getDownstreamName(),
                lineCreationRequest.getDistance(),
                lineCreationRequest.getAdditionalFare()
        );
    }

    public static StationAdditionToLineDto toStationAdditionToLineDto(long lineId, StationAdditionRequest stationAdditionRequest) {
        return new StationAdditionToLineDto(
                lineId,
                stationAdditionRequest.getStationName(),
                stationAdditionRequest.getUpstreamId(),
                stationAdditionRequest.getDownstreamId(),
                stationAdditionRequest.getDistanceToUpstream()
        );
    }

    public static StationDeletionFromLineDto toStationRemovalFromLineDto(long lineId, long stationId) {
        return new StationDeletionFromLineDto(lineId, stationId);
    }
}
