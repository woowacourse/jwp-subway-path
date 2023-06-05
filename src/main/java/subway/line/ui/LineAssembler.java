package subway.line.ui;

import java.util.List;
import java.util.stream.Collectors;
import subway.interstation.ui.dto.response.InterStationResponse;
import subway.line.application.dto.request.LineAddInterStationRequestDto;
import subway.line.application.dto.request.LineCreateRequestDto;
import subway.line.application.dto.request.LineUpdateRequestDto;
import subway.line.application.dto.response.InterStationResponseDto;
import subway.line.application.dto.response.LineResponseDto;
import subway.line.ui.dto.request.LineAddStationRequest;
import subway.line.ui.dto.request.LineCreateRequest;
import subway.line.ui.dto.request.LineUpdateInfoRequest;
import subway.line.ui.dto.response.LineResponse;
import subway.line.ui.dto.response.LinesResponse;

public class LineAssembler {

    private LineAssembler() {
    }

    public static LineCreateRequestDto toLineCreateRequestDto(LineCreateRequest request) {
        return new LineCreateRequestDto(
                request.getName(),
                request.getColor(),
                request.getUpStationId(),
                request.getDownStationId(),
                request.getDistance()
        );
    }

    public static LineResponse toLineResponse(LineResponseDto responseDto) {
        return new LineResponse(
                responseDto.getId(),
                responseDto.getName(),
                responseDto.getColor(),
                toInterStationResponses(responseDto.getInterStations())
        );

    }

    private static List<InterStationResponse> toInterStationResponses(List<InterStationResponseDto> responseDto) {
        return responseDto.stream()
                .map(LineAssembler::toInterStationResponse)
                .collect(Collectors.toList());
    }

    public static InterStationResponse toInterStationResponse(InterStationResponseDto responseDto) {
        return new InterStationResponse(
                responseDto.getId(),
                responseDto.getUpStationId(),
                responseDto.getDownStationId(),
                responseDto.getDistance()
        );
    }

    public static LinesResponse toLinesResponse(List<LineResponseDto> resultDtos) {
        return new LinesResponse(
                resultDtos.stream()
                        .map(LineAssembler::toLineResponse)
                        .collect(Collectors.toList())
        );
    }

    public static LineUpdateRequestDto toLineUpdateInfoRequestDto(Long id, LineUpdateInfoRequest request) {
        return new LineUpdateRequestDto(
                id,
                request.getName(),
                request.getColor()
        );
    }

    public static LineAddInterStationRequestDto toLineAddInterStationRequestDto(Long id,
            LineAddStationRequest request) {
        return new LineAddInterStationRequestDto(
                id,
                request.getUpStationId(),
                request.getDownStationId(),
                request.getNewStationId(),
                request.getDistance());
    }
}
