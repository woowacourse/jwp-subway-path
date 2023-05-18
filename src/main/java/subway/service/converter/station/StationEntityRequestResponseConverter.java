package subway.service.converter.station;

import subway.entity.StationEntity;
import subway.controller.dto.request.StationRequest;
import subway.controller.dto.response.StationResponse;

public class StationEntityRequestResponseConverter {

    public static StationEntity toEntity(final StationRequest request) {
        return new StationEntity(request.getName());
    }

    public static StationResponse toResponse(final StationEntity entity) {
        return new StationResponse(entity.getId(), entity.getName());
    }

}
