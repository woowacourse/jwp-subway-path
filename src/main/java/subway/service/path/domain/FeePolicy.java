package subway.service.path.domain;

import java.util.List;

public interface FeePolicy {
    int calculateFee(List<SectionEdge> edges);
}
