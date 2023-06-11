package subway.domain.strategy;

import subway.domain.Section;
import subway.repository.LineRepository;

public class DeleteTerminalStrategy implements DeleteSectionStrategy{
    private final Section section;

    public DeleteTerminalStrategy(Section section) {
        this.section = section;
    }

    @Override
    public void execute(LineRepository lineRepository) {
        lineRepository.removeSectionById(section.getId());
    }
}
