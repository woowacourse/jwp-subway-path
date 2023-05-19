package subway.application.line.port.in.addinterstation;

import subway.application.line.port.in.LineResponseDto;

public interface LineAddInterStationUseCase {

    LineResponseDto addInterStation(final LineAddInterStationRequestDto requestDto);
}
