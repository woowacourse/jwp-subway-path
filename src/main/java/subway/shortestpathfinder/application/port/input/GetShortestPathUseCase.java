package subway.shortestpathfinder.application.port.input;

import subway.shortestpathfinder.domain.AgeGroupFeeCalculator;
import subway.shortestpathfinder.dto.GetShortestPathResponse;

public interface GetShortestPathUseCase {
    GetShortestPathResponse getShortestPath(String startStationName, String endStationName, AgeGroupFeeCalculator ageGroupFeeCalculator);
}
