package subway.application.path.usecase;

import subway.ui.dto.response.ShortestPathResponse;

public interface FindShortestPathUseCase {
    ShortestPathResponse findShortestPath(final Long startStationId, final Long arrivalStationId);
}
