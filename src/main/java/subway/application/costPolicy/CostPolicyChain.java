package subway.application.costPolicy;

import subway.domain.Path;

public interface CostPolicyChain {

    void setNext(CostPolicyChain next);

    long calculate(Path path, int age, long cost);
}
