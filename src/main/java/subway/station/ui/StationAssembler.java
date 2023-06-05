package subway.station.ui;

import java.util.List;
import java.util.stream.Collectors;
import subway.station.application.port.in.StationCreateRequestDto;
import subway.station.application.port.in.StationInfoResponseDto;
import subway.station.application.port.in.StationInfoUpdateRequestDto;
import subway.station.ui.dto.in.StationCreateRequest;
import subway.station.ui.dto.in.StationInfoResponse;
import subway.station.ui.dto.in.StationInfosResponse;
import subway.station.ui.dto.in.StationUpdateInfoRequest;

public class StationAssembler {

    private StationAssembler() {
    }

    public static StationInfoUpdateRequestDto toUpdateStationInfoRequestDto(Long id,
            StationUpdateInfoRequest request) {
        return new StationInfoUpdateRequestDto(id, request.getName());
    }

    public static StationInfoResponse toStationInfoResponse(StationInfoResponseDto stationInfoById) {
        return new StationInfoResponse(stationInfoById.getId(), stationInfoById.getName());
    }

    public static StationInfosResponse toStationInfosResponse(List<StationInfoResponseDto> result) {
        return new StationInfosResponse(result.stream()
                .map(stationInfoResponseDto ->
                        new StationInfoResponse(stationInfoResponseDto.getId(), stationInfoResponseDto.getName()))
                .collect(Collectors.toList()));
    }

    public static StationCreateRequestDto toStationCreateRequestDto(StationCreateRequest request) {
        return new StationCreateRequestDto(request.getName());
    }
}
