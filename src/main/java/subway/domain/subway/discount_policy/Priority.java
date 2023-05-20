package subway.domain.subway.discount_policy;

enum Priority {

    AGE_POLICY(1),
    ;

    private final int value;

    Priority(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
