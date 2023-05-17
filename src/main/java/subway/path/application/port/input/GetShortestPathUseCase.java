package subway.path.application.port.input;

import subway.path.dto.GetShortestPathResponse;

public interface GetShortestPathUseCase {
    GetShortestPathResponse getShortestPath(String startStationName, String endStationName);
}
