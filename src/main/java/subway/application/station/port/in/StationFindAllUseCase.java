package subway.application.station.port.in;

import java.util.List;

public interface StationFindAllUseCase {

    List<StationInfoResponseDto> findAll();
}
