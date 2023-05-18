package subway.application;

import subway.dto.PathDto;

public interface PathService {
    public PathDto findShortest(long startId, long endId);


}
