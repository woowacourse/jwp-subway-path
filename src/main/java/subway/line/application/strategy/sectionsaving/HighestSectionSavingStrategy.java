package subway.line.application.strategy.sectionsaving;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import subway.line.Line;
import subway.line.application.LineRepository;
import subway.line.domain.section.application.SectionRepository;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.station.Station;

@Component
@Order(4)
public class HighestSectionSavingStrategy implements SectionSavingStrategy {
    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;

    public HighestSectionSavingStrategy(SectionRepository sectionRepository, LineRepository lineRepository) {
        this.sectionRepository = sectionRepository;
        this.lineRepository = lineRepository;
    }

    @Override
    public boolean support(Line line, Station previousStation, Station nextStation, Distance distance) {
        return line.getHead().equals(nextStation);
    }

    @Override
    public long insert(Line line, Station previousStation, Station nextStation, Distance distance) {
        final var section = sectionRepository.insert(line.getId(), previousStation, nextStation, distance);
        lineRepository.updateHeadStation(line, previousStation);

        line.addSection(section);
        line.changeHead(previousStation);

        return section.getId();
    }
}
