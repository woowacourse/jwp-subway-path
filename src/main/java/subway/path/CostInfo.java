package subway.path;

public enum CostInfo {
    START_COST(1250),
    FEE(100),
    BASIC_COST_DISTANCE(10),
    BASIC_ADDITIONAL_COST_END_BOUNDARY(50),
    UNIT_OF_FIRST_DISTANCE(5),
    UNIT_OF_SECOND_DISTANCE(8);

    private final int value;

    CostInfo(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
