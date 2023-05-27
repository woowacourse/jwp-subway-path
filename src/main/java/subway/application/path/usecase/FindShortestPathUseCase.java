package subway.application.path.usecase;

import subway.ui.dto.response.ShortestPathResponse;

public interface FindShortestPathUseCase {
    ShortestPathResponse findShortestPath(Long startStationId, Long arrivalStationId, int passengerAge);
}
