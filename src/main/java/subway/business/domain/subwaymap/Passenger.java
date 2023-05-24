package subway.business.domain.subwaymap;

import java.util.Arrays;

public enum Passenger {
    GENERAL(1, "general"),
    YOUTH(0.8, "youth"),
    CHILDREN(0.5, "children"),
    SENIOR(0, "senior");

    private final double fareRate;
    private final String text;

    Passenger(double fareRate, String text) {
        this.fareRate = fareRate;
        this.text = text;
    }

    public static Passenger of(String passengerText) {
        return Arrays.stream(Passenger.values())
                .filter(passenger -> passenger.text.equals(passengerText))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format(
                        "잘못된 승객의 종류입니다. (입력 받은 승객의 종류: %s)",
                        passengerText
                )));
    }

    public double getFareRate() {
        return fareRate;
    }
}
