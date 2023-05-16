package subway.service.section.domain;

import java.util.List;

public interface FeePolicy {
    public int calculateFee(List<SectionEdge> edges);
}
