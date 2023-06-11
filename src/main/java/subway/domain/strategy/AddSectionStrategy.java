package subway.domain.strategy;

import subway.repository.LineRepository;

public interface AddSectionStrategy {

    void execute(LineRepository lineRepository);
}
