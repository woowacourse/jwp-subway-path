package subway.ui;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum EndpointType {
    UP("up"),
    DOWN("down");

    private final String value;

    EndpointType(final String value) {
        this.value = value;
    }

    @JsonCreator
    public static EndpointType fromValue(final String value) {
        return Arrays.stream(values())
                .filter(type -> type.value.equals(value))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 종점 타입입니다."));
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
