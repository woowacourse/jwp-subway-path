package subway.domain.strategy;

import subway.domain.Section;
import subway.repository.LineRepository;

public class AddFirstSectionStrategy implements AddSectionStrategy {
    private final Section section;

    public AddFirstSectionStrategy(Section section) {
        this.section = section;
    }


    @Override
    public void execute(LineRepository lineRepository) {
        lineRepository.saveSection(section);
    }
}
