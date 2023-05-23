package subway.application.costpolicy;

import subway.domain.Path;
import subway.domain.vo.Age;

public interface CostPolicyChain {

    void setNext(CostPolicyChain next);

    long calculate(Path path, Age age, long cost);
}
