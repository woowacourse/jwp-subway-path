package subway.station.application.port.in;

import java.util.List;

public interface StationFindAllUseCase {

    List<StationInfoResponseDto> findAll();
}
