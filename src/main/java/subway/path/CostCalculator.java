package subway.path;

import org.springframework.stereotype.Component;

@Component
public class CostCalculator {

    private static final int START_COST = 1250;
    private static final int FEE = 100;
    private static final int BASIC_COST_DISTANCE = 10;
    private static final int BASIC_ADDITIONAL_COST_END_BOUNDARY = 50;
    private static final int UNIT_OF_FIRST_DISTANCE = 5;
    private static final int UNIT_OF_SECOND_DISTANCE = 8;

    public int calculate(int distance) {
        int cost = START_COST;

        if (distance >= BASIC_COST_DISTANCE) {
            cost += getFirstStateCost(distance - BASIC_COST_DISTANCE);
        }
        if (distance >= BASIC_ADDITIONAL_COST_END_BOUNDARY) {
            cost += getSecondStateCost(distance - BASIC_ADDITIONAL_COST_END_BOUNDARY);
        }

        return cost;
    }

    private int getFirstStateCost(int distance) {
        int additionalDistance = Math.min(distance, BASIC_ADDITIONAL_COST_END_BOUNDARY - BASIC_COST_DISTANCE);
        return ((additionalDistance + UNIT_OF_FIRST_DISTANCE - 1) / UNIT_OF_FIRST_DISTANCE) * FEE;
    }

    private int getSecondStateCost(int distance) {
        return ((distance + UNIT_OF_SECOND_DISTANCE - 1) / UNIT_OF_SECOND_DISTANCE) * FEE;
    }

}
