package subway.application.strategy.insert;

import org.springframework.stereotype.Component;
import subway.domain.section.SingleLineSections;

import java.util.List;

@Component
public class SectionInserter {

    private final List<InsertStationStrategy> strategies;

    public SectionInserter(List<InsertStationStrategy> strategies) {
        this.strategies = strategies;
    }

    public Long insert(SingleLineSections sections, InsertSection insertSection) {
        Long newSectionId = null;

        for (InsertStationStrategy strategy : strategies) {
            if (strategy.support(sections, insertSection)) {
                newSectionId = strategy.insert(sections, insertSection);
                break;
            }
        }

        return newSectionId;
    }
}
