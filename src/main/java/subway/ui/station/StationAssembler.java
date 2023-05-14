package subway.ui.station;

import subway.application.station.port.in.StationInfoResponseDto;
import subway.application.station.port.in.StationInfoUpdateRequestDto;
import subway.ui.station.dto.in.StationUpdateInfoRequest;
import subway.ui.station.dto.out.StationInfoResponse;

public class StationAssembler {

    private StationAssembler() {
    }

    public static StationInfoUpdateRequestDto toUpdateStationInfoRequestDto(final Long id,
                                                                            final StationUpdateInfoRequest request) {
        return new StationInfoUpdateRequestDto(id, request.getName());
    }

    public static StationInfoResponse toStationInfoResponse(final StationInfoResponseDto stationInfoById) {
        return new StationInfoResponse(stationInfoById.getId(), stationInfoById.getName());
    }
}
