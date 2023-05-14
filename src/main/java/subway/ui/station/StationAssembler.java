package subway.ui.station;

import subway.application.station.port.in.StationInfoUpdateRequestDto;
import subway.ui.station.dto.in.StationUpdateInfoRequest;

public class StationAssembler {

    private StationAssembler() {
    }

    public static StationInfoUpdateRequestDto toUpdateStationInfoRequestDto(final Long id,
                                                                            final StationUpdateInfoRequest request) {
        return new StationInfoUpdateRequestDto(id, request.getName());
    }
}
