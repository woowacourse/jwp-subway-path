package subway.line.ui;

import java.util.List;
import java.util.stream.Collectors;
import subway.line.application.port.in.InterStationResponseDto;
import subway.line.application.port.in.LineResponseDto;
import subway.line.application.port.in.addinterstation.LineAddInterStationRequestDto;
import subway.line.application.port.in.create.LineCreateRequestDto;
import subway.line.application.port.in.update.LineUpdateRequestDto;
import subway.line.ui.dto.in.InterStationResponse;
import subway.line.ui.dto.in.LineAddStationRequest;
import subway.line.ui.dto.in.LineCreateRequest;
import subway.line.ui.dto.in.LineResponse;
import subway.line.ui.dto.in.LineUpdateInfoRequest;
import subway.line.ui.dto.in.LinesResponse;

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
