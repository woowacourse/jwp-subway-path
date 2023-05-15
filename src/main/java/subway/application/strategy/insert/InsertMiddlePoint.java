package subway.application.strategy.insert;

import org.springframework.stereotype.Component;
import subway.domain.Sections;

import java.util.List;

@Component
public class InsertMiddlePoint {

    private final List<InsertStrategy> strategies;

    public InsertMiddlePoint(List<InsertStrategy> strategies) {
        this.strategies = strategies;
    }

    public Long insert(Sections sections, InsertSection insertSection) {
        Long newSectionId = null;

        for (InsertStrategy strategy : strategies) {
            if (strategy.support(sections, insertSection)) {
                newSectionId = strategy.insert(sections, insertSection);
            }
        }

        return newSectionId;
    }
}
