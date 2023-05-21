package subway.path;

import org.springframework.stereotype.Component;

import static subway.path.CostInfo.BASIC_ADDITIONAL_COST_END_BOUNDARY;
import static subway.path.CostInfo.BASIC_COST_DISTANCE;
import static subway.path.CostInfo.FEE;
import static subway.path.CostInfo.START_COST;
import static subway.path.CostInfo.UNIT_OF_FIRST_DISTANCE;
import static subway.path.CostInfo.UNIT_OF_SECOND_DISTANCE;

@Component
public class CostCalculator implements CostCalculatePolicy {

    public int calculateAdult(int distance) {
        int cost = START_COST.getValue();

        if (distance >= BASIC_COST_DISTANCE.getValue()) {
            cost += getFirstStateCost(distance - BASIC_COST_DISTANCE.getValue());
        }
        if (distance >= BASIC_ADDITIONAL_COST_END_BOUNDARY.getValue()) {
            cost += getSecondStateCost(distance - BASIC_ADDITIONAL_COST_END_BOUNDARY.getValue());
        }

        return cost;
    }

    private int getFirstStateCost(int distance) {
        int additionalDistance = Math.min(distance, BASIC_ADDITIONAL_COST_END_BOUNDARY.getValue() - BASIC_COST_DISTANCE.getValue());
        return ((additionalDistance + UNIT_OF_FIRST_DISTANCE.getValue() - 1) / UNIT_OF_FIRST_DISTANCE.getValue()) * FEE.getValue();
    }

    private int getSecondStateCost(int distance) {
        return ((distance + UNIT_OF_SECOND_DISTANCE.getValue() - 1) / UNIT_OF_SECOND_DISTANCE.getValue()) * FEE.getValue();
    }

}
