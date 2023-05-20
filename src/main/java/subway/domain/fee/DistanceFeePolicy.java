package subway.domain.fee;

import org.springframework.stereotype.Component;
import subway.domain.Section;

import java.util.ArrayList;
import java.util.List;

@Component
public class DistanceFeePolicy implements FeePolicy {

    private final static int DEFAULT_FEE = 1250;
    private final static int FIRST_TO_SECOND_FEE = 100;
    private final static int SECOND_TO_THIRD_FEE = 100;
    private final static int FIRST_BOUNDARY = 10;
    private final static int SECOND_BOUNDARY = 50;
    private final static int THIRD_BOUNDARY = Integer.MAX_VALUE;
    private final static int FIRST_TO_SECOND_UNIT = 5;
    private final static int SECOND_TO_THIRD_UNIT = 8;

    private final List<DistanceFee> distanceFees;

    public DistanceFeePolicy() {
        distanceFees = new ArrayList<>();
        distanceFees.add(new DistanceFee(FIRST_BOUNDARY, SECOND_BOUNDARY, FIRST_TO_SECOND_FEE, FIRST_TO_SECOND_UNIT));
        distanceFees.add(new DistanceFee(SECOND_BOUNDARY, THIRD_BOUNDARY, SECOND_TO_THIRD_FEE, SECOND_TO_THIRD_UNIT));
    }

    @Override
    public int calculateFee(List<Section> sections) {
        int totalDistance = sections.stream()
                .mapToInt(Section::getDistance)
                .sum();
        int fee = DEFAULT_FEE;
        for (DistanceFee distanceFee : distanceFees) {
            fee = fee + distanceFee.calculateSectionFee(totalDistance);
        }
        return fee;
    }
}
