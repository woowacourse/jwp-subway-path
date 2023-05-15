package subway.application.strategy.delete;

import org.springframework.stereotype.Component;
import subway.domain.Sections;
import subway.domain.Station;

import java.util.List;

@Component
public class SectionDeleter {

    private final List<DeleteStationStrategy> strategies;

    public SectionDeleter(List<DeleteStationStrategy> strategies) {
        this.strategies = strategies;
    }

    public void delete(Sections sections, Station targetStation) {
        for (DeleteStationStrategy strategy : strategies) {
            if (strategy.support(sections, targetStation)) {
                strategy.delete(sections, targetStation);
            }
        }
    }
}
