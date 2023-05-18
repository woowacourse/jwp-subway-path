package subway.application.path.usecase;

import subway.ui.dto.response.PathResponse;

public interface FindShortestPathUseCase {
    PathResponse findShortestPath(final Long startStationId, final Long arrivalStationId);
}
