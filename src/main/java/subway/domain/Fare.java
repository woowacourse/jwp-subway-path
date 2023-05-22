package subway.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Fare {
    private static final String UNIT = "원";

    private int fare;
    private String unit;

    public Fare(int fare) {
        this.fare = fare;
        this.unit = UNIT;
    }
}
