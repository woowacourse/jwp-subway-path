package subway.line.application.port.in.addinterstation;

import subway.line.application.port.in.LineResponseDto;

public interface LineAddInterStationUseCase {

    LineResponseDto addInterStation(final LineAddInterStationRequestDto requestDto);
}
