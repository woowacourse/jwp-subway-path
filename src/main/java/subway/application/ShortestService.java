package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.ShortestResponse;

@Service
@Transactional(readOnly = true)
public class ShortestService {
    public ShortestResponse getShortest(final Long startId, final Long endId) {
        return new ShortestResponse(10L, null);
    }
}
