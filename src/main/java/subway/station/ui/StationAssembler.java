package subway.station.ui;

import java.util.List;
import java.util.stream.Collectors;
import subway.station.application.dto.request.StationCreateRequestDto;
import subway.station.application.dto.request.StationInfoResponseDto;
import subway.station.application.dto.request.StationInfoUpdateRequestDto;
import subway.station.ui.dto.reqest.StationCreateRequest;
import subway.station.ui.dto.reqest.StationUpdateInfoRequest;
import subway.station.ui.dto.response.StationInfoResponse;
import subway.station.ui.dto.response.StationInfosResponse;

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
