package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.path.PathRequest;
import subway.dto.path.PathResponse;

@Service
@Transactional
public class PathService {

    @Transactional(readOnly = true)
    public PathResponse findPath(PathRequest pathRequest) {
        return null;
    }
}
