package subway.application;

import subway.dto.response.PathResponse;

public interface PathService {
    public PathResponse findShortest(long startId, long endId);


}
