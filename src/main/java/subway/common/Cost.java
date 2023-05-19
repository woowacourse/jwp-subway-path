package subway.common;

import org.springframework.stereotype.Component;

@Component
public class Cost {
    private static final int STANDARD_COST = 1250;
    private static final int START_BOUNDARY = 10;
    private static final int END_BOUNDARY = 50;
    private static final int UNIT_OF_FIRST_DISTANCE = 5;
    private static final int UNIT_OF_SECOND_DISTANCE = 8;
    private static final int FEE = 100;

    public int calculate(int distance) {
        int cost = STANDARD_COST;

        if (distance >= START_BOUNDARY) {
            cost += costCalculateFirstOption(distance - START_BOUNDARY);
        }
        if (distance >= END_BOUNDARY) {
            cost += costCalculateSecondOption(distance - END_BOUNDARY);
        }
        return cost;
    }

    private int costCalculateFirstOption(int distance) {
        if (distance >= END_BOUNDARY - START_BOUNDARY) {
            distance = END_BOUNDARY - START_BOUNDARY;
        }
        if (distance % UNIT_OF_FIRST_DISTANCE == 0 && distance != 0) {
            return (distance / UNIT_OF_FIRST_DISTANCE) * FEE;
        }
        return ((distance / UNIT_OF_FIRST_DISTANCE) + 1) * FEE;
    }

    private int costCalculateSecondOption(int distance) {
        if (distance % UNIT_OF_SECOND_DISTANCE == 0 && distance != 0) {
            return (distance / UNIT_OF_SECOND_DISTANCE) * FEE;
        }
        return ((distance / UNIT_OF_SECOND_DISTANCE) + 1) * FEE;
    }
}
