package subway.ui.line;

import java.util.List;
import java.util.stream.Collectors;
import subway.application.line.port.in.InterStationResponseDto;
import subway.application.line.port.in.LineAddInterStationRequestDto;
import subway.application.line.port.in.LineCreateRequestDto;
import subway.application.line.port.in.LineResponseDto;
import subway.application.line.port.in.LineUpdateRequestDto;
import subway.ui.line.dto.in.InterStationResponse;
import subway.ui.line.dto.in.LineAddStationRequest;
import subway.ui.line.dto.in.LineCreateRequest;
import subway.ui.line.dto.in.LineResponse;
import subway.ui.line.dto.in.LineUpdateInfoRequest;
import subway.ui.line.dto.in.LinesResponse;

public class LineAssembler {

    private LineAssembler() {
    }

    public static LineCreateRequestDto toLineCreateRequestDto(final LineCreateRequest request) {
        return new LineCreateRequestDto(
            request.getName(),
            request.getColor(),
            request.getUpStationId(),
            request.getDownStationId(),
            request.getDistance()
        );
    }

    public static LineResponse toLineResponse(final LineResponseDto responseDto) {
        return new LineResponse(
            responseDto.getId(),
            responseDto.getName(),
            responseDto.getColor(),
            toInterStationResponses(responseDto.getInterStations())
        );

    }

    private static List<InterStationResponse> toInterStationResponses(final List<InterStationResponseDto> responseDto) {
        return responseDto.stream()
            .map(LineAssembler::toInterStationResponse)
            .collect(Collectors.toList());
    }

    public static InterStationResponse toInterStationResponse(final InterStationResponseDto responseDto) {
        return new InterStationResponse(
            responseDto.getId(),
            responseDto.getUpStationId(),
            responseDto.getDownStationId(),
            responseDto.getDistance()
        );
    }

    public static LinesResponse toLinesResponse(final List<LineResponseDto> resultDtos) {
        return new LinesResponse(
            resultDtos.stream()
                .map(LineAssembler::toLineResponse)
                .collect(Collectors.toList())
        );
    }

    public static LineUpdateRequestDto toLineUpdateInfoRequestDto(final Long id, final LineUpdateInfoRequest request) {
        return new LineUpdateRequestDto(
            id,
            request.getName(),
            request.getColor()
        );
    }

    public static LineAddInterStationRequestDto toLineAddInterStationRequestDto(final Long id,
                                                                                final LineAddStationRequest request) {
        return new LineAddInterStationRequestDto(
            id,
            request.getUpStationId(),
            request.getDownStationId(),
            request.getNewStationId(),
            request.getDistance());
    }
}
