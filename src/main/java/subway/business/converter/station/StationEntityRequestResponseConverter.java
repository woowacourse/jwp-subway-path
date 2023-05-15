package subway.business.converter.station;

import subway.persistence.entity.StationEntity;
import subway.presentation.dto.request.StationRequest;
import subway.presentation.dto.response.StationResponse;

public class StationEntityRequestResponseConverter {

    public static StationEntity toEntity(final StationRequest request) {
        return new StationEntity(request.getName());
    }

    public static StationResponse toResponse(final StationEntity entity) {
        return new StationResponse(entity.getId(), entity.getName());
    }

}
