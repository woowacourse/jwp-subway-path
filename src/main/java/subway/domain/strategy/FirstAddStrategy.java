package subway.domain.strategy;

import subway.domain.Section;
import subway.repository.LineRepository;

public class FirstAddStrategy implements AddSectionStrategy {
    private final Section section;

    public FirstAddStrategy(Section section) {
        this.section = section;
    }


    @Override
    public void execute(LineRepository lineRepository) {
        lineRepository.saveSection(section);
    }
}
