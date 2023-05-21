package subway.domain.fare;

import org.springframework.stereotype.Component;
import subway.domain.Line;
import subway.domain.path.PathEdgeProxy;

import java.util.List;

@Component
public final class LineAdditionalFareStrategyImpl implements LineAdditionalFareStrategy {

    @Override
    public int calculate(final List<PathEdgeProxy> lines) {
        return lines.stream()
                .map(PathEdgeProxy::getLine)
                .mapToInt(Line::getAdditionalFare)
                .max()
                .orElse(0);
    }
}
