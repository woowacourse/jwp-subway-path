package subway.domain.strategy;

import subway.repository.LineRepository;

public interface DeleteSectionStrategy {

    void execute(LineRepository lineRepository);
}
