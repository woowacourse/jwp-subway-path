package subway.domain.fare.strategy;

import org.springframework.stereotype.Component;
import subway.domain.fare.FareInfo;
import subway.domain.path.PathEdgeProxy;

@Component
public final class LineAdditionalFareStrategy implements FareStrategy {

    @Override
    public FareInfo calculate(final FareInfo fareInfo) {
        final int additionalFare = fareInfo.getShortest().stream()
                .mapToInt(PathEdgeProxy::getAdditionalFare)
                .max()
                .orElse(0);

        return fareInfo.addFare(additionalFare);
    }
}
