package subway.domain.strategy;

import subway.domain.Line;
import subway.repository.LineRepository;

public class SecondaryAddStrategy implements AddSectionStrategy {
    private final Line line;

    public SecondaryAddStrategy(Line line) {
        this.line = line;
    }

    @Override
    public void execute(LineRepository lineRepository) {
        lineRepository.removeSectionsByLineId(line.getId());
        lineRepository.saveAllSectionsInLine(line);
    }
}
