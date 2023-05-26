package subway.domain.farepolicy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class FarePolicyConfiguration {

    public static final int DISTANCE_BOUNDARY_10 = 10;
    public static final double UNIT_DISTANCE_FOR_BOUNDARY_10 = 5.0;
    public static final int DISTANCE_BOUNDARY_50 = 50;
    public static final double UNIT_DISTANCE_FOR_BOUNDARY_50 = 8.0;
    public static final int UNIT_OVER_FARE = 100;

    @Bean
    public FarePolicy farePolicy() {
        return new DefaultFarePolicy(List.of(
                new DistanceOverFarePolicy(DISTANCE_BOUNDARY_10, DISTANCE_BOUNDARY_50, UNIT_DISTANCE_FOR_BOUNDARY_10, UNIT_OVER_FARE),
                new DistanceOverFarePolicy(DISTANCE_BOUNDARY_50, Integer.MAX_VALUE, UNIT_DISTANCE_FOR_BOUNDARY_50, UNIT_OVER_FARE)
        ));
    }
}
