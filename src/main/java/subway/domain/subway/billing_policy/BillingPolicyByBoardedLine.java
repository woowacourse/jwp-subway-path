package subway.domain.subway.billing_policy;

import java.util.List;
import org.springframework.stereotype.Component;
import subway.domain.Path;
import subway.domain.line.Line;

@Component
public class BillingPolicyByBoardedLine implements BillingPolicy {

    @Override
    public Fare calculateFare(final Path path) {
        final List<Line> borderedLines = path.getBorderedLines();
        return new Fare(
                borderedLines.stream()
                        .mapToInt(Line::getExtraFare)
                        .max()
                        .orElse(0)
        );
    }
}
