package subway.ui.station;

import java.util.List;
import java.util.stream.Collectors;
import subway.application.station.port.in.StationCreateRequestDto;
import subway.application.station.port.in.StationInfoResponseDto;
import subway.application.station.port.in.StationInfoUpdateRequestDto;
import subway.ui.station.dto.in.StationCreateRequest;
import subway.ui.station.dto.in.StationUpdateInfoRequest;
import subway.ui.station.dto.out.StationInfoResponse;
import subway.ui.station.dto.out.StationInfosResponse;

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

    public static StationInfosResponse toStationInfosResponse(final List<StationInfoResponseDto> result) {
        return new StationInfosResponse(result.stream()
            .map(stationInfoResponseDto ->
                new StationInfoResponse(stationInfoResponseDto.getId(), stationInfoResponseDto.getName()))
            .collect(Collectors.toList()));
    }

    public static StationCreateRequestDto toStationCreateRequestDto(final StationCreateRequest request) {
        return new StationCreateRequestDto(request.getName());
    }
}
